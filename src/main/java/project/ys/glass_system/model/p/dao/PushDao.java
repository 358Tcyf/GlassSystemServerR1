package project.ys.glass_system.model.p.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import project.ys.glass_system.model.p.entity.Push;

import java.util.List;

public interface PushDao extends JpaRepository<Push, Integer> {

    List<Push> findPushesByReceiver(String receiver);
}
