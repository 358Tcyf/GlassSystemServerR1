package project.ys.glass_system.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import project.ys.glass_system.model.entity.Role;
import project.ys.glass_system.model.entity.User;

public interface UserDao extends JpaRepository<User, Integer> {

    User findByNo(String no);

    void deleteByNo(String no);

    User findByNoAndPassword(String no, String password);

    User findDistinctFirstByRoleOrderByNoDesc(Role role);
}
