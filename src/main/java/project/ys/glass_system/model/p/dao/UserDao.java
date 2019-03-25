package project.ys.glass_system.model.p.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.ys.glass_system.model.p.entity.Role;
import project.ys.glass_system.model.p.entity.User;

import java.util.List;

public interface UserDao extends JpaRepository<User, Integer> {


    @Query(value = "select distinct u from User u where  u.name LIKE %?1% OR u.phone LIKE %?1% OR u.email LIKE %?1%")
    List<User> searchUsers(String searchText);

    User findByNo(String no);

    void deleteByNo(String no);

    User findByNoAndPassword(String no, String password);

    User findDistinctFirstByRoleOrderByNoDesc(Role role);

}
