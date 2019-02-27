package project.ys.glass_system.model.p.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import project.ys.glass_system.model.p.entity.Role;
import project.ys.glass_system.model.p.entity.User;

public interface UserDao extends JpaRepository<User, Integer> {

    User findByNo(String no);

    void deleteByNo(String no);

    User findByNoAndPassword(String no, String password);

    User findDistinctFirstByRoleOrderByNoDesc(Role role);
}
