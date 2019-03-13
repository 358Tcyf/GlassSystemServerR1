package project.ys.glass_system.model.p.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import project.ys.glass_system.model.p.entity.Permission;

public interface PermissionDao extends JpaRepository<Permission, Integer> {


}
