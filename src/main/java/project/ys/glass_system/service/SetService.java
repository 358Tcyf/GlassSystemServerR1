package project.ys.glass_system.service;

import project.ys.glass_system.model.p.entity.PushSet;

import java.util.List;

public interface SetService {

    PushSet getPushSet(String no);

    List<String> getTags(String no);

    boolean updateSet(String no, PushSet pushSet);

    boolean updateTags(String no, List<String> tags);
}
