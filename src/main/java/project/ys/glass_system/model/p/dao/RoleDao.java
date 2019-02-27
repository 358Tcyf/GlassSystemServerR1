package project.ys.glass_system.model.p.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import project.ys.glass_system.model.p.entity.Role;

public interface RoleDao extends JpaRepository<Role, Integer> {

    Role findById(int id);

}
