package project.ys.glass_system.service.t.impl;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Service;
import project.ys.glass_system.model.p.bean.BaseChart;
import project.ys.glass_system.model.p.bean.BaseEntry;
import project.ys.glass_system.model.p.dao.UserDao;
import project.ys.glass_system.model.p.entity.Push;
import project.ys.glass_system.model.p.entity.PushSet;
import project.ys.glass_system.model.p.entity.Tag;
import project.ys.glass_system.model.p.entity.User;
import project.ys.glass_system.model.t.dao.*;
import project.ys.glass_system.model.t.entity.GlassModel;
import project.ys.glass_system.model.t.entity.ProduceNote;
import project.ys.glass_system.model.t.entity.TestNote;
import project.ys.glass_system.model.t.entity.TestRank;
import project.ys.glass_system.service.t.AutoPushService;

import javax.annotation.Resource;
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
            if (set.isCommonSwitch()) {
                if (set.isPushSwitch()) {
                    if (!isEmpty(set.getTags())) {
                        Push daily = dailyPush(date, ignore, set.getTags(), user.getNo());
                        Push weekly = weeklyPush(date, ignore, set.getTags(), user.getNo());
                    }
                }
            }
        }
    }

    @Override
    public Push dailyPush(LocalDate date, boolean ignore, List<Tag> tags, String alias) {
        LocalDateTime time = LocalDateTime.now();
        if (time.getHour() == 22 || ignore) {
            long milliTime = localDateTimeToMilli(time);
            Push push = new Push(milliTime);
            List<BaseChart> content = new ArrayList<>();
            for (Tag tag : tags) {
                if (tag.getName().equals(DailyProduce))
                    content.add(dailyProduceChart(date));
                if (tag.getName().equals(DailyProduce))
                    content.add(dailyProduceChart(date));
                if (tag.getName().equals(DailyProduce))
                    content.add(dailyProduceChart(date));
            }
            if (content.size() == 0) return null;
            push.setContent(JSON.toJSONString(content));
            push.setDefaultSubMenu(content.get(0).getSub());
            push.setTitle(date.getMonth()+"月 第"+getWeekIndexOfMonth(date)+ "周数据");
            push.setPushUuid(getNum19());
            return push;
        }
        return null;
    }

    @Override
    public Push weeklyPush(LocalDate date, boolean ignore, List<Tag> tags, String alias) {
        if (date.equals(date.with(DayOfWeek.SUNDAY)) || ignore) {
            LocalDateTime time = LocalDateTime.now();
            if (time.getHour() == 22 || ignore) {
                long milliTime = localDateTimeToMilli(time);
                Push push = new Push(milliTime);
                List<BaseChart> content = new ArrayList<>();
                for (Tag tag : tags) {
                    if (tag.getName().equals(WeeklyProduce))
                        content.add(weeklyProduceChart(date));
                    if (tag.getName().equals(WeeklyTestRank))
                        content.add(weeklyProduceChart(date));
                    if (tag.getName().equals(WeeklyConsume))
                        content.add(weeklyProduceChart(date));
                }
                if (content.size() == 0) return null;
                push.setContent(JSON.toJSONString(content));
                push.setDefaultSubMenu(content.get(0).getSub());
                push.setTitle(dateToStr(date, DATE_FORMAT_CN) + "数据");
                push.setPushUuid(getNum19());
                return push;
            }
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
        BaseChart consumeDate = new BaseChart(DailyConsume, DailyConsume + "统计", "生产能耗");
        consumeDate.setType(ring_chart, true);
        consumeDate.setLabels(Arrays.asList(ConsumeLabel));
        consumeDate.setxValues(ConsumeLabel);
        consumeDate.setyValues(new ArrayList<>());
        ProduceNote note = produceNoteDao.findByDate(date);
        consumeDate.getyValues().add(new BaseEntry(ConsumeLabel[0], produceDao.sumWaterByBelong(note)));
        consumeDate.getyValues().add(new BaseEntry(ConsumeLabel[1], produceDao.sumElectricityByBelong(note)));
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
        BaseChart consumeDate = new BaseChart(WeeklyConsume, WeeklyConsume + "统计", "生产能耗");
        consumeDate.setType(ring_chart, true);
        consumeDate.setLabels(Arrays.asList(ConsumeLabel));
        consumeDate.setxValues(ConsumeLabel);
        consumeDate.setyValues(new ArrayList<>());
        ProduceNote note = produceNoteDao.findByDate(date);
        consumeDate.getyValues().add(new BaseEntry(ConsumeLabel[0], produceDao.sumWaterByBelong(note)));
        consumeDate.getyValues().add(new BaseEntry(ConsumeLabel[1], produceDao.sumElectricityByBelong(note)));
        consumeDate.getyValues().add(new BaseEntry(ConsumeLabel[2], produceDao.sumMaterialByBelong(note)));
        consumeDate.getyValues().add(new BaseEntry(ConsumeLabel[3], produceDao.sumCoalByBelong(note)));
        return consumeDate;
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
        return date.get(ChronoField.ALIGNED_WEEK_OF_MONTH);
    }

    public int getWeekIndexOfYear(LocalDate date) {
        return date.get(ChronoField.ALIGNED_WEEK_OF_YEAR);
    }
}
