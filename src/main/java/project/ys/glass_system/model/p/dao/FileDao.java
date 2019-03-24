package project.ys.glass_system.model.p.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import project.ys.glass_system.model.p.entity.File;

public interface FileDao extends JpaRepository<File, Integer> {

    File findFileByUuid(String uuid);
}
