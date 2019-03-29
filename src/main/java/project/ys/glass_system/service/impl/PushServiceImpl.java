package project.ys.glass_system.service.impl;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Service;
import project.ys.glass_system.model.p.bean.AlarmLog;
import project.ys.glass_system.model.p.bean.BaseChart;
import project.ys.glass_system.model.p.bean.BaseEntry;
import project.ys.glass_system.model.p.dao.UserDao;
import project.ys.glass_system.model.p.entity.*;
import project.ys.glass_system.model.s.dao.ProductDao;
import project.ys.glass_system.model.s.dao.ProductNoteDao;
import project.ys.glass_system.model.s.entity.Glass;
import project.ys.glass_system.model.s.entity.ProductNotes;
import project.ys.glass_system.model.s.entity.Products;
import project.ys.glass_system.service.PushService;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.apache.logging.log4j.util.Strings.isEmpty;
import static project.ys.glass_system.getui.GetuiUtil.sendSingleMessage;
import static project.ys.glass_system.getui.GetuiUtil.transmissionTemplate;
import static project.ys.glass_system.model.p.bean.AlarmLog.ALARM_TAGS;
import static project.ys.glass_system.model.p.bean.BaseChart.*;
import static project.ys.glass_system.model.p.entity.PushSet.getTime;
import static project.ys.glass_system.model.p.entity.Tag.*;
import static project.ys.glass_system.util.DateUtils.format1;
import static project.ys.glass_system.util.LocalDateUtils.*;
import static project.ys.glass_system.util.UuidUtil.getNum19;

@Service
@Transactional
public class PushServiceImpl implements PushService {

    DecimalFormat df2 = new DecimalFormat("###.00");

    @Resource
    UserDao userDao;

    @Resource
    ProductDao productDao;

    @Resource
    ProductNoteDao productNoteDao;

    @Resource
    GlassServiceImpl glassService;

    @Override
    public void pushEveryUser(LocalDate date, boolean ignoreTime) {
        List<User> userList = userDao.findAll();
        if (ignoreTime)
            System.out.println(dateToStr(LocalDateTime.now(), format1) + "请求忽略日期和时间限制");
        for (User user : userList) {
            pushWithAlias(date, user, ignoreTime);
        }
    }

    @Override
    public void pushWithAlias(LocalDate date, User user, boolean ignoreTime) {
        PushSet set = user.getPushSet();
        LocalDateTime dateTime = LocalDateTime.now();
        if (set != null) {
            if (set.isCommonSwitch()) {
                if (set.isPushSwitch()) {
                    long localDateTimeToMilli = localDateTimeToMilli(dateTime);
                    int pushTime = getTime(set.getTime());
                    int nowTime = LocalTime.now().getHour();
                    if (ignoreTime) {
                        push(date, set.getTags(), user.getNo());
                    } else {
                        if (set.getStart() == 0 || set.getEnd() == 0) {
                            System.out.println(dateToStr(LocalDateTime.now(), format1) + "系统判断" + user.getName() + "的推送起止时间设置不合法，不予推送");
                        } else if (!(localDateTimeToMilli > set.getStart() && localDateTimeToMilli < set.getEnd())) {
                            System.out.println(dateToStr(LocalDateTime.now(), format1) + "系统判断" + user.getName() + "的推送不在推送时间段内，不予推送");
                        } else {
                            if ((nowTime + 1) % pushTime == 0) {
                                System.out.println(dateToStr(LocalDateTime.now(), format1) + "系统判断" + user.getName() + "的推送在推送时间段内，请求推送");
                                push(date, set.getTags(), user.getNo());
                            } else {
                                int h = 0;
                                for (h = 0; h < nowTime + 1; h += pushTime) ;
                                System.out.println(dateToStr(LocalDateTime.now(), format1) + "现在是" + nowTime + "时," + user.getName() + "的推送不在推送时间段内将在" + h + "时给" + user.getName() + "发送推送");
                            }
                        }
                    }
                }
                if (set.isAlarmSwitch()) {
                    long localDateTimeToMilli = localDateTimeToMilli(dateTime);
                    if (ignoreTime) {
                        alarm(dateTime, set.getAlarmTags(), user.getNo());
                    } else if (set.getStart() == 0 || set.getEnd() == 0) {
                        System.out.println(dateToStr(LocalDateTime.now(), format1) + "系统判断" + user.getName() + "的推送起止时间设置不合法，不予推送");
                    } else if (!(localDateTimeToMilli > set.getStart() && localDateTimeToMilli < set.getEnd())) {
                        System.out.println(dateToStr(LocalDateTime.now(), format1) + "系统判断" + user.getName() + "的推送不在推送时间段内，不予推送");
                    } else {
                        alarm(dateTime, set.getAlarmTags(), user.getNo());
                    }
                }
            }
        }
    }


    private void push(LocalDate date, List<Tag> tags, String alias) {
        Push push = packDailyData(date, tags);
        push.setPushUuid(getNum19());
        push.setReceiver(alias);
        if (push != null) {
            String message = JSON.toJSONString(push);
            System.out.println(dateToStr(LocalDateTime.now(), format1) + "系统尝试发送数据推送，目标别名：" + alias);
            sendSingleMessage(1, alias, transmissionTemplate(message));
        }
    }

    private void alarm(LocalDateTime date, List<AlarmTag> tags, String alias) {
        Alarm alarm = packDailyAlarm(date, tags);
        alarm.setAlarmUuid(getNum19());
        alarm.setReceiver(alias);
        if (alarm != null) {
            String message = JSON.toJSONString(alarm);
            System.out.println(dateToStr(LocalDateTime.now(), format1) + "系统尝试发送预警推送，目标别名：" + alias);
            sendSingleMessage(1, alias, transmissionTemplate(message));
        }
    }

    @Override
    public Push packDailyData(LocalDate date, List<Tag> tags) {
        if (tags.size() == 0)
            return null;
        else {
            LocalDateTime now = LocalDateTime.now();
            long time = localDateTimeToMilli(now);
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
            push.setTitle(dateToStr(date, DATE_FORMAT_CN) + "数据");
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

    @Override
    public Alarm packDailyAlarm(LocalDateTime date, List<AlarmTag> tags) {
        if (tags.size() == 0)
            return null;
        else {
            LocalDateTime now = LocalDateTime.now();
            long time = localDateTimeToMilli(now);
            Alarm alarm = new Alarm(time);
            List<AlarmLog> alarmContent = new ArrayList<>();
            for (AlarmTag tag : tags) {
                if (tag.getContent().equals(ALARM_TAGS[0])) {
                    String observe = observeFailRate(date, tag);
                    if (!isEmpty(observe))
                        alarmContent.add(new AlarmLog(tag.getContent(), observe));
                }
                if (tag.getContent().equals(ALARM_TAGS[1])) {
                    String observe = observeElecConsu(date, tag);
                    if (!isEmpty(observe))
                        alarmContent.add(new AlarmLog(tag.getContent(), observe));
                }
                if (tag.getContent().equals(ALARM_TAGS[2])) {
                    String observe = observeWtrConsu(date, tag);
                    if (!isEmpty(observe))
                        alarmContent.add(new AlarmLog(tag.getContent(), observe));
                }
                if (tag.getContent().equals(ALARM_TAGS[3])) {
                    String observe = observeCoalConsu(date, tag);
                    if (!isEmpty(observe))
                        alarmContent.add(new AlarmLog(tag.getContent(), observe));
                }
            }
            alarm.setContent(JSON.toJSONString(alarmContent));
            alarm.setTitle(dateToStr(date, DATE_FORMAT_CN) + "预警");
            return alarm;
        }
    }


    @Override
    public String observeFailRate(LocalDateTime date, AlarmTag tag) {
        Date start = localDateTimeToDate(date);
        start.setMinutes(0);
        start.setSeconds(0);
        Date end = localDateTimeToDate(date);
        end.setMinutes(59);
        end.setSeconds(59);
        List<Products> products = productDao.findProductsByDateBetween(dateToLocalDateTime(start), dateToLocalDateTime(end));

        boolean error = false;
        String result = "观测" + ALARM_TAGS[0] + ": \n";
        for (Products product : products) {
            float failRate = (float) product.getFail() / (float) product.getPlan() * 100;
            String fail = product.getModel().getModel() + ":" + df2.format(failRate) + "% \n";
            if (failRate < tag.getMin() || failRate > tag.getMax()) {
                result += fail;
                error = true;
            }
        }
        if (error)
            return result;
        else
            return "";
    }

    @Override
    public String observeElecConsu(LocalDateTime date, AlarmTag tag) {
        ProductNotes productNote = productNoteDao.findByDate(date.toLocalDate());
        boolean error = false;
        String result = "观测" + ALARM_TAGS[1] + ": ";
        String elec = df2.format(productNote.getElectricity()) + "\n";
        if (productNote.getElectricity() < tag.getMin() || productNote.getElectricity() > tag.getMax()) {
            result += elec;
            error = true;
        }
        if (error)
            return result;
        else
            return "";
    }

    @Override
    public String observeWtrConsu(LocalDateTime date, AlarmTag tag) {
        ProductNotes productNote = productNoteDao.findByDate(date.toLocalDate());
        boolean error = false;
        String result = "观测" + ALARM_TAGS[2] + ": ";
        String water = df2.format(productNote.getWater()) + "\n";
        if (productNote.getWater() < tag.getMin() || productNote.getWater() > tag.getMax()) {
            result += water;
            error = true;
        }
        if (error)
            return result;
        else
            return "";
    }

    @Override
    public String observeCoalConsu(LocalDateTime date, AlarmTag tag) {
        ProductNotes productNote = productNoteDao.findByDate(date.toLocalDate());
        boolean error = false;
        String result = "观测" + ALARM_TAGS[3] + ": ";
        String coal = df2.format(productNote.getCoal()) + "\n";
        if (productNote.getCoal() < tag.getMin() || productNote.getCoal() > tag.getMax()) {
            result += coal;
            error = true;
        }
        if (error)
            return result;
        else
            return "";
    }


}
