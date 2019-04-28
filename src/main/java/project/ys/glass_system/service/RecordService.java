package project.ys.glass_system.service;

import project.ys.glass_system.model.p.entity.User;

import java.util.Map;

public interface RecordService {
    void addRecord(User account, int action);

    Map<String, Object> topRecord(boolean login);
}
