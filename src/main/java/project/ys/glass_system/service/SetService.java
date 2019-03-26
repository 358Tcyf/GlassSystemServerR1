package project.ys.glass_system.service;

import project.ys.glass_system.model.p.entity.AlarmTag;
import project.ys.glass_system.model.p.entity.PushSet;

import java.util.List;

public interface SetService {

    PushSet getPushSet(String no);

    List<String> getTags(String no);

    List<AlarmTag> getAlarmTags(String no);

    boolean updateSet(String no, PushSet pushSet);

    boolean updateTags(String no, List<String> tags);

    boolean updateAlarmTags(String no, List<AlarmTag> tags);
}
