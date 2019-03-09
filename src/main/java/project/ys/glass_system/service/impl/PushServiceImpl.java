package project.ys.glass_system.service.impl;

import org.springframework.stereotype.Service;
import project.ys.glass_system.model.p.bean.BaseChart;
import project.ys.glass_system.model.p.bean.BaseEntry;
import project.ys.glass_system.model.s.dao.ProductDao;
import project.ys.glass_system.model.s.dao.ProductNoteDao;
import project.ys.glass_system.model.s.entity.Products;
import project.ys.glass_system.service.PushService;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;

import static project.ys.glass_system.model.p.bean.BaseChart.line_chart;
import static project.ys.glass_system.util.LocalDateUtils.*;

@Service
@Transactional
public class PushServiceImpl implements PushService {


    @Resource
    ProductDao productDao;

    @Resource
    ProductNoteDao productNoteDao;

    @Override
    public Map<String, Object> packDailyData(LocalDate date) {
        Map<String, Object> dailyData = new HashMap<>();
        dailyData.put("Item1", packDailyProduceCount(date));
        dailyData.put("Item2", packDailyProduceCountList(date));
        dailyData.put("Item3", packDailyProduceCount(date));
        dailyData.put("Item4", packDailyProduceCountList(date));
        return dailyData;
    }

    private final String[] sixDivideTimes = {"00:00:00", "04:00:00", "08:00:00", "12:00:00", "16:00:00", "20:00:00", "23:59:59"};

    @Override
    public BaseChart packDailyProduceCount(LocalDate date) {
        BaseChart produceDate = new BaseChart();
        List<BaseEntry> yValues = new ArrayList<>();
        produceDate.setTitle("产品生产总量统计");
        produceDate.setLabel("生产总量");
        produceDate.setChart_type(line_chart);
        produceDate.setOnly(true);
        produceDate.setxValues(new String[]{"0时", "4时", "8时", "12时", "16时", "20时", "24时"});
        for (int i = 0, j = i + 1; j < sixDivideTimes.length; i++, j++) {
            List<Products> products = productDao.findProductsByDateBetween(stringToLocalDateTime(date, sixDivideTimes[i]), stringToLocalDateTime(date, sixDivideTimes[j]));
            int success = 0;
            for (Products product : products) {
                success += product.getPlan() - product.getFail();
            }
            yValues.add(new BaseEntry(j, success));
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
        produceDate.setTitle("产品生产总量统计");
        produceDate.setLabels(Arrays.asList("生产数", "残片数"));
        produceDate.setChart_type(line_chart);
        produceDate.setOnly(false);
        produceDate.setxValues(new String[]{"0时", "4时", "8时", "12时", "16时", "20时", "24时"});
        for (int i = 0, j = i + 1; j < sixDivideTimes.length; i++, j++) {
            List<Products> products = productDao.findProductsByDateBetween(stringToLocalDateTime(date, sixDivideTimes[i]), stringToLocalDateTime(date, sixDivideTimes[j]));
            int plan = 0, fail = 0;
            for (Products product : products) {
                plan += product.getPlan();
                fail += product.getFail();
            }
            yValue1.add(new BaseEntry(j, plan));
            yValue2.add(new BaseEntry(j, fail));
        }
        yValues.add(yValue1);
        yValues.add(yValue2);
        produceDate.setyListValues(yValues);
        return produceDate;
    }
}
