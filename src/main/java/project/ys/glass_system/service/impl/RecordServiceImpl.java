package project.ys.glass_system.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.ys.glass_system.model.p.dao.RecordDao;
import project.ys.glass_system.model.p.dao.UserDao;
import project.ys.glass_system.model.p.entity.Record;
import project.ys.glass_system.model.p.entity.User;
import project.ys.glass_system.service.RecordService;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static project.ys.glass_system.util.LocalDateUtils.*;

@Service
@Transactional
public class RecordServiceImpl implements RecordService {

    @Resource
    RecordDao recordDao;

    @Resource
    UserDao userDao;

    @Override
    public void addRecord(User user, int action) {
        Record record = new Record();
        record.setUser(user);
        record.setAction(action);
        record.setTime(localDateTimeToMilli(LocalDateTime.now()));
        recordDao.save(record);
    }

    @Override
    public Map<String, Object> topRecord(boolean login) {
        Pageable pageable = new PageRequest(0, 10);
        Page<Record> records = null;
        if (login)
            records = recordDao.queryRecordsByActionBetween(1, 2, pageable);
        else
            records = recordDao.queryRecordsByActionBetween(11, 12, pageable);
        Map<String, Object> map = new HashMap<>();
        map.put("count", records.getTotalElements());
        List<Map<String, Object>> listMap = new ArrayList<>();
        for (Record record : records) {
            Map<String, Object> r = new HashMap<>();
            r.put("account", record.getUser().getNo());
            if (login)
                r.put("name", record.getUser().getName());
            else
                r.put("action", record.getAction() == 11 ? "上传" : "下载");
            r.put("time", dateToStr(record.getTime(), DATE_TIME_FORMAT));
            listMap.add(r);
        }
        map.put("object", listMap);
        return map;
    }
}
