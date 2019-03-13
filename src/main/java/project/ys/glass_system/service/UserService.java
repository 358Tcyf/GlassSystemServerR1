package project.ys.glass_system.service;

import project.ys.glass_system.model.p.entity.Role;
import project.ys.glass_system.model.p.entity.User;

import java.util.Map;

public interface UserService {

    void create(Role role);

    void addUser(User user, int roleId);

    String getLatestNo(int roleId);

    int resetPassword(String no);

    void logoffUser(String no);

    boolean isExisted(String account);

    boolean checkPassword(String account, String password);

    User login(String account, String password);

    Map<String, Object> userInfo(String no);

    Map<String, Object> userList();

    boolean updateTags(String no,String tags);

    String getTags(String no);
}
