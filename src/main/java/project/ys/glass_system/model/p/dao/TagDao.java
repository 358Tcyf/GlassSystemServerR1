package project.ys.glass_system.model.p.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import project.ys.glass_system.model.p.entity.Tag;

public interface TagDao extends JpaRepository<Tag, Integer> {

    Tag findByName(String name);
}
