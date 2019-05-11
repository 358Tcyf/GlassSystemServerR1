package project.ys.glass_system.service;

import project.ys.glass_system.model.p.bean.BaseChart;
import project.ys.glass_system.model.p.entity.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface PushService {

    void updatePush(String account, List<Push> pushes);

    List<Push> downloadPush(String account);

    void updateAlarm(String account, List<Alarm> alarms);

    List<Alarm> downloadAlarm(String account);

    Map<String, Object> pushQuery(String title, long startTime, long endTime, String receiverID, String receiver, int type, int read, int page, int limit);

    Map<String, Object> getTabs(String uuid);

    boolean deletePush(String uuid);

    void deletePushList(String[] uuids);

    Object getChart(String uuid, String subMenu);

    Object getAlarm(String uuid);
}
