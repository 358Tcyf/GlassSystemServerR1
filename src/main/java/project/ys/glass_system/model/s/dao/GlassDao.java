package project.ys.glass_system.model.s.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import project.ys.glass_system.model.s.entity.Glass;

public interface GlassDao extends JpaRepository<Glass, Integer> {

    Glass findGlassByModel(String model);
}
