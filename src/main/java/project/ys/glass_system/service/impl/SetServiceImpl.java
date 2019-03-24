package project.ys.glass_system.service.impl;

import org.springframework.stereotype.Service;
import project.ys.glass_system.model.p.dao.PushSetDao;
import project.ys.glass_system.model.p.dao.TagDao;
import project.ys.glass_system.model.p.dao.UserDao;
import project.ys.glass_system.model.p.entity.PushSet;
import project.ys.glass_system.model.p.entity.Tag;
import project.ys.glass_system.model.p.entity.User;
import project.ys.glass_system.service.SetService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class SetServiceImpl implements SetService {

    @Resource
    UserDao userDao;


    @Resource
    TagDao tagDao;

    @Resource
    PushSetDao pushSetDao;

    @Override
    public PushSet getPushSet(String no) {
        User user = userDao.findByNo(no);
        PushSet set = user.getPushSet();
        if (set != null)
            return set;
        else {
            set = new PushSet(false, 1, false);
            user.setPushSet(set);
            return pushSetDao.save(set);
        }
    }

    @Override
    public List<String> getTags(String no) {
        List<String> strings = new ArrayList<>();
        List<Tag> tags = userDao.findByNo(no).getPushSet().getTags();
        if (tags == null)
            return null;
        for (Tag tag : tags)
            strings.add(tag.getName());
        return strings;
    }

    @Override
    public boolean updateSet(String no, PushSet pushSet) {
        User user = userDao.findByNo(no);
        if (user == null)
            return false;
        PushSet set = user.getPushSet();
        set.setPushSwitch(pushSet.isPushSwitch());
        set.setTime(pushSet.getTime());
        set.setAlarmSwitch(pushSet.isAlarmSwitch());
        pushSetDao.saveAndFlush(set);
        user.setPushSet(pushSet);
        return true;
    }

    @Override
    public boolean updateTags(String no, List<String> tags) {
        User user = userDao.findByNo(no);
        if (user == null)
            return false;
        PushSet set = user.getPushSet();
        List<Tag> setTags = set.getTags();
        setTags.clear();
        for (String name : tags) {
            if (tagDao.findByName(name) == null) {
                tagDao.save(new Tag(name));
            } else {
                Tag tag = tagDao.findByName(name);
                setTags.add(tag);
            }
        }
        set.setTags(setTags);
        pushSetDao.saveAndFlush(set);
        return true;
    }

}
