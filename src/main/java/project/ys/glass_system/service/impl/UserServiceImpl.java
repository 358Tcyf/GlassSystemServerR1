package project.ys.glass_system.service.impl;

import org.springframework.stereotype.Service;
import project.ys.glass_system.model.p.dao.RoleDao;
import project.ys.glass_system.model.p.dao.UserDao;
import project.ys.glass_system.model.p.entity.Role;
import project.ys.glass_system.model.p.entity.User;
import project.ys.glass_system.service.UserService;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    public static final int SUPER_MANAGER = 1;
    public static final int MANAGEMENT_SECTION = 2;
    public static final int PRODUCT_SECTION = 3;
    public static final int SALE_SECTION = 4;
    public static final String DEFAULT_PASSWORD = "123456";

    @Resource
    private UserDao userDao;

    @Resource
    private RoleDao roleDao;

    @Override
    public void create(Role role) {
        roleDao.save(role);
    }

    @Override
    public void addUser(User user, int roleId) {
        Role role = roleDao.findById(roleId);
        user.setRole(role);
        userDao.save(user);
    }

    @Override
    public String getLatestNo(int roleId) {
        Role role = roleDao.findById(roleId);
        User user = userDao.findDistinctFirstByRoleOrderByNoDesc(role);
        String latestNo = "";
        int no;
        if (user != null) {
            latestNo = user.getNo();
            latestNo = latestNo.substring(1);
            no = Integer.parseInt(latestNo) + 1;
        } else {
            no = 1;
        }
        switch (roleId) {
            case SUPER_MANAGER:
                latestNo = "A" + String.format("%02d", no);
                break;
            case MANAGEMENT_SECTION:
                latestNo = "M" + String.format("%03d", no);
                break;
            case PRODUCT_SECTION:
                latestNo = "P" + String.format("%04d", no);
                break;
            case SALE_SECTION:
                latestNo = "S" + String.format("%04d", no);
                break;
        }
        return latestNo;
    }

    @Override
    public int resetPassword(String no) {
        if (isExisted(no)) {
            User user = userDao.findByNo(no);
            if (DEFAULT_PASSWORD.equals(user.getPassword()))
                return 0;
            else {
                user.setPassword(DEFAULT_PASSWORD);
                userDao.saveAndFlush(user);
                return 1;
            }
        } else return -1;
    }

    @Override
    public void logoffUser(String no) {
        if (isExisted(no))
            userDao.deleteByNo(no);
    }

    @Override
    public boolean isExisted(String account) {
        return userDao.findByNo(account) != null;
    }

    @Override
    public boolean checkPassword(String account, String password) {
        return userDao.findByNoAndPassword(account, password) != null;
    }

    @Override
    public User login(String account, String password) {
        return userDao.findByNoAndPassword(account, password);
    }

    @Override
    public Map<String, Object> userInfo(String no) {
        User find = userDao.findByNo(no);
        Map<String, Object> result = new HashMap<>();
        result.put("no", find.getNo());
        result.put("phone", find.getPhone());
        result.put("name", find.getName());
        result.put("email", find.getEmail());
        result.put("roleName", find.getRole().getName());
        return result;
    }

    @Override
    public Map<String, Object> userList() {
        List<User> allUsers = userDao.findAll();
        if (allUsers == null)
            return null;
        Map<String, Object> resultData = new HashMap<>();
        List<Map<String, Object>> users = new ArrayList<>();
        for (User user : allUsers) {
            if (!user.getNo().startsWith("A"))
                users.add(userInfo(user.getNo()));
        }
        resultData.putIfAbsent("staffs", users);
        return resultData;
    }

    public boolean updateTags(String no, String tags) {
        if (isExisted(no)) {
            User user = userDao.findByNo(no);
            user.setTags(tags);
            return true;
        }
        return false;
    }

    public String getTags(String no) {
        if (isExisted(no)) {
            User user = userDao.findByNo(no);
            return user.getTags();
        }
        return null;
    }
}
