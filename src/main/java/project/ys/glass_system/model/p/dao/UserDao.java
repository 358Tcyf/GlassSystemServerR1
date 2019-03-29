package project.ys.glass_system.model.p.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.ys.glass_system.model.p.entity.Role;
import project.ys.glass_system.model.p.entity.User;

import java.util.List;

public interface UserDao extends JpaRepository<User, Integer> {


    @Query(value = "select distinct u from User u where  u.name LIKE %?1% OR u.phone LIKE %?1% OR u.email LIKE %?1%")
    List<User> searchUsers(String searchText);

    User findByNo(String no);

    User findDistinctByNoOrPhoneOrEmail(String no, String phone, String email);

    void deleteByNo(String no);

    User findByNoAndPassword(String no, String password);

    User findDistinctByNoOrPhoneOrEmailAndPassword(String no, String phone, String email, String password);

    User findDistinctFirstByRoleOrderByNoDesc(Role role);

    Page<User> queryUsersByNameLikeAndNoLikeAndPhoneLikeAndEmailLike(String name, String no, String phone, String email, Pageable pageable);

    Page<User> queryUsersByNameLikeAndNoLikeAndRoleAndPhoneLikeAndEmailLike(String name, String no, Role role, String phone, String email, Pageable pageable);

//
//    @Query(value = "select u from User u where u.name LIKE %?1% AND u.account LIKE %?2% AND u.phone LIKE %?5% AND u.email LIKE %?4%")
//    Page<User> queryUsers(String name, String account, String phone, String email, Pageable pageable);


}
