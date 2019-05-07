package project.ys.glass_system.service.t;

import project.ys.glass_system.model.p.bean.BaseChart;
import project.ys.glass_system.model.p.entity.Push;
import project.ys.glass_system.model.p.entity.Tag;
import project.ys.glass_system.model.p.entity.User;

import java.time.LocalDate;
import java.util.List;

public interface AutoPushService {
    void pushEveryOne(LocalDate date, boolean ignore);

    void pushUser(LocalDate date, User user, boolean ignore);

    Push dailyPush(LocalDate date, boolean ignore, List<Tag> tags, String alias);

    Push weeklyPush(LocalDate date, boolean ignore, List<Tag> tags, String alias);

    BaseChart dailyProduceChart(LocalDate date);

    BaseChart dailyTestChart(LocalDate date);

    BaseChart dailyConsumeChart(LocalDate date);

    BaseChart weeklyProduceChart(LocalDate date);

    BaseChart weeklyTestChart(LocalDate date);

    BaseChart weeklyConsumeChart(LocalDate date);
}
