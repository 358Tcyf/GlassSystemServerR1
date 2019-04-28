package project.ys.glass_system.service.impl;

import com.alibaba.fastjson.JSON;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.ys.glass_system.model.p.bean.AlarmLog;
import project.ys.glass_system.model.p.bean.BaseChart;
import project.ys.glass_system.model.p.bean.BaseEntry;
import project.ys.glass_system.model.p.dao.AlarmDao;
import project.ys.glass_system.model.p.dao.PushDao;
import project.ys.glass_system.model.p.dao.TagDao;
import project.ys.glass_system.model.p.dao.UserDao;
import project.ys.glass_system.model.p.entity.*;
import project.ys.glass_system.model.s.dao.GlassDao;
import project.ys.glass_system.model.s.dao.OrderDao;
import project.ys.glass_system.model.s.dao.ProductDao;
import project.ys.glass_system.model.s.dao.ProductNoteDao;
import project.ys.glass_system.model.s.entity.*;
import project.ys.glass_system.service.PushService;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static org.apache.logging.log4j.util.Strings.isEmpty;
import static project.ys.glass_system.getui.GetuiUtil.sendSingleMessage;
import static project.ys.glass_system.getui.GetuiUtil.transmissionTemplate;
import static project.ys.glass_system.model.p.bean.AlarmLog.ALARM_TAGS;
import static project.ys.glass_system.model.p.bean.BaseChart.*;
import static project.ys.glass_system.model.p.entity.PushSet.getTime;
import static project.ys.glass_system.model.p.entity.Tag.*;
import static project.ys.glass_system.util.DateUtils.format1;
import static project.ys.glass_system.util.LocalDateUtils.*;
import static project.ys.glass_system.util.UuidUtil.getNum19;

@Service
@Transactional
public class PushServiceImpl implements PushService {

    DecimalFormat df2 = new DecimalFormat("###.00");

    @Resource
    UserDao userDao;

    @Resource
    TagDao tagDao;

    @Resource
    ProductDao productDao;

    @Resource
    OrderDao orderDao;
    @Resource
    ProductNoteDao productNoteDao;
    @Resource
    GlassDao glassDao;
    @Resource
    GlassServiceImpl glassService;
    @Resource
    PushDao pushDao;
    @Resource
    AlarmDao alarmDao;

    @Override
    public void pushEveryUser(LocalDate date, boolean ignoreTime) {
        List<User> userList = userDao.findAll();
        if (ignoreTime)
            System.out.println(dateToStr(LocalDateTime.now(), format1) + "请求忽略日期和时间限制");
        for (User user : userList) {
            pushWithAlias(date, user, ignoreTime);
        }
    }

    @Override
    public void pushWithAlias(LocalDate date, User user, boolean ignoreTime) {
        PushSet set = user.getPushSet();
        LocalDateTime dateTime = LocalDateTime.now();
        if (set != null) {
            if (set.isCommonSwitch()) {
                if (set.isPushSwitch()) {
                    long localDateTimeToMilli = localDateTimeToMilli(dateTime);
                    int pushTime = getTime(set.getTime());
                    int nowTime = LocalTime.now().getHour();
                    if (ignoreTime) {
                        push(date, set.getTags(), user.getNo());
                    } else {
                        if (set.getStart() == 0 || set.getEnd() == 0) {
                            System.out.println(dateToStr(LocalDateTime.now(), format1) + "系统判断" + user.getName() + "的推送起止时间设置不合法，不予推送");
                        } else if (!(localDateTimeToMilli > set.getStart() && localDateTimeToMilli < set.getEnd())) {
                            System.out.println(dateToStr(LocalDateTime.now(), format1) + "系统判断" + user.getName() + "的推送不在推送时间段内，不予推送");
                        } else {
                            if ((nowTime + 1) % pushTime == 0) {
                                System.out.println(dateToStr(LocalDateTime.now(), format1) + "系统判断" + user.getName() + "的推送在推送时间段内，请求推送");
                                push(date, set.getTags(), user.getNo());
                            } else {
                                int h = 0;
                                for (h = 0; h < nowTime + 1; h += pushTime) ;
                                System.out.println(dateToStr(LocalDateTime.now(), format1) + "现在是" + nowTime + "时," + user.getName() + "的推送不在推送时间段内将在" + h + "时给" + user.getName() + "发送推送");
                            }
                        }
                    }
                }
                if (set.isAlarmSwitch()) {
                    long localDateTimeToMilli = localDateTimeToMilli(dateTime);
                    if (ignoreTime) {
                        alarm(dateTime, set.getAlarmTags(), user.getNo());
                    } else if (set.getStart() == 0 || set.getEnd() == 0) {
                        System.out.println(dateToStr(LocalDateTime.now(), format1) + "系统判断" + user.getName() + "的推送起止时间设置不合法，不予推送");
                    } else if (!(localDateTimeToMilli > set.getStart() && localDateTimeToMilli < set.getEnd())) {
                        System.out.println(dateToStr(LocalDateTime.now(), format1) + "系统判断" + user.getName() + "的推送不在推送时间段内，不予推送");
                    } else {
                        alarm(dateTime, set.getAlarmTags(), user.getNo());
                    }
                }
            }
        }
    }

    private void push(LocalDate date, List<Tag> tags, String alias) {
        Push push1 = packDailyProduceData(date, tags);
        Push push2 = packDailySaleData(date, tags);
        if (push1 != null || push2 != null)
            System.out.println(dateToStr(LocalDateTime.now(), format1) + "系统尝试发送数据推送，目标别名：" + alias);
        if (push2 != null) {
            push2.setReceiver(alias);
            sendSingleMessage(1, alias, transmissionTemplate(JSON.toJSONString(push2)));
        }
        if (push1 != null) {
            push1.setReceiver(alias);
            sendSingleMessage(1, alias, transmissionTemplate(JSON.toJSONString(push1)));
        }
    }

    private void alarm(LocalDateTime date, List<AlarmTag> tags, String alias) {
        Alarm alarm = packDailyAlarm(date, tags);
        if (alarm != null) {
            alarm.setAlarmUuid(getNum19());
            alarm.setReceiver(alias);
            String message = JSON.toJSONString(alarm);
            System.out.println(dateToStr(LocalDateTime.now(), format1) + "系统尝试发送预警推送，目标别名：" + alias);
            sendSingleMessage(1, alias, transmissionTemplate(message));
        }
    }

    @Override
    public Push packDailyProduceData(LocalDate date, List<Tag> tags) {
        Tag tagDailyProduceCountList = tagDao.findByName(DailyProduceCountList);
        Tag tagDailyCountOfModel = tagDao.findByName(DailyCountOfModel);
        Tag tagDailyProduceQualityList = tagDao.findByName(DailyProduceQualityList);
        Tag tagDailyConsume = tagDao.findByName(DailyConsume);
        if (tags.contains(tagDailyProduceCountList) ||
                tags.contains(tagDailyCountOfModel) ||
                tags.contains(tagDailyProduceQualityList) ||
                tags.contains(tagDailyConsume)) {
            LocalDateTime now = LocalDateTime.now();
            long time = localDateTimeToMilli(now) + 60 * 1000;
            Push push = new Push(time);
            List<BaseChart> content = new ArrayList<>();
            for (Tag tag : tags) {
                if (tag.getName().equals(DailyProduceCountList))
                    content.add(packDailyProduceCountList(date));
                if (tag.getName().equals(DailyCountOfModel))
                    content.add(packDailyCountOfModel(date));
                if (tag.getName().equals(DailyProduceQualityList))
                    content.add(packDailyProduceQualityList(date));
                if (tag.getName().equals(DailyConsume))
                    content.add(packDailyConsume(date));
            }
            push.setContent(JSON.toJSONString(content));
            if (content.size() > 0)
                push.setDefaultSubMenu(content.get(0).getSubmenu());
            push.setTitle(dateToStr(date, DATE_FORMAT_CN) + "数据");
            push.setPushUuid(getNum19());
            return push;
        } else
            return null;
    }

    @Override
    public Push packDailySaleData(LocalDate date, List<Tag> tags) {
        Tag tagDailySaleCount = tagDao.findByName(DailySaleCount);
        Tag tagDailyDeliveryCount = tagDao.findByName(DailyDeliveryCount);
        Tag tagDailySale = tagDao.findByName(DailySale);
        Tag tagDailyCustomRate = tagDao.findByName(DailyCustomRate);
        if (tags.contains(tagDailySaleCount) ||
                tags.contains(tagDailyDeliveryCount) ||
                tags.contains(tagDailySale) ||
                tags.contains(tagDailyCustomRate)) {
            LocalDateTime now = LocalDateTime.now();
            long time = localDateTimeToMilli(now) + 60 * 1000;
            Push push = new Push(time);
            List<BaseChart> content = new ArrayList<>();
            for (Tag tag : tags) {
                if (tag.getName().equals(DailySaleCount))
                    content.add(packDailySaleCount(date));
                if (tag.getName().equals(DailyDeliveryCount))
                    content.add(packDailyDeliveryCount(date));
                if (tag.getName().equals(DailySale))
                    content.add(packDailySale(date));
                if (tag.getName().equals(DailyCustomRate))
                    content.add(packDailyCustomRate(date));
            }
            push.setContent(JSON.toJSONString(content));
            if (content.size() > 0)
                push.setDefaultSubMenu(content.get(0).getSubmenu());
            push.setTitle(dateToStr(date, DATE_FORMAT_CN) + "数据");
            push.setPushUuid(getNum19());
            return push;
        } else
            return null;
    }

    private final String[] sixDivideTimes = {"00:00:00", "04:00:00", "08:00:00", "12:00:00", "16:00:00", "20:00:00", "23:59:59"};

    @Override
    public BaseChart packDailyProduceCount(LocalDate date) {
        BaseChart produceData = new BaseChart();
        List<BaseEntry> yValues = new ArrayList<>();
        produceData.setMenu("生产信息");
        produceData.setSubmenu("生产量");
        produceData.setTitle("产品生产总量统计");
        produceData.setLabel("生产总量");
        produceData.setChart_type(bar_chart);
        produceData.setOnly(true);
        produceData.setxValues(new String[]{"0时", "4时", "8时", "12时", "16时", "20时", "24时"});
        for (int i = 0, j = i + 1; j < sixDivideTimes.length; i++, j++) {
            List<Products> products = productDao.findProductsByDateBetween(stringToLocalDateTime(date, sixDivideTimes[i]), stringToLocalDateTime(date, sixDivideTimes[j]));
            int success = 0;
            for (Products product : products) {
                success += product.getPlan() - product.getFail();
            }
            yValues.add(new BaseEntry((float) j, (float) success));
        }
        produceData.setyValues(yValues);
        return produceData;
    }

    @Override
    public BaseChart packDailyProduceCountList(LocalDate date) {
        BaseChart produceData = new BaseChart();
        List<List<BaseEntry>> yValues = new ArrayList<>();
        List<BaseEntry> yValue1 = new ArrayList<>();
        List<BaseEntry> yValue2 = new ArrayList<>();
        produceData.setMenu("生产信息");
        produceData.setSubmenu("生产量");
        produceData.setTitle("产品生产总量统计");
        produceData.setLabels(Arrays.asList("生产数", "残片数"));
        produceData.setChart_type(bar_chart);
        produceData.setOnly(true);
        produceData.setxValues(new String[]{"0时", "4时", "8时", "12时", "16时", "20时", "24时"});
        for (int i = 0, j = i + 1; j < sixDivideTimes.length; i++, j++) {
            List<Products> products = productDao.findProductsByDateBetween(stringToLocalDateTime(date, sixDivideTimes[i]), stringToLocalDateTime(date, sixDivideTimes[j]));
            int plan = 0, fail = 0;
            for (Products product : products) {
                plan += product.getPlan();
                fail += product.getFail();
            }
            yValue1.add(new BaseEntry((float) j, (float) plan));
            yValue2.add(new BaseEntry((float) j, (float) fail));
        }
        yValues.add(yValue1);
        yValues.add(yValue2);
        produceData.setyListValues(yValues);
        return produceData;
    }

    @Override
    public BaseChart packDailyCountOfModel(LocalDate date) {
        BaseChart produceData = new BaseChart();
        List<BaseEntry> yValues = new ArrayList<>();
        produceData.setMenu("生产信息");
        produceData.setSubmenu("生产型号统计");
        produceData.setTitle("各型号生产统计");
        produceData.setDescription("各型号玻璃生产数量");
        produceData.setLabel("各型号玻璃生产数量");
        produceData.setChart_type(pie_chart);
        produceData.setOnly(true);
        String[] labels = glassService.xValues();
        produceData.setLabels(Arrays.asList(labels));
        produceData.setxValues(labels);
        for (int i = 0; i < labels.length; i++) {
            List<Products> products = productDao.findProductsByModel(glassService.findByModel(labels[i]));
            int success = 0;
            for (Products product : products) {
                success += product.getPlan() - product.getFail();
            }
            yValues.add(new BaseEntry(labels[i], (float) success));
        }
        produceData.setyValues(yValues);
        return produceData;
    }

    @Override
    public BaseChart packDailyProduceQualityList(LocalDate date) {
        BaseChart produceData = new BaseChart();
        List<List<BaseEntry>> yValues = new ArrayList<>();
        List<BaseEntry> yValue1 = new ArrayList<>();
        List<BaseEntry> yValue2 = new ArrayList<>();
        List<BaseEntry> yValue3 = new ArrayList<>();
        produceData.setMenu("生产信息");
        produceData.setSubmenu("生产质量");
        produceData.setTitle("产品生产质量统计");
        produceData.setLabels(Arrays.asList("镀膜成功率", "钢化成功率", "残片率"));
        produceData.setChart_type(bar_chart);
        produceData.setOnly(false);
        produceData.setxValues(glassService.xValues());
        List<Glass> glasses = glassService.findAll();
        for (int i = 0; i < glasses.size(); i++) {
            List<Products> products = productDao.findProductsByModel(glasses.get(i));
            int coat = 0, harden = 0, fail = 0, plan = 0;
            for (Products product : products) {
                coat += product.getCoat();
                harden += product.getHarden();
                fail += product.getFail();
                plan += product.getPlan();
            }
            yValue1.add(new BaseEntry((float) i + 1, (float) coat * 100 / (float) plan));
            yValue2.add(new BaseEntry((float) i + 1, (float) harden * 100 / (float) plan));
            yValue3.add(new BaseEntry((float) i + 1, (float) fail * 100 / (float) plan));
        }
        yValues.add(yValue1);
        yValues.add(yValue2);
        yValues.add(yValue3);
        produceData.setyListValues(yValues);
        return produceData;
    }

    @Override
    public BaseChart packDailyConsume(LocalDate date) {
        BaseChart produceDate = new BaseChart();
        List<BaseEntry> yValues = new ArrayList<>();
        produceDate.setMenu("生产信息");
        produceDate.setSubmenu("生产能耗");
        produceDate.setTitle("生产能耗统计");
        produceDate.setDescription("各类型生产能耗");
        produceDate.setLabels(Arrays.asList(new String[]{"电消耗", "煤消耗", "水消耗"}));
        produceDate.setChart_type(ring_chart);
        produceDate.setOnly(true);
        String[] labels = new String[]{"电消耗", "煤消耗", "水消耗"};
        produceDate.setxValues(labels);
        ProductNotes productNote = productNoteDao.findByDate(date);
        yValues.add(new BaseEntry(labels[0], (float) productNote.getElectricity()));
        yValues.add(new BaseEntry(labels[1], (float) productNote.getCoal()));
        yValues.add(new BaseEntry(labels[2], (float) productNote.getWater()));
        produceDate.setyValues(yValues);
        return produceDate;
    }

    @Override
    public BaseChart packDailySaleCount(LocalDate date) {
        BaseChart saleData = new BaseChart();
        List<List<BaseEntry>> yValues = new ArrayList<>();
        saleData.setMenu(SaleData);
        saleData.setSubmenu(DailySaleCount);
        saleData.setTitle("产品销售量统计");
        String[] labels = glassService.xValues();
        List<Glass> glasses = glassDao.findAll();
        saleData.setLabels(Arrays.asList(labels));
        saleData.setChart_type(line_chart);
        saleData.setOnly(false);
        saleData.setxValues(new String[]{"0时", "4时", "8时", "12时", "16时", "20时", "24时"});
        for (Glass glass : glasses) {
            List<BaseEntry> yValue = new ArrayList<>();
            for (int i = 0, j = i + 1; j < sixDivideTimes.length; i++, j++) {
                List<Orders> orders = orderDao.findOrdersByDateBetween(stringToLocalDateTime(date, sixDivideTimes[i]), stringToLocalDateTime(date, sixDivideTimes[j]));
                for (Orders order : orders) {
                    List<OrderItems> orderItems = order.getOrderItems();
                    float sum = 0;
                    for (OrderItems orderItem : orderItems) {
                        if (orderItem.getModel() == glass) {
                            sum += orderItem.getDelivery();
                        }
                    }
                    yValue.add(new BaseEntry((float) j, (float) sum));
                }
            }
            yValues.add(yValue);
        }
        saleData.setyListValues(yValues);
        return saleData;
    }

    @Override
    public BaseChart packDailyDeliveryCount(LocalDate date) {
        BaseChart saleData = new BaseChart();
        List<List<BaseEntry>> yValues = new ArrayList<>();
        saleData.setMenu(SaleData);
        saleData.setSubmenu(DailyDeliveryCount);
        saleData.setTitle("产品交易量统计");
        String[] labels = glassService.xValues();
        List<Glass> glasses = glassDao.findAll();
        saleData.setLabels(Arrays.asList(labels));
        saleData.setChart_type(line_chart);
        saleData.setOnly(false);
        saleData.setxValues(new String[]{"0时", "4时", "8时", "12时", "16时", "20时", "24时"});
        for (Glass glass : glasses) {
            List<BaseEntry> yValue = new ArrayList<>();
            for (int i = 0, j = i + 1; j < sixDivideTimes.length; i++, j++) {
                List<Orders> orders = orderDao.findOrdersByDateBetween(stringToLocalDateTime(date, sixDivideTimes[i]), stringToLocalDateTime(date, sixDivideTimes[j]));
                for (Orders order : orders) {
                    List<OrderItems> orderItems = order.getOrderItems();
                    float sum = 0;
                    for (OrderItems orderItem : orderItems) {
                        if (orderItem.getModel() == glass) {
                            sum += orderItem.getDelivery();
                        }
                    }
                    yValue.add(new BaseEntry((float) j, (float) sum));
                }
            }
            yValues.add(yValue);
        }
        saleData.setyListValues(yValues);
        return saleData;
    }

    @Override
    public BaseChart packDailySale(LocalDate date) {
        BaseChart saleData = new BaseChart();
        List<BaseEntry> yValues = new ArrayList<>();
        saleData.setMenu(SaleData);
        saleData.setSubmenu(DailySale);
        saleData.setTitle("产品销售总额统计");
        saleData.setLabel("产品销售总额");
        saleData.setChart_type(line_chart);
        saleData.setOnly(true);
        saleData.setxValues(new String[]{"0时", "4时", "8时", "12时", "16时", "20时", "24时"});
        for (int i = 0, j = i + 1; j < sixDivideTimes.length; i++, j++) {
            List<Orders> orders = orderDao.findOrdersByDateBetween(stringToLocalDateTime(date, sixDivideTimes[i]), stringToLocalDateTime(date, sixDivideTimes[j]));
            int sum = 0;
            for (Orders order : orders) {
                sum += order.getPrice();
            }
            yValues.add(new BaseEntry((float) j, (float) sum));
        }
        saleData.setyValues(yValues);
        return saleData;
    }

    @Override
    public BaseChart packDailyCustomRate(LocalDate date) {
        BaseChart saleData = new BaseChart();
        List<BaseEntry> yValues = new ArrayList<>();
        saleData.setMenu(SaleData);
        saleData.setSubmenu(DailyCustomRate);
        saleData.setTitle("销售订单满意率统计");
        saleData.setLabel("顾客满意率");
        saleData.setChart_type(line_chart);
        saleData.setOnly(true);
        saleData.setxValues(new String[]{"0时", "4时", "8时", "12时", "16时", "20时", "24时"});
        for (int i = 0, j = i + 1; j < sixDivideTimes.length; i++, j++) {
            List<Orders> orders = orderDao.findOrdersByDateBetween(stringToLocalDateTime(date, sixDivideTimes[i]), stringToLocalDateTime(date, sixDivideTimes[j]));
            int sum = 0;
            int count = orders.size();
            for (Orders order : orders) {
                sum += order.getRate();
            }
            yValues.add(new BaseEntry((float) j, (float) sum / (count * 5.0) * 100));
        }
        saleData.setyValues(yValues);
        return saleData;
    }

    @Override
    public Alarm packDailyAlarm(LocalDateTime date, List<AlarmTag> tags) {
        if (tags.size() == 0)
            return null;
        else {
            LocalDateTime now = LocalDateTime.now();
            long time = localDateTimeToMilli(now) + 60 * 1000;
            Alarm alarm = new Alarm(time);
            List<AlarmLog> alarmContent = new ArrayList<>();
            boolean error = false;
            for (AlarmTag tag : tags) {
                if (tag.getContent().equals(ALARM_TAGS[0])) {
                    String observe = observeFailRate(date, tag);
                    if (!isEmpty(observe)) {
                        error = true;
                        alarmContent.add(new AlarmLog(tag.getContent(), observe));
                    }
                }
                if (tag.getContent().equals(ALARM_TAGS[1])) {
                    String observe = observeElecConsu(date, tag);
                    if (!isEmpty(observe)) {
                        error = true;
                        alarmContent.add(new AlarmLog(tag.getContent(), observe));
                    }
                }
                if (tag.getContent().equals(ALARM_TAGS[2])) {
                    String observe = observeWtrConsu(date, tag);
                    if (!isEmpty(observe)) {
                        alarmContent.add(new AlarmLog(tag.getContent(), observe));
                    }
                }
                if (tag.getContent().equals(ALARM_TAGS[3])) {
                    String observe = observeCoalConsu(date, tag);
                    if (!isEmpty(observe)) {
                        error = true;
                        alarmContent.add(new AlarmLog(tag.getContent(), observe));
                    }
                }
            }
            alarm.setContent(JSON.toJSONString(alarmContent));
            alarm.setTitle(dateToStr(date, DATE_FORMAT_CN) + "预警");
            if (error)
                return alarm;
            else
                return null;
        }
    }

    @Override
    public String observeFailRate(LocalDateTime date, AlarmTag tag) {
        Date start = localDateTimeToDate(date);
        start.setMinutes(0);
        start.setSeconds(0);
        Date end = localDateTimeToDate(date);
        end.setMinutes(59);
        end.setSeconds(59);
        List<Products> products = productDao.findProductsByDateBetween(dateToLocalDateTime(start), dateToLocalDateTime(end));
        boolean error = false;
        String result = "观测" + ALARM_TAGS[0] + ": \n";
        for (Products product : products) {
            float failRate = (float) product.getFail() / (float) product.getPlan() * 100;
            String fail = product.getModel().getModel() + ":" + df2.format(failRate) + "% \n";
            if (failRate < tag.getMin() || failRate > tag.getMax()) {
                result += fail;
                error = true;
            }
        }
        if (error)
            return result;
        else
            return null;
    }

    @Override
    public String observeElecConsu(LocalDateTime date, AlarmTag tag) {
        ProductNotes productNote = productNoteDao.findByDate(date.toLocalDate());
        boolean error = false;
        String result = "观测" + ALARM_TAGS[1] + ": ";
        String elec = df2.format(productNote.getElectricity()) + "\n";
        if (productNote.getElectricity() < tag.getMin() || productNote.getElectricity() > tag.getMax()) {
            result += elec;
            error = true;
        }
        if (error)
            return result;
        else
            return null;
    }

    @Override
    public String observeWtrConsu(LocalDateTime date, AlarmTag tag) {
        ProductNotes productNote = productNoteDao.findByDate(date.toLocalDate());
        boolean error = false;
        String result = "观测" + ALARM_TAGS[2] + ": ";
        String water = df2.format(productNote.getWater()) + "\n";
        if (productNote.getWater() < tag.getMin() || productNote.getWater() > tag.getMax()) {
            result += water;
            error = true;
        }
        if (error)
            return result;
        else
            return null;
    }

    @Override
    public String observeCoalConsu(LocalDateTime date, AlarmTag tag) {
        ProductNotes productNote = productNoteDao.findByDate(date.toLocalDate());
        boolean error = false;
        String result = "观测" + ALARM_TAGS[3] + ": ";
        String coal = df2.format(productNote.getCoal()) + "\n";
        if (productNote.getCoal() < tag.getMin() || productNote.getCoal() > tag.getMax()) {
            result += coal;
            error = true;
        }
        if (error)
            return result;
        else
            return null;
    }

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
