package project.ys.glass_system.service.t.impl;

import com.alibaba.fastjson.JSON;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import project.ys.glass_system.model.p.bean.AlarmLog;
import project.ys.glass_system.model.p.bean.BaseChart;
import project.ys.glass_system.model.p.bean.BaseEntry;
import project.ys.glass_system.model.p.dao.AlarmTagDao;
import project.ys.glass_system.model.p.dao.UserDao;
import project.ys.glass_system.model.p.entity.*;
import project.ys.glass_system.model.t.dao.*;
import project.ys.glass_system.model.t.entity.*;
import project.ys.glass_system.service.t.AutoPushService;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjuster;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;
import static project.ys.glass_system.constant.AutoPushConstant.*;
import static project.ys.glass_system.getui.GetuiUtil.sendSingleMessage;
import static project.ys.glass_system.getui.GetuiUtil.transmissionTemplate;
import static project.ys.glass_system.model.p.bean.AlarmLog.ALARM_TAGS;
import static project.ys.glass_system.model.p.bean.BaseChart.*;
import static project.ys.glass_system.util.LocalDateUtils.*;
import static project.ys.glass_system.util.UuidUtil.getNum19;

@Service
public class AutoPushServiceImpl implements AutoPushService {

    @Resource
    UserDao userDao;

    @Resource
    GlassModelDao modelDao;

    @Resource
    TestRankDao rankDao;

    @Resource
    ProduceNoteDao produceNoteDao;

    @Resource
    ProduceDao produceDao;


    @Resource
    TestNoteDao testNoteDao;

    @Resource
    TestDao testDao;

    @Resource
    TestResultDao testResultDao;

    @Resource
    AlarmTagDao alarmTagDao;

    @Override
    public void pushEveryOne(LocalDate date, boolean ignore) {
        List<User> userList = userDao.findAll();
        for (User user : userList) {
            pushUser(date, user, ignore);
        }
    }

    @Override
    public void pushUser(LocalDate date, User user, boolean ignore) {
        PushSet set = user.getPushSet();
        if (!isEmpty(set)) {
            if (set.isCommonSwitch() || ignore) {
                long localDateTimeToMilli = localDateTimeToMilli(LocalDateTime.now());
                if ((localDateTimeToMilli > set.getStart() && localDateTimeToMilli < set.getEnd()) || ignore) {
                    if (set.isPushSwitch() || ignore) {
                        if (!isEmpty(set.getTags())) {
                            Push daily = dailyPush(date, ignore, set, user.getNo());
                            Push weekly = weeklyPush(date, ignore, set, user.getNo());
                            if (!isEmpty(daily)) {
                                daily.setReceiver(user.getNo());
                                sendSingleMessage(1, daily.getReceiver(), transmissionTemplate(JSON.toJSONString(daily)));
                            }
                            if (!isEmpty(weekly)) {
                                weekly.setReceiver(user.getNo());
                                sendSingleMessage(1, weekly.getReceiver(), transmissionTemplate(JSON.toJSONString(weekly)));
                            }
                        }
                    }
                    if (set.isAlarmSwitch() || ignore) {
                        if (!isEmpty(alarmTagDao.findBySet(set))) {
                            Alarm alarm = hourlyAlarm(LocalDateTime.now(), ignore, set, user.getNo());
                            if (!isEmpty(alarm)) {
                                alarm.setReceiver(user.getNo());
                                sendSingleMessage(1, alarm.getReceiver(), transmissionTemplate(JSON.toJSONString(alarm)));
                            }
                        }
                    }
                }

            }
        }
    }

    @Override
    public Push dailyPush(LocalDate date, boolean ignore, PushSet set, String alias) {
        LocalDateTime time = LocalDateTime.now();
        int pushTime = (set.getTime() == 0 || ignore) ? 22 : 8;
        LocalDate pushDate = (set.getTime() == 0 || ignore) ? date : date.minusDays(1);
        if (time.getHour() == pushTime || ignore) {
            long milliTime = localDateTimeToMilli(time);
            Push push = new Push(milliTime);
            List<BaseChart> content = new ArrayList<>();
            for (Tag tag : set.getTags()) {
                if (tag.getName().equals(DailyProduce))
                    content.add(dailyProduceChart(pushDate));
                if (tag.getName().equals(DailyTestRank))
                    content.add(dailyTestChart(pushDate));
                if (tag.getName().equals(DailyConsume))
                    content.add(dailyConsumeChart(pushDate));
            }
            if (content.size() == 0) return null;
            push.setContent(JSON.toJSONString(content));
            push.setDefaultSubMenu(content.get(0).getSub());
            push.setTitle(dateToStr(pushDate, DATE_FORMAT_CN) + "数据");
            push.setPushUuid(getNum19());
            return push;
        }
        return null;
    }

    @Override
    public Push weeklyPush(LocalDate date, boolean ignore, PushSet set, String alias) {
        if (date.equals(date.with(DayOfWeek.SUNDAY)) || ignore) {
            LocalDateTime time = LocalDateTime.now();
            int pushTime = (set.getTime() == 0 || ignore) ? 22 : 8;
            LocalDate pushDate = (set.getTime() == 0 || ignore) ? date : date.minusDays(1);
            if (time.getHour() == pushTime || ignore) {
                long milliTime = localDateTimeToMilli(time);
                Push push = new Push(milliTime);
                List<BaseChart> content = new ArrayList<>();
                for (Tag tag : set.getTags()) {
                    if (tag.getName().equals(WeeklyProduce))
                        content.add(weeklyProduceChart(pushDate));
                    if (tag.getName().equals(WeeklyTestRank))
                        content.add(weeklyTestChart(pushDate));
                    if (tag.getName().equals(WeeklyConsume))
                        content.add(weeklyConsumeChart(pushDate));
                }
                if (content.size() == 0) return null;
                push.setContent(JSON.toJSONString(content));
                push.setDefaultSubMenu(content.get(0).getSub());
                push.setTitle(dateToStr(pushDate, DATE_FORMAT_MONTH) + "第" + getWeekIndexOfMonth(pushDate) + "周数据");
                push.setPushUuid(getNum19());
                return push;
            }
        }
        return null;
    }

    @Override
    public Alarm hourlyAlarm(LocalDateTime time, boolean ignore, PushSet set, String alias) {
        if (time.getMinute() < 5 || ignore) {
            Alarm alarm = new Alarm(localDateTimeToMilli(time));
            List<AlarmLog> alarmContent = new ArrayList<>();
            boolean error = false;
            for (AlarmTag tag : alarmTagDao.findBySet(set)) {
                if (tag.getContent().equals(ALARM_TAGS[0]) || tag.getContent().equals(ALARM_TAGS[1])) {
                    TestRank rank = rankDao.findByName(tag.getContent().substring(0, 3));
                    String observe = observeRankResult(time, tag, rank);
                    if (!Strings.isEmpty(observe)) {
                        error = true;
                        alarmContent.add(new AlarmLog(tag.getContent(), observe));
                    }
                }
                if (tag.getContent().equals(ALARM_TAGS[2])
                        || tag.getContent().equals(ALARM_TAGS[3])
                        || tag.getContent().equals(ALARM_TAGS[4])
                        || tag.getContent().equals(ALARM_TAGS[5])) {
                    String observe = observeConsu(time, tag);
                    if (!Strings.isEmpty(observe)) {
                        error = true;
                        alarmContent.add(new AlarmLog(tag.getContent(), observe));
                    }
                }
            }
            alarm.setContent(JSON.toJSONString(alarmContent));
            alarm.setTitle(dateToStr(time, DATE_FORMAT_HOUR) + "预警");
            alarm.setAlarmUuid(getNum19());
            if (error)
                return alarm;
            else
                return null;
        }
        return null;
    }

    @Override
    public BaseChart dailyProduceChart(LocalDate date) {
        BaseChart produceData = new BaseChart(DailyProduce, DailyProduce + "统计", "各型号生产数");
        produceData.setLabel("生产数");
        produceData.setType(pie_chart, true);
        List<GlassModel> models = modelDao.findAll();
        produceData.setLabels(modelLabelList());
        produceData.setxValues(modelLabelArray());
        produceData.setyValues(new ArrayList<>());
        ProduceNote note = produceNoteDao.findByDate(date);
        for (GlassModel model : models) {
            int count = produceDao.sumNumByBelongAndModel(note, model);
            produceData.getyValues().add(new BaseEntry(model.getName(), count));
        }
        return produceData;
    }

    @Override
    public BaseChart dailyTestChart(LocalDate date) {
        BaseChart testData = new BaseChart(DailyTestRank, DailyTestRank + "统计", "质检品级结果");
        testData.setLabels(rankLabelList());
        testData.setType(bar_chart, false);
        TestNote note = testNoteDao.findByDate(date);
        List<GlassModel> models = modelDao.findAll();
        List<TestRank> ranks = rankDao.findAllOrderByName();
        testData.setLabels(rankLabelList());
        testData.setxValues(modelLabelArray());
        testData.setyListValues(new ArrayList<>());
        for (TestRank rank : ranks) {
            List<BaseEntry> yValue = new ArrayList<>();
            for (int i = 0; i < models.size(); i++) {
                int sum = testResultDao.sumNumByBelongInAndModelAndRank(note.getTestItems(), models.get(i), rank);
                yValue.add(new BaseEntry(i + 1, sum));
            }
            testData.getyListValues().add(yValue);
        }

        return testData;
    }

    @Override
    public BaseChart dailyConsumeChart(LocalDate date) {
        BaseChart consumeDate = new BaseChart(DailyConsume, DailyConsume + "统计", "各类型能耗");
        consumeDate.setType(ring_chart, true);
        consumeDate.setLabel("水单位:吨、电量单位:K·KW·h、原料单位：吨、煤单位:吨n");
        consumeDate.setLabels(Arrays.asList(ConsumeLabel));
        consumeDate.setxValues(ConsumeLabel);
        consumeDate.setyValues(new ArrayList<>());
        ProduceNote note = produceNoteDao.findByDate(date);
        consumeDate.getyValues().add(new BaseEntry(ConsumeLabel[0], produceDao.sumWaterByBelong(note)));
        consumeDate.getyValues().add(new BaseEntry(ConsumeLabel[1], produceDao.sumElectricityByBelong(note) / 1000));
        consumeDate.getyValues().add(new BaseEntry(ConsumeLabel[2], produceDao.sumMaterialByBelong(note)));
        consumeDate.getyValues().add(new BaseEntry(ConsumeLabel[3], produceDao.sumCoalByBelong(note)));
        return consumeDate;
    }

    @Override
    public BaseChart weeklyProduceChart(LocalDate date) {
        BaseChart produceData = new BaseChart(WeeklyProduce, WeeklyProduce + "统计", "每日生产数");
        produceData.setLabels(modelLabelList());
        produceData.setType(line_chart, false);
        List<GlassModel> models = modelDao.findAll();
        List<LocalDate> dates = getWeekDays(date);
        produceData.setLabels(modelLabelList());
        produceData.setxValues(Weeks);
        produceData.setyListValues(new ArrayList<>());
        for (GlassModel model : models) {
            List<BaseEntry> yValue = new ArrayList<>();
            for (int i = 0; i < dates.size(); i++) {
                ProduceNote note = produceNoteDao.findByDate(dates.get(i));
                int sum = produceDao.sumNumByBelongAndModel(note, model);
                yValue.add(new BaseEntry(i + 1, sum));
            }
            produceData.getyListValues().add(yValue);
        }
        return produceData;
    }

    @Override
    public BaseChart weeklyTestChart(LocalDate date) {
        BaseChart testData = new BaseChart(WeeklyTestRank, WeeklyTestRank + "统计", "每日质检数");
        testData.setLabels(modelLabelList());
        testData.setType(line_chart, false);
        List<TestRank> ranks = rankDao.findAll();
        List<LocalDate> dates = getWeekDays(date);
        testData.setLabels(rankLabelList());
        testData.setxValues(Weeks);
        testData.setyListValues(new ArrayList<>());
        for (TestRank rank : ranks) {
            List<BaseEntry> yValue = new ArrayList<>();
            for (int i = 0; i < dates.size(); i++) {
                TestNote note = testNoteDao.findByDate(dates.get(i));
                int sum = (isEmpty(note) || isEmpty(note.getTestItems())) ? 0 : testResultDao.sumNumByBelongInAndRank(note.getTestItems(), rank);
                yValue.add(new BaseEntry(i + 1, sum));
            }
            testData.getyListValues().add(yValue);
        }
        return testData;
    }

    @Override
    public BaseChart weeklyConsumeChart(LocalDate date) {
        BaseChart consumeDate = new BaseChart(WeeklyConsume, WeeklyConsume + "统计", "各类型能耗");
        consumeDate.setType(ring_chart, true);
        consumeDate.setLabel("水单位:吨、电量单位:K·KW·h、原料单位：吨、煤单位:吨n");
        consumeDate.setLabels(Arrays.asList(ConsumeLabel));
        consumeDate.setxValues(ConsumeLabel);
        consumeDate.setyValues(new ArrayList<>());
        ProduceNote note = produceNoteDao.findByDate(date);
        consumeDate.getyValues().add(new BaseEntry(ConsumeLabel[0], produceDao.sumWaterByBelong(note)));
        consumeDate.getyValues().add(new BaseEntry(ConsumeLabel[1], produceDao.sumElectricityByBelong(note) / 1000));
        consumeDate.getyValues().add(new BaseEntry(ConsumeLabel[2], produceDao.sumMaterialByBelong(note)));
        consumeDate.getyValues().add(new BaseEntry(ConsumeLabel[3], produceDao.sumCoalByBelong(note)));
        return consumeDate;
    }


    @Override
    public String observeRankResult(LocalDateTime time, AlarmTag tag, TestRank rank) {
        List<TestItem> tests = testDao.findByTimeBetween(time.minusHours(1).minusMinutes(5), time.minusMinutes(5));
        int count = testResultDao.sumNumByBelongInAndRank(tests, rank);
        boolean error = false;
        String result = "观测" + tag.getContent() + ": ";
        String fail = rank.getName() + " " + count + "件 \n";
        if (count < tag.getMin() || count > tag.getMax()) {
            result += fail;
            error = true;
        }
        if (error)
            return result;
        else
            return null;
    }

    DecimalFormat df2 = new DecimalFormat("###.00");

    @Override
    public String observeConsu(LocalDateTime time, AlarmTag tag) {
        float consu = 0;
        if (tag.getContent().equals(ALARM_TAGS[2]))
            consu = produceDao.sumWaterByTimeBetween(time.minusHours(1).minusMinutes(5), time.minusMinutes(5));
        if (tag.getContent().equals(ALARM_TAGS[3]))
            consu = produceDao.sumElectricityByTimeBetween(time.minusHours(1).minusMinutes(5), time.minusMinutes(5));
        if (tag.getContent().equals(ALARM_TAGS[4]))
            consu = produceDao.sumMaterialByTimeBetween(time.minusHours(1).minusMinutes(5), time.minusMinutes(5));
        if (tag.getContent().equals(ALARM_TAGS[5]))
            consu = produceDao.sumCoalByTimeBetween(time.minusHours(1).minusMinutes(5), time.minusMinutes(5));
        boolean error = false;
        String result = "观测" + tag.getContent() + ": ";
        String fail = df2.format(consu) + " \n";
        if (consu < tag.getMin() || consu > tag.getMax()) {
            result += fail;
            error = true;
        }
        if (error)
            return result;
        else
            return null;
    }


    public String[] modelLabelArray() {
        return modelDao.findNameArray();
    }

    public List<String> modelLabelList() {
        return modelDao.findNameList();
    }

    public List<String> rankLabelList() {
        return rankDao.findNameListOrderByName();
    }


    public List<LocalDate> getWeekDays(LocalDate date) {
        List<LocalDate> thisWeek = new ArrayList<>();
        for (TemporalAdjuster adjuster : Adjuster) {
            thisWeek.add(date.with(adjuster));
        }
        return thisWeek;
    }

    public List<LocalDate> getWeekDays() {
        return getWeekDays(LocalDate.now());
    }

    public int getWeekIndexOfMonth(LocalDate date) {
        return date.with(DayOfWeek.SUNDAY).get(ChronoField.ALIGNED_WEEK_OF_MONTH);
    }

    public int getWeekIndexOfYear(LocalDate date) {
        return date.with(DayOfWeek.SUNDAY).get(ChronoField.ALIGNED_WEEK_OF_YEAR);
    }
}
