package project.ys.glass_system.model.t.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import project.ys.glass_system.model.t.entity.TestItem;

import java.time.LocalDateTime;
import java.util.List;

public interface TestDao extends JpaRepository<TestItem, Integer> {

    List<TestItem> findByTimeBetween(LocalDateTime time, LocalDateTime time2);

}
