package project.ys.glass_system.model.t.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import project.ys.glass_system.model.t.entity.TestItem;

public interface TestDao extends JpaRepository<TestItem, Integer> {

}
