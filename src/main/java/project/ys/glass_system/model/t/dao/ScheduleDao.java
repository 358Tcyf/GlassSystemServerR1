package project.ys.glass_system.model.t.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.ys.glass_system.model.t.entity.GlassModel;
import project.ys.glass_system.model.t.entity.ProduceNote;
import project.ys.glass_system.model.t.entity.Schedule;

import java.util.List;

public interface ScheduleDao extends JpaRepository<Schedule, Integer> {

    @Query("SELECT COALESCE(SUM(schedule.num),0) FROM gls_produce_schedule schedule WHERE schedule.belong = ?1 AND schedule.model =?2")
    int sumNumByBelongAndModel(ProduceNote belong, GlassModel model);

    List<Schedule> findByBelong(ProduceNote belong);
}
