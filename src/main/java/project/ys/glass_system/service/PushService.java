package project.ys.glass_system.service;

import project.ys.glass_system.model.p.bean.BaseChart;
import project.ys.glass_system.model.p.entity.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface PushService {

    void pushEveryUser(LocalDate date, boolean ignoreTime);

    void pushWithAlias(LocalDate date, User user, boolean ignoreTime);

    Push packDailyProduceData(LocalDate date, List<Tag> tags);

    Push packDailySaleData(LocalDate date, List<Tag> tags);

    BaseChart packDailyProduceCount(LocalDate date);

    BaseChart packDailyProduceCountList(LocalDate date);

    BaseChart packDailyCountOfModel(LocalDate date);

    BaseChart packDailyProduceQualityList(LocalDate date);

    BaseChart packDailyConsume(LocalDate date);

    BaseChart packDailySaleCount(LocalDate date);

    BaseChart packDailyDeliveryCount(LocalDate date);

    BaseChart packDailySale(LocalDate date);

    BaseChart packDailyCustomRate(LocalDate date);


    Alarm packDailyAlarm(LocalDateTime date, List<AlarmTag> tags);

    String observeFailRate(LocalDateTime date, AlarmTag tag);

    String observeElecConsu(LocalDateTime date, AlarmTag tag);

    String observeWtrConsu(LocalDateTime date, AlarmTag tag);

    String observeCoalConsu(LocalDateTime date, AlarmTag tag);

    void updatePush(String account, List<Push> pushes);

    List<Push> downloadPush(String account);

    void updateAlarm(String account, List<Alarm> alarms);

    List<Alarm> downloadAlarm(String account);

    Map<String, Object> pushQuery(String title, long startTime, long endTime, String receiverID, String receiver,int type, int read, int page, int limit);

    boolean deletePush(String uuid);

    void deletePushList(String[]  uuids);
}
