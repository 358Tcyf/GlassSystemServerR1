package project.ys.glass_system.service;

import project.ys.glass_system.model.p.bean.BaseChart;
import project.ys.glass_system.model.p.entity.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface PushService {

    void pushEveryUser(LocalDate date, boolean ignoreTime);

    void pushWithAlias(LocalDate date, User user, boolean ignoreTime);

    Push packDailyData(LocalDate date, List<Tag> tags);

    Push packDailyData(LocalDate date);

    BaseChart packDailyProduceCount(LocalDate date);

    BaseChart packDailyProduceCountList(LocalDate date);

    BaseChart packDailyCountOfModel(LocalDate date);

    BaseChart packDailyProduceQualityList(LocalDate date);

    BaseChart packDailyConsume(LocalDate date);

    Alarm packDailyAlarm(LocalDateTime date, List<AlarmTag> tags);

    String observeFailRate(LocalDateTime date, AlarmTag tag);

    String observeElecConsu(LocalDateTime date, AlarmTag tag);

    String observeWtrConsu(LocalDateTime date, AlarmTag tag);

    String observeCoalConsu(LocalDateTime date, AlarmTag tag);


}
