package project.ys.glass_system.service.t;

import project.ys.glass_system.model.p.bean.BaseChart;
import project.ys.glass_system.model.p.entity.*;
import project.ys.glass_system.model.t.entity.TestRank;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface AutoPushService {
    void pushEveryOne(LocalDate date, boolean ignore);

    void pushUser(LocalDate date, User user, boolean ignore);

    Push dailyPush(LocalDate date, boolean ignore, PushSet set, String alias);

    Push weeklyPush(LocalDate date, boolean ignore, PushSet set, String alias);

    Alarm hourlyAlarm(LocalDateTime time, boolean ignore, PushSet set, String alias);

    BaseChart dailyProduceChart(LocalDate date);

    BaseChart dailyTestChart(LocalDate date);

    BaseChart dailyConsumeChart(LocalDate date);

    BaseChart weeklyProduceChart(LocalDate date);

    BaseChart weeklyTestChart(LocalDate date);

    BaseChart weeklyConsumeChart(LocalDate date);

    String observeRankResult(LocalDateTime time, AlarmTag tag, TestRank rank);

    String observeConsu(LocalDateTime time, AlarmTag tag);
}
