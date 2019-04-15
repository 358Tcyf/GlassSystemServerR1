package project.ys.glass_system.service.impl;

import org.springframework.stereotype.Service;
import project.ys.glass_system.model.p.dao.AlarmTagDao;
import project.ys.glass_system.model.p.dao.PushSetDao;
import project.ys.glass_system.model.p.dao.TagDao;
import project.ys.glass_system.model.p.dao.UserDao;
import project.ys.glass_system.model.p.entity.AlarmTag;
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
    AlarmTagDao alarmTagDao;

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
    public List<AlarmTag> getAlarmTags(String no) {
        return userDao.findByNo(no).getPushSet().getAlarmTags();
    }

    @Override
    public boolean updateSet(String no, PushSet pushSet) {
        User user = userDao.findByNo(no);
        if (user == null)
            return false;
        PushSet set = user.getPushSet();
        set.setCommonSwitch(pushSet.isCommonSwitch());
        set.setSound(pushSet.isSound());
        set.setVibrate(pushSet.isVibrate());
        set.setFlags(pushSet.isFlags());
        set.setStart(pushSet.getStart());
        set.setEnd(pushSet.getEnd());
        set.setTime(pushSet.getTime());
        set.setPushSwitch(pushSet.isCommonSwitch() && pushSet.isPushSwitch());
        set.setSmartSub(pushSet.isCommonSwitch() && pushSet.isPushSwitch());
        set.setAlarmSwitch(pushSet.isCommonSwitch() && pushSet.isAlarmSwitch());
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

    @Override
    public boolean cancelTags(String no, List<String> tags) {
        User user = userDao.findByNo(no);
        if (user == null)
            return false;
        PushSet set = user.getPushSet();
        List<Tag> setTags = set.getTags();
        for (String name : tags) {
            for(int i = 0;i < setTags.size();i++){
                if(name.equals(setTags.get(i).getName())){
                    setTags.remove(i);
                }
            }
        }
        set.setTags(setTags);
        pushSetDao.saveAndFlush(set);
        return true;
    }

    @Override
    public boolean updateAlarmTags(String no, List<AlarmTag> tags) {
        User user = userDao.findByNo(no);
        if (user == null)
            return false;
        PushSet set = user.getPushSet();
        List<AlarmTag> setTags = set.getAlarmTags();
        setTags.clear();
        AlarmTag alarmTag;
        for (AlarmTag tag : tags) {
            if (alarmTagDao.findByContentAndMinAndMax(tag.getContent(), tag.getMin(), tag.getMax()) == null) {
                alarmTag = alarmTagDao.save(tag);
                setTags.add(alarmTag);
            } else {
                alarmTag = alarmTagDao.findByContent(tag.getContent());
                setTags.add(alarmTag);
            }
        }
        set.setAlarmTags(setTags);
        pushSetDao.saveAndFlush(set);
        return true;
    }

    @Override
    public void cancelSmartSub(String no) {
        User user = userDao.findByNo(no);
        PushSet set = user.getPushSet();
        set.setSmartSub(false);
        pushSetDao.save(set);

    }

}
