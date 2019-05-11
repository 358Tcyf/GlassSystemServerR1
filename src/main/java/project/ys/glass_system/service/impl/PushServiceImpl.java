package project.ys.glass_system.service.impl;

import com.alibaba.fastjson.JSON;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.ys.glass_system.model.p.bean.AlarmLog;
import project.ys.glass_system.model.p.bean.BaseChart;
import project.ys.glass_system.model.p.bean.BaseEntry;
import project.ys.glass_system.model.p.bean.EChartPieEntry;
import project.ys.glass_system.model.p.dao.AlarmDao;
import project.ys.glass_system.model.p.dao.PushDao;
import project.ys.glass_system.model.p.dao.UserDao;
import project.ys.glass_system.model.p.entity.Alarm;
import project.ys.glass_system.model.p.entity.Push;
import project.ys.glass_system.model.p.entity.User;
import project.ys.glass_system.service.PushService;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.logging.log4j.util.Strings.isEmpty;
import static project.ys.glass_system.constant.AutoPushConstant.PUSH_TABS;
import static project.ys.glass_system.util.LocalDateUtils.DATE_TIME_FORMAT;
import static project.ys.glass_system.util.LocalDateUtils.dateToStr;

@Service
@Transactional
public class PushServiceImpl implements PushService {

    @Resource
    UserDao userDao;
    @Resource
    PushDao pushDao;
    @Resource
    AlarmDao alarmDao;

    @Override
    public void updatePush(String account, List<Push> pushes) {
        List<Push> oldPushes = pushDao.findPushesByReceiver(account);
        for (Push oldPush : oldPushes) {
            pushDao.delete(oldPush);
        }
        for (Push push : pushes) {
            pushDao.save(push);
        }
    }

    @Override
    public List<Push> downloadPush(String account) {
        List<Push> oldPushes = pushDao.findPushesByReceiver(account);
        return oldPushes;
    }

    @Override
    public void updateAlarm(String account, List<Alarm> alarms) {
        List<Alarm> oldAlarms = alarmDao.findAlarmsByReceiver(account);
        for (Alarm oldAlarm : oldAlarms) {
            alarmDao.delete(oldAlarm);
        }
        for (Alarm alarm : alarms) {
            alarmDao.save(alarm);
        }
    }

    @Override
    public List<Alarm> downloadAlarm(String account) {
        List<Alarm> oldAlarms = alarmDao.findAlarmsByReceiver(account);
        return oldAlarms;
    }

    @Override
    public Map<String, Object> pushQuery(String title, long startTime, long endTime, String receiverID, String receiver, int type, int read, int page, int limit) {
        Pageable pageable = new PageRequest(page - 1, limit);
        Page<Push> pushList;
        Page<Alarm> alarmList;
        List<User> users;
        List<String> receivers = new ArrayList<>();
        if (isEmpty(receiver)) {
            users = userDao.findAll();
        } else {
            users = userDao.findUsersByNameLike("%" + receiver + "%");
        }
        if (users.size() > 0) {
            for (User user : users) {
                receivers.add(user.getNo());
            }
        }
        if (read != 0) {
            pushList = pushDao.queryPushesByTitleLikeAndCreateTimeBetweenAndReceiverLikeAndReceiverInAndHaveRead("%" + title + "%", startTime, endTime, "%" + receiverID + "%", receivers, read == 1, pageable);
            alarmList = alarmDao.queryAlarmsByTitleLikeAndCreateTimeBetweenAndReceiverLikeAndReceiverInAndHaveRead("%" + title + "%", startTime, endTime, "%" + receiverID + "%", receivers, read == 1, pageable);
        } else {
            pushList = pushDao.queryPushesByTitleLikeAndCreateTimeBetweenAndReceiverLikeAndReceiverIn("%" + title + "%", startTime, endTime, "%" + receiverID + "%", receivers, pageable);
            alarmList = alarmDao.queryAlarmsByTitleLikeAndCreateTimeBetweenAndReceiverLikeAndReceiverIn("%" + title + "%", startTime, endTime, "%" + receiverID + "%", receivers, pageable);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("count", pushList.getTotalElements() + alarmList.getTotalElements());
        List<Map<String, Object>> listMap = new ArrayList<>();
        for (Push push : pushList) {
            Map<String, Object> p = new HashMap<>();
            User user = userDao.findByNo(push.getReceiver());
            p.put("id", push.getPushUuid());
            p.put("title", push.getTitle());
            p.put("createTime", dateToStr(push.getCreateTime(), DATE_TIME_FORMAT));
            p.put("receiver_id", user.getNo());
            p.put("receiver", user.getName());
            p.put("content", push.getContent());
            p.put("type", 1);
            p.put("read", push.isHaveRead());
            if (type != 2)
                listMap.add(p);
        }
        for (Alarm alarm : alarmList) {
            Map<String, Object> a = new HashMap<>();
            User user = userDao.findByNo(alarm.getReceiver());
            a.put("id", alarm.getAlarmUuid());
            a.put("title", alarm.getTitle());
            a.put("createTime", dateToStr(alarm.getCreateTime(), DATE_TIME_FORMAT));
            a.put("receiver_id", user.getNo());
            a.put("receiver", user.getName());
            a.put("content", alarm.getContent());
            a.put("type", 2);
            a.put("read", alarm.isHaveRead());

            if (type != 1)
                listMap.add(a);
        }
        map.put("object", listMap);
        return map;
    }


    @Override
    public Map<String, Object> getTabs(String uuid) {
        Push push = pushDao.findByPushUuid(uuid);
        Map<String, Object> result = new HashMap<>();
        List<String> subMenus = new ArrayList<>();
        String content = push.getContent();
        String defaultSubMenu = push.getDefaultSubMenu();
        List<BaseChart> baseCharts = JSON.parseArray(content, BaseChart.class);
        sortCharts(baseCharts, defaultSubMenu, subMenus);
        result.put("tabs", subMenus);
        return result;
    }

    @Override
    public Object getChart(String uuid, String subMenu) {
        Push push = pushDao.findByPushUuid(uuid);
        String content = push.getContent();
        List<BaseChart> baseCharts = JSON.parseArray(content, BaseChart.class);
        for (BaseChart baseChart : baseCharts) {
            if (baseChart.getSub().equals(subMenu)) {
                Map<String, Object> chart = new HashMap<>();
                chart.put("type", baseChart.getType());
                switch (baseChart.getType()) {
                    case 0:
                        chart.put("chart", baseChart);
                        chart.put("title", baseChart.getTitle());
                        if (baseChart.isOnly()) {
                            String[] labels = new String[]{baseChart.getLabel()};
                            chart.put("legend", labels);
                            chart.put("xAxis",baseChart.getxValues());
                            List<Object> data = new ArrayList<>();
                            List<List<Object>> series = new ArrayList<>();
                            for (BaseEntry entry : baseChart.getyValues()) {
                                data.add(entry.getY());
                            }
                            series.add(data);
                            chart.put("series", series);
                        } else {
                            chart.put("legend", baseChart.getLabels());
                            chart.put("xAxis", baseChart.getxValues());
                            List<List<Object>> series = new ArrayList<>();
                            for (List<BaseEntry> entryList : baseChart.getyListValues()) {
                                List<Object> data = new ArrayList<>();
                                for (BaseEntry entry : entryList) {
                                    data.add(entry.getY());
                                }
                                series.add(data);
                            }
                            chart.put("series", series);
                        }
                        return chart;
                    case 1:
                        chart.put("chart", baseChart);
                        chart.put("title", baseChart.getTitle());
                        chart.put("legend", baseChart.getLabels());
                        if (baseChart.isOnly()) {
                            chart.put("xAxis", baseChart.getxValues());
                        } else chart.put("xAxis", baseChart.getxValues());
                        List<List<Object>> series = new ArrayList<>();
                        for (List<BaseEntry> entryList : baseChart.getyListValues()) {
                            List<Object> data = new ArrayList<>();
                            for (BaseEntry entry : entryList) {
                                data.add(entry.getY());
                            }
                            series.add(data);
                        }
                        chart.put("series", series);
                        return chart;
                    case 2:
                    case 3:
                        chart.put("title", baseChart.getTitle());
                        chart.put("subtitle", baseChart.getDesc());
                        chart.put("series", "水单位:吨、电量单位:K·KW·h、原料单位：吨、煤单位:吨n");
                        chart.put("legend", baseChart.getxValues());
                        List<EChartPieEntry> data = new ArrayList<>();
                        for (BaseEntry entry : baseChart.getyValues()) {
                            data.add(new EChartPieEntry((String) entry.getX(), entry.getY()));
                        }
                        chart.put("data", data);
                        return chart;
                    default:
                        return baseChart;
                }
            }
        }
        return null;
    }

    @Override
    public Object getAlarm(String uuid) {
        Alarm alarm = alarmDao.findByAlarmUuid(uuid);
        StringBuilder content = new StringBuilder();
        List<AlarmLog> alarmLogs = JSON.parseArray(alarm.getContent(), AlarmLog.class);
        for(AlarmLog log: alarmLogs){
            content.append(log.getLog()).append("<br/>");
        }
        return content;
    }

    void sortCharts(List<BaseChart> charts, String defaultSubMenu, List<String> subMenus) {
        String[] push_charts = PUSH_TABS;
        for (int i = 0; i < PUSH_TABS.length; i++) {
            if (PUSH_TABS[i].equals(defaultSubMenu)) {
                push_charts[i] = push_charts[0];
                push_charts[0] = defaultSubMenu;
            }
        }
        for (int i = 0; i < PUSH_TABS.length; i++)
            for (BaseChart chart : charts) {
                if (chart.getSub().equals(PUSH_TABS[i])) {
                    subMenus.add(chart.getSub());
                }
            }
    }

    @Override
    public boolean deletePush(String uuid) {
        if (pushDao.findByPushUuid(uuid) != null) {
            pushDao.delete(pushDao.findByPushUuid(uuid));
            return true;
        }
        if (alarmDao.findByAlarmUuid(uuid) != null) {
            alarmDao.delete(alarmDao.findByAlarmUuid(uuid));
            return true;
        }
        return false;
    }

    @Override
    public void deletePushList(String[] uuids) {
        for (String uuid : uuids) {
            deletePush(uuid);
        }
    }
}
