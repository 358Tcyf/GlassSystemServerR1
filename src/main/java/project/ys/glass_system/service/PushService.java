package project.ys.glass_system.service;

import project.ys.glass_system.model.p.bean.BaseChart;

import java.time.LocalDate;
import java.util.Map;

public interface PushService {
    Map<String, Object> packDailyData(LocalDate date);

    BaseChart packDailyProduceCount(LocalDate date);

    BaseChart packDailyProduceCountList(LocalDate date);

}
