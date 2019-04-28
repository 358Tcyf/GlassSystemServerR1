package project.ys.glass_system.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

import static org.hibernate.internal.util.StringHelper.isEmpty;
import static project.ys.glass_system.constant.HttpConstant.FILE;
import static project.ys.glass_system.constant.UserConstant.*;
import static project.ys.glass_system.util.EncodeUtils.encode;

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
            User user = userDao.findDistinctByNoOrPhoneOrEmail(no, no, no);
            if (encode(DEFAULT_PASSWORD).equals(user.getPassword()))
                return 0;
            else {
                user.setPassword(encode(DEFAULT_PASSWORD));
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
        return userDao.findDistinctByNoOrPhoneOrEmail(account, account, account) != null;
    }

    @Override
    public boolean checkPassword(String account, String password) {
        return userDao.findDistinctByNoOrPhoneOrEmailAndPassword(account, account, account, encode(password)) != null;
    }

    @Override
    public User login(String account, String password) {
        return userDao.findDistinctByNoOrPhoneOrEmailAndPassword(account, account, account, encode(password));
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
        if (find.getPic() != null)
            result.put("picPath", FILE + "/" + find.getNo());
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

    @Override
    public boolean updateUser(String no, String email, String phone) {
        if (isExisted(no)) {
            User user = userDao.findByNo(no);
            user.setEmail(email);
            user.setPhone(phone);
            return true;
        }
        return false;
    }

    @Override
    public void updatePassword(String no, String newPassword) {
        User user = userDao.findByNo(no);
        user.setPassword(encode(newPassword));
    }

    @Override
    public Map<String, Object> searchUserList(String searchText) {
        List<User> allUsers = userDao.searchUsers(searchText);
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

    @Override
    public Map<String, Object> userQuery(String name, String account, int role, String phone, String email, int page, int limit) {
        Pageable pageable = new PageRequest(page - 1, limit);
        Page<User> allList = null;
        if (isEmpty(name) && isEmpty(account) && isEmpty(phone) && isEmpty(email) && role == 0)
            allList = userDao.findAll(pageable);
        else if (role == 0)
            allList = userDao.queryUsersByNameLikeAndNoLikeAndPhoneLikeAndEmailLike("%"+name+"%", "%"+account+"%", "%"+phone+"%", "%"+email+"%", pageable);
        else {
            Role role1 = roleDao.findById(role+1);
            allList = userDao.queryUsersByNameLikeAndNoLikeAndRoleAndPhoneLikeAndEmailLike("%"+name+"%", "%"+account+"%",role1, "%"+phone+"%", "%"+email+"%", pageable);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("count", allList.getTotalElements());
        List<Map<String, Object>> listMap = new ArrayList<>();
        for (User user : allList) {
            if (!user.getNo().startsWith("A")) {
                Map<String, Object> u = new HashMap<>();
                u.put("id", user.getId());
                u.put("account", user.getNo());
                u.put("role", user.getRole().getName());
                u.put("name", user.getName());
                u.put("phone", user.getPhone());
                u.put("email", user.getEmail());
                listMap.add(u);
            }
        }
        map.put("object", listMap);
        return map;
    }

    @Override
    public void logoffUserList(String[] accounts) {
        for(String account:accounts){
            logoffUser(account);
        }
    }
}
