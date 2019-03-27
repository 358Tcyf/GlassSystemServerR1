package project.ys.glass_system.model.p.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import project.ys.glass_system.model.p.entity.Alarm;

public interface AlarmDao extends JpaRepository<Alarm, Integer> {


}
