package project.ys.glass_system.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import project.ys.glass_system.model.entity.Role;

public interface RoleDao extends JpaRepository<Role, Integer> {

    Role findById(int id);

}
