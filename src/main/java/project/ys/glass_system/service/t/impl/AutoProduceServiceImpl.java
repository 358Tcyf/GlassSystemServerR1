package project.ys.glass_system.service.t.impl;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.ys.glass_system.model.t.dao.*;
import project.ys.glass_system.model.t.entity.*;
import project.ys.glass_system.service.t.AutoProduceService;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static project.ys.glass_system.util.LocalDateUtils.DATE_TIME_FORMAT;
import static project.ys.glass_system.util.LocalDateUtils.dateToStr;
import static project.ys.glass_system.util.RandomUtils.randomFloat;
import static project.ys.glass_system.util.RandomUtils.randomInt;
import static project.ys.glass_system.util.UuidUtil.getNum14;
import static project.ys.glass_system.util.UuidUtil.getNum19;

@Service
public class AutoProduceServiceImpl implements AutoProduceService {

    @Resource
    GlassModelDao modelDao;

    @Resource
    ScheduleDao scheduleDao;

    @Resource
    ProduceNoteDao noteDao;

    @Resource
    ProduceDao produceDao;

    @Resource
    GlassItemDao glassItemDao;

    @Resource
    FactoryDao factoryDao;

    @Resource
    WarehouseDao warehouseDao;

    @Resource
    WarehouseItemDao warehouseItemDao;

    @Resource
    TestNoteDao testNoteDao;

    @Resource
    TestDao testDao;
    @Resource
    TestRankDao rankDao;
    @Resource
    TestResultDao testResultDao;

    @Override
    public void produceStart(LocalDate date, boolean ignore) {
        /*
         * 7点制定好生产计划，方便8点开始生产
         * */
        if (LocalDateTime.now().getHour() == 7 || ignore) {
            ProduceNote note = new ProduceNote();
            note.setDate(date);
            if (noteDao.exists(Example.of(note))) return;
            noteDao.save(note);
            List<GlassModel> models = modelDao.findAll();
            List<Schedule> plans = new ArrayList<>();
            for (GlassModel model : models) {
//                if (randomBoolean())
                plans.add(new Schedule(model, note, randomInt(100, 300)));
            }
            scheduleDao.saveAll(plans);
        }
    }

    @Override
    public void produce(boolean ignore) {
        LocalDateTime time = LocalDateTime.now();
        /*
         * 只允许在8时到20时之间进行生产
         * */
        if ((time.getHour() >= 8 && time.getHour() < 20) || ignore) {
            ProduceNote todayNote = noteDao.findByDate(time.toLocalDate());
            List<Schedule> plans = todayNote.getPlans();
            int i = randomInt(0, plans.size());                                 /*在计划生产的玻璃中选择一种型号进行生产*/
            GlassModel produceModel = plans.get(i).getModel();
            int planNum = plans.get(i).getNum();                                                /*获得计划生产数*/
            int finishNum = produceDao.sumNumByBelongAndModel(todayNote, produceModel);         /*查询已生产数*/
            int restNum = randomInt(0, planNum - finishNum);                                    /*得到剩余还需生产数*/
            if (restNum <= -20)
                return;                                                                         /*允许多生产20块玻璃，如果无须生产则结束生产*/
            int doNum = randomInt(1, 20);                                                       /*一次最多生产20块玻璃*/
            ProduceItem produce = new ProduceItem(time, getNum14("PRO"));
            produce.setPlan(todayNote, produceModel, doNum);
            float weight = 0.03f * doNum * produceModel.getLength() * produceModel.getWidth();  /*计算玻璃的重量*/
            /*
             * 模拟生产一吨玻璃的各消耗
             * */
            produce.setConsu(weight * randomFloat(5, 10),
                    weight * randomFloat(5, 7.5f) * 100,
                    weight * randomFloat(2, 4),
                    weight * randomFloat(2, 2.5f));
            produceDao.save(produce);
            List<GlassItem> glassItems = new ArrayList<>();
            for (int j = 0; j < doNum; j++) {
                /*
                 * 程序睡眠1秒，防止uuid重复
                 * */
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                glassItems.add(new GlassItem(produce, produce.getModel(), getNum14(produce.getModel().getUuid())));
            }
            glassItemDao.saveAll(glassItems);
        }
    }

    @Override
    public void store(boolean ignore) {
        LocalDateTime time = LocalDateTime.now();
        if ((time.getHour() >= 9 && time.getHour() <= 21 && time.getHour() % 2 == 1) || ignore) {
            List<Factory> factories = factoryDao.findAll();
            Factory factory = factories.get(randomInt(0, factories.size()));
            List<Warehouse> warehouses = warehouseDao.findByOwn(factory);
            Warehouse warehouse = warehouses.get(randomInt(0, warehouses.size()));
            WarehouseItem warehouseItem = new WarehouseItem(getNum19(), LocalDateTime.now(), warehouse);
            warehouseItemDao.save(warehouseItem);
            ProduceNote note = noteDao.findByDate(LocalDate.now());
            List<ProduceItem> produceItems = produceDao.findByBelong(note);
            List<GlassItem> glassItems = glassItemDao.findByStoreAndProduceIn(null, produceItems);
            List<GlassItem> saveGlass = new ArrayList<>();
            for (GlassItem item : glassItems) {
                item.setStore(warehouseItem);
                saveGlass.add(item);
            }
            glassItemDao.saveAll(saveGlass);
        }
    }

    @Override
    public void testStart(LocalDate date, boolean ignore) {
        /*
         * 7点准备检测
         * */
        if (LocalDateTime.now().getHour() == 7 || ignore) {
            TestNote note = new TestNote();
            note.setDate(date);
            if (testNoteDao.exists(Example.of(note))) return;
            testNoteDao.save(note);
        }
    }

    @Override
    public void test(boolean ignore) {
        LocalDateTime time = LocalDateTime.now();
        /*
         * 8点-20点间进行检测，每隔2小时进行一次
         * */
        if ((time.getHour() >= 8 && time.getHour() <= 20 && time.getHour() % 2 == 0) || ignore) {
            TestNote note = testNoteDao.findByDate(time.toLocalDate());
            TestItem test = new TestItem(time, getNum14("TES"), note);                  /*新建检测批次*/
            testDao.save(test);
            List<WarehouseItem> warehouseItems = warehouseItemDao.findByTest(false);          /*查找仓储单例中哪批是未经检测的*/
            List<WarehouseItem> saveWarehouse = new ArrayList<>();
            List<GlassItem> glassItems = glassItemDao.findByStoreIn(warehouseItems);          /*得到这些仓储批次的玻璃*/
            List<GlassItem> saveGlass = new ArrayList<>();
            List<TestRank> ranks = rankDao.findAll();
            List<GlassModel> models = modelDao.findAll();
            /*
             * 检测玻璃，随机给出玻璃等级
             * */
            for (GlassItem item : glassItems) {
                item.setTest(test);
                item.setRank(ranks.get(randomInt(0, ranks.size())));
                saveGlass.add(item);
            }
            glassItemDao.saveAll(saveGlass);
            /*
             * 将仓储批次设为已被检测
             * */
            for (WarehouseItem item : warehouseItems) {
                item.setTest(true);
                saveWarehouse.add(item);
            }
            warehouseItemDao.saveAll(saveWarehouse);
            /*
             * 给检测批次存储检测结果
             * */
            List<TestResult> testResults = new ArrayList<>();
            for (TestRank rank : ranks) {
                for (GlassModel model : models) {
                    testResults.add(new TestResult(test, model, rank, glassItemDao.countByModelAndRankAndTest(model, rank, test)));
                }
            }
            testResultDao.saveAll(testResults);
        }
    }

    @Override
    public Map<String, Object> factoryQuery(int page, int limit) {
        Pageable pageable = new PageRequest(page - 1, limit);
        Page<Factory> factories = factoryDao.findAll(pageable);
        Map<String, Object> map = new HashMap<>();
        map.put("count", factories.getTotalElements());
        List<Map<String, Object>> listMap = new ArrayList<>();
        for (Factory factory : factories) {
            Map<String, Object> o = new HashMap<>();
            o.put("uuid", factory.getUuid());
            o.put("name", factory.getName());
            o.put("province", factory.getProvince());
            o.put("city", factory.getCity());
            listMap.add(o);
        }
        map.put("object", listMap);
        return map;
    }

    @Override
    public Map<String, Object> produceQuery(int page, int limit) {
        Pageable pageable = new PageRequest(page - 1, limit);
        Page<ProduceItem> produceItems = produceDao.findAll(pageable);
        Map<String, Object> map = new HashMap<>();
        map.put("count", produceItems.getTotalElements());
        List<Map<String, Object>> listMap = new ArrayList<>();
        for (ProduceItem item : produceItems) {
            Map<String, Object> o = new HashMap<>();
            o.put("uuid", item.getUuid());
            o.put("model", item.getModel().getName());
            o.put("num", item.getNum());
            o.put("date", item.getBelong().getDate());
            o.put("time", item.getTime().toLocalTime());
            o.put("water", item.getWater() + "吨");
            o.put("electricity", item.getElectricity() + "千瓦时");
            o.put("material", item.getMaterial() + "吨");
            o.put("coal", item.getCoal() + "吨");
            listMap.add(o);
        }
        map.put("object", listMap);
        return map;
    }

    @Override
    public Map<String, Object> glassQuery(int page, int limit) {
        Pageable pageable = new PageRequest(page - 1, limit);
        Page<GlassItem> glassItems = glassItemDao.findAll(pageable);
        Map<String, Object> map = new HashMap<>();
        map.put("count", glassItems.getTotalElements());
        List<Map<String, Object>> listMap = new ArrayList<>();
        for (GlassItem item : glassItems) {
            Map<String, Object> o = new HashMap<>();
            o.put("uuid", item.getUuid());
            o.put("model", item.getModel().getName());
            o.put("produce",  dateToStr(item.getProduce().getTime(),DATE_TIME_FORMAT));
            o.put("store", item.getStore().getBelong().getName());
            o.put("factory", item.getStore().getBelong().getOwn().getName());
            o.put("test", dateToStr(item.getTest().getTime(),DATE_TIME_FORMAT));
            o.put("rank", item.getRank().getName());
            listMap.add(o);
        }
        map.put("object", listMap);
        return map;
    }

    @Override
    public Map<String, Object> wareHouseQuery(int page, int limit) {
        Pageable pageable = new PageRequest(page - 1, limit);
        Page<WarehouseItem> warehouses = warehouseItemDao.findAll(pageable);
        Map<String, Object> map = new HashMap<>();
        map.put("count", warehouses.getTotalElements());
        List<Map<String, Object>> listMap = new ArrayList<>();
        for (WarehouseItem item : warehouses) {
            Map<String, Object> o = new HashMap<>();
            o.put("uuid", item.getUuid());
            o.put("sum", glassItemDao.countByStore(item));
            o.put("time",dateToStr(item.getTime(),DATE_TIME_FORMAT));
            o.put("check", item.isTest());
            o.put("store", item.getBelong().getName());
            o.put("factory", item.getBelong().getOwn().getName());
            listMap.add(o);
        }
        map.put("object", listMap);
        return map;
    }

    @Override
    public Map<String, Object> testQuery(int page, int limit) {
        Pageable pageable = new PageRequest(page - 1, limit);
        Page<TestResult> testResults = testResultDao.queryByNumNot(0,pageable);
        Map<String, Object> map = new HashMap<>();
        map.put("count", testResults.getTotalElements());
        List<Map<String, Object>> listMap = new ArrayList<>();
        for (TestResult result : testResults) {
            Map<String, Object> o = new HashMap<>();
            o.put("uuid", result.getBelong().getUuid());
            o.put("time", dateToStr(result.getBelong().getTime(),DATE_TIME_FORMAT));
            o.put("model", result.getModel().getName());
            o.put("rank", result.getRank().getName());
            o.put("num", result.getNum());
            listMap.add(o);
        }
        map.put("object", listMap);
        return map;    }
}
