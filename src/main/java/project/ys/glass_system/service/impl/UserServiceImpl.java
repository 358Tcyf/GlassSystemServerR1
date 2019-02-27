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
            case 0:
                latestNo = "A" + String.format("%02d", no);
                break;
            case 1:
                latestNo = "M" + String.format("%03d", no);
                break;
            case 2:
                latestNo = "P" + String.format("%04d", no);
                break;
            case 3:
                latestNo = "S" + String.format("%04d", no);
                break;
        }
        System.out.println(latestNo);
        return latestNo;
    }

    @Override
    public void logoffUser(String no) {
        userDao.deleteByNo(no);
    }

    @Override
    public boolean isExisted(String account) {
        if (userDao.findByNo(account) == null)
            return false;
        return true;
    }

    @Override
    public boolean checkPassword(String account, String password) {
        if (userDao.findByNoAndPassword(account, password) == null)
            return false;
        return true;
    }

    @Override
    public User login(String account, String password) {
        return userDao.findByNoAndPassword(account, password);
    }

    @Override
    public Map<String, Object> userInfo(String no) {
        User find = userDao.findByNo(no);
        Map<String, Object> user = new HashMap<>();
        Map<String, Object> role = new HashMap<>();
        Map<String, Object> result = new HashMap<>();
        user.put("no", find.getNo());
        user.put("phone", find.getPhone());
        user.put("name", find.getName());
        user.put("email", find.getEmail());
        role.put("roleName", find.getRole().getName());
        result.put("user", user);
        result.put("role", role);
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
        System.out.println(resultData);
        return resultData;
    }
}
