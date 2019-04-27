package project.ys.glass_system.model.p.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import project.ys.glass_system.model.p.entity.Alarm;

import java.util.List;

public interface AlarmDao extends JpaRepository<Alarm, Integer> {

    List<Alarm> findAlarmsByReceiver(String receiver);

    Alarm findByAlarmUuid(String alarmUuid);
}
