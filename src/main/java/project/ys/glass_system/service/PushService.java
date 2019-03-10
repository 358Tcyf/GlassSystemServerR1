package project.ys.glass_system.service;

import project.ys.glass_system.model.p.bean.BaseChart;
import project.ys.glass_system.model.p.bean.Push;

import java.time.LocalDate;

public interface PushService {
    Push packDailyData(LocalDate date);

    BaseChart packDailyProduceCount(LocalDate date);

    BaseChart packDailyProduceCountList(LocalDate date);

    BaseChart packDailyCountOfModel(LocalDate date);

    BaseChart packDailyProduceQualityList(LocalDate date);

    BaseChart packDailyConsume(LocalDate date);

}
