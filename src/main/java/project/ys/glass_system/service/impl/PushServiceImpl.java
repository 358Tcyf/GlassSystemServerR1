package project.ys.glass_system.service.impl;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Service;
import project.ys.glass_system.model.p.bean.BaseChart;
import project.ys.glass_system.model.p.bean.BaseEntry;
import project.ys.glass_system.model.p.dao.UserDao;
import project.ys.glass_system.model.p.entity.Push;
import project.ys.glass_system.model.p.entity.PushSet;
import project.ys.glass_system.model.p.entity.Tag;
import project.ys.glass_system.model.p.entity.User;
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
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static project.ys.glass_system.getui.GetuiUtil.sendSingleMessage;
import static project.ys.glass_system.getui.GetuiUtil.transmissionTemplate;
import static project.ys.glass_system.model.p.bean.BaseChart.*;
import static project.ys.glass_system.model.p.entity.PushSet.getTime;
import static project.ys.glass_system.model.p.entity.Tag.*;
import static project.ys.glass_system.util.LocalDateUtils.*;

@Service
@Transactional
public class PushServiceImpl implements PushService {

    @Resource
    UserDao userDao;

    @Resource
    ProductDao productDao;

    @Resource
    ProductNoteDao productNoteDao;

    @Resource
    GlassServiceImpl glassService;

    @Override
    public void pushEveryUser(LocalDate date) {
        List<User> userList = userDao.findAll();
        for (User user : userList) {
            PushSet set = user.getPushSet();
            if (set != null) {
                if (set.isCommonSwitch()) {
                    if (set.isPushSwitch()) {
                        LocalDateTime dateTime = LocalDateTime.now();
                        long localDateTimeToMilli = localDateTimeToMilli(dateTime);
                        int pushTime = getTime(set.getTime());
                        int nowTime = LocalTime.now().getHour();
                        if (set.getStart() == 0 || set.getEnd() == 0) {
                            System.out.println(user.getName() + "的推送起止时间设置不合法");
                        } else if (!(localDateTimeToMilli > set.getStart() && localDateTimeToMilli < set.getEnd())) {
                            System.out.println(user.getName() + "的推送不在推送时间段内");
                        } else if ((nowTime + 1) % pushTime == 0) {
                            System.out.println(user.getName() + "的推送在推送时间段内");
                            System.out.println("系统于" + LocalDateTime.now() + "发布了给：" + user.getName() + "的推送");
                            Push push = packDailyData(date, set.getTags());
                            if (push != null) {
                                String message = JSON.toJSONString(push);
                                System.out.println(push);
                                sendSingleMessage(1, user.getNo(), transmissionTemplate(message));
                            }
                        } else {
                            System.out.println("现在是" + nowTime + "时");
                            int h = 0;
                            for (h = 0; h < nowTime + 1; h += pushTime) ;
                            System.out.println("系统将在" + h + "时给" + user.getName() + "发送推送");
                        }
                    }
                }
            }
        }
    }

    @Override
    public Push packDailyData(LocalDate date, List<Tag> tags) {
        if (tags.size() == 0)
            return null;
        else {
            LocalDateTime now = LocalDateTime.now();
            long time = localDateTimeToMilli(now);
            System.out.println("Push CreateTime ->" + time);
            Push push = new Push(time);
            List<BaseChart> content = new ArrayList<>();
            for (Tag tag : tags) {
                if (tag.getName().equals(DailyProduceCountList))
                    content.add(packDailyProduceCountList(date));
                if (tag.getName().equals(DailyCountOfModel))
                    content.add(packDailyCountOfModel(date));
                if (tag.getName().equals(DailyProduceQualityList))
                    content.add(packDailyProduceQualityList(date));
                if (tag.getName().equals(DailyConsume))
                    content.add(packDailyConsume(date));
            }
            push.setContent(JSON.toJSONString(content));
            push.setDefaultSubMenu(content.get(0).getSubmenu());
            push.setTitle(dateToStr(date, DATE_FORMAT_CN) + "推送数据");
            return push;
        }
    }

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
