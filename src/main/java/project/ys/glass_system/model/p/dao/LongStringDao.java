package project.ys.glass_system.model.p.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import project.ys.glass_system.model.p.entity.LongString;

public interface LongStringDao extends JpaRepository<LongString, Integer> {

}
