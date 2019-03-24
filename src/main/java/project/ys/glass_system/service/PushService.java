package project.ys.glass_system.service;

import project.ys.glass_system.model.p.bean.BaseChart;
import project.ys.glass_system.model.p.entity.Push;
import project.ys.glass_system.model.p.entity.Tag;

import java.time.LocalDate;
import java.util.List;

public interface PushService {
    Push packDailyData(LocalDate date, List<Tag> tags);

    Push packDailyData(LocalDate date);

    BaseChart packDailyProduceCount(LocalDate date);

    BaseChart packDailyProduceCountList(LocalDate date);

    BaseChart packDailyCountOfModel(LocalDate date);

    BaseChart packDailyProduceQualityList(LocalDate date);

    BaseChart packDailyConsume(LocalDate date);

}
