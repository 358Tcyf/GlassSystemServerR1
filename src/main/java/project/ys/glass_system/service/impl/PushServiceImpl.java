package project.ys.glass_system.service.impl;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Service;
import project.ys.glass_system.model.p.bean.BaseChart;
import project.ys.glass_system.model.p.bean.BaseEntry;
import project.ys.glass_system.model.p.entity.Push;
import project.ys.glass_system.model.s.dao.ProductDao;
import project.ys.glass_system.model.s.dao.ProductNoteDao;
import project.ys.glass_system.model.s.entity.Glass;
import project.ys.glass_system.model.s.entity.ProductNotes;
import project.ys.glass_system.model.s.entity.Products;
import project.ys.glass_system.service.PushService;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static project.ys.glass_system.model.p.bean.BaseChart.*;
import static project.ys.glass_system.util.LocalDateUtils.*;

@Service
@Transactional
public class PushServiceImpl implements PushService {


    @Resource
    ProductDao productDao;

    @Resource
    ProductNoteDao productNoteDao;

    @Resource
    GlassServiceImpl glassService;

    @Override
    public Push packDailyData(LocalDate date) {
        LocalDateTime now = LocalDateTime.now();
        long time = localDateTimeToMilli(now);
        System.out.println("Push CreateTime ->" + time);
        Push push = new Push(time);
        List<BaseChart> content = new ArrayList<>();
        content.add(packDailyProduceCountList(date));
        content.add(packDailyCountOfModel(date));
        content.add(packDailyProduceQualityList(date));
        content.add(packDailyConsume(date));
        push.setContent(JSON.toJSONString(content));
        push.setDefaultSubMenu(content.get(0).getSubmenu());
        push.setTitle(dateToStr(date, DATE_FORMAT_CN) + "推送数据");
        return push;
    }

    private final String[] sixDivideTimes = {"00:00:00", "04:00:00", "08:00:00", "12:00:00", "16:00:00", "20:00:00", "23:59:59"};

    @Override
    public BaseChart packDailyProduceCount(LocalDate date) {
        BaseChart produceDate = new BaseChart();
        List<BaseEntry> yValues = new ArrayList<>();
        produceDate.setMenu("生产信息");
        produceDate.setSubmenu("生产量");
        produceDate.setTitle("产品生产总量统计");
        produceDate.setLabel("生产总量");
        produceDate.setChart_type(bar_chart);
        produceDate.setOnly(true);
        produceDate.setxValues(new String[]{"0时", "4时", "8时", "12时", "16时", "20时", "24时"});
        for (int i = 0, j = i + 1; j < sixDivideTimes.length; i++, j++) {
            List<Products> products = productDao.findProductsByDateBetween(stringToLocalDateTime(date, sixDivideTimes[i]), stringToLocalDateTime(date, sixDivideTimes[j]));
            int success = 0;
            for (Products product : products) {
                success += product.getPlan() - product.getFail();
            }
            yValues.add(new BaseEntry((float) j, (float) success));
        }
        produceDate.setyValues(yValues);
        return produceDate;
    }

    @Override
    public BaseChart packDailyProduceCountList(LocalDate date) {
        BaseChart produceDate = new BaseChart();
        List<List<BaseEntry>> yValues = new ArrayList<>();
        List<BaseEntry> yValue1 = new ArrayList<>();
        List<BaseEntry> yValue2 = new ArrayList<>();
        produceDate.setMenu("生产信息");
        produceDate.setSubmenu("生产量");
        produceDate.setTitle("产品生产总量统计");
        produceDate.setLabels(Arrays.asList("生产数", "残片数"));
        produceDate.setChart_type(bar_chart);
        produceDate.setOnly(true);
        produceDate.setxValues(new String[]{"0时", "4时", "8时", "12时", "16时", "20时", "24时"});
        for (int i = 0, j = i + 1; j < sixDivideTimes.length; i++, j++) {
            List<Products> products = productDao.findProductsByDateBetween(stringToLocalDateTime(date, sixDivideTimes[i]), stringToLocalDateTime(date, sixDivideTimes[j]));
            int plan = 0, fail = 0;
            for (Products product : products) {
                plan += product.getPlan();
                fail += product.getFail();
            }
            yValue1.add(new BaseEntry((float) j, (float) plan));
            yValue2.add(new BaseEntry((float) j, (float) fail));
        }
        yValues.add(yValue1);
        yValues.add(yValue2);
        produceDate.setyListValues(yValues);
        return produceDate;
    }

    @Override
    public BaseChart packDailyCountOfModel(LocalDate date) {
        BaseChart produceDate = new BaseChart();
        List<BaseEntry> yValues = new ArrayList<>();
        produceDate.setMenu("生产信息");
        produceDate.setSubmenu("生产型号统计");
        produceDate.setTitle("各型号生产统计");
        produceDate.setDescription("各型号玻璃生产数量");
        produceDate.setLabel("各型号玻璃生产数量");
        produceDate.setChart_type(pie_chart);
        produceDate.setOnly(true);
        String[] labels = glassService.xValues();
        produceDate.setLabels(Arrays.asList(labels));
        produceDate.setxValues(labels);
        for (int i = 0; i < labels.length; i++) {
            List<Products> products = productDao.findProductsByModel(glassService.findByModel(labels[i]));
            int success = 0;
            for (Products product : products) {
                success += product.getPlan() - product.getFail();
            }
            yValues.add(new BaseEntry(labels[i], (float) success));
        }
        produceDate.setyValues(yValues);
        return produceDate;
    }

    @Override
    public BaseChart packDailyProduceQualityList(LocalDate date) {
        BaseChart produceDate = new BaseChart();
        List<List<BaseEntry>> yValues = new ArrayList<>();
        List<BaseEntry> yValue1 = new ArrayList<>();
        List<BaseEntry> yValue2 = new ArrayList<>();
        List<BaseEntry> yValue3 = new ArrayList<>();
        produceDate.setMenu("生产信息");
        produceDate.setSubmenu("生产质量");
        produceDate.setTitle("产品生产总量统计");
        produceDate.setLabels(Arrays.asList("镀膜成功率", "钢化成功率", "残片率"));
        produceDate.setChart_type(bar_chart);
        produceDate.setOnly(false);
        produceDate.setxValues(glassService.xValues());
        List<Glass> glasses = glassService.findAll();
        for (int i = 0; i < glasses.size(); i++) {
            List<Products> products = productDao.findProductsByModel(glasses.get(i));
            int coat = 0, harden = 0, fail = 0, plan = 0;
            for (Products product : products) {
                coat += product.getCoat();
                harden += product.getHarden();
                fail += product.getFail();
                plan += product.getPlan();
            }
            yValue1.add(new BaseEntry((float) i + 1, (float) coat * 100 / (float) plan));
            yValue2.add(new BaseEntry((float) i + 1, (float) harden * 100 / (float) plan));
            yValue3.add(new BaseEntry((float) i + 1, (float) fail * 100 / (float) plan));
        }

        yValues.add(yValue1);
        yValues.add(yValue2);
        yValues.add(yValue3);
        produceDate.setyListValues(yValues);
        return produceDate;
    }

    @Override
    public BaseChart packDailyConsume(LocalDate date) {
        BaseChart produceDate = new BaseChart();
        List<BaseEntry> yValues = new ArrayList<>();
        produceDate.setMenu("生产信息");
        produceDate.setSubmenu("生产能耗");
        produceDate.setTitle("生产能耗统计");
        produceDate.setDescription("各类型生产能耗");
        produceDate.setLabels(Arrays.asList(new String[]{"电消耗", "煤消耗", "水消耗"}));
        produceDate.setChart_type(ring_chart);
        produceDate.setOnly(true);
        String[] labels = new String[]{"电消耗", "煤消耗", "水消耗"};
        produceDate.setxValues(labels);
        ProductNotes productNote = productNoteDao.findByDate(date);
        yValues.add(new BaseEntry(labels[0], (float) productNote.getElectricity()));
        yValues.add(new BaseEntry(labels[1], (float) productNote.getCoal()));
        yValues.add(new BaseEntry(labels[2], (float) productNote.getWater()));
        produceDate.setyValues(yValues);
        return produceDate;
    }
}
