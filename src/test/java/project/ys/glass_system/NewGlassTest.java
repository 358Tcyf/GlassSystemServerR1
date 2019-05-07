package project.ys.glass_system;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import project.ys.glass_system.model.t.dao.*;
import project.ys.glass_system.model.t.entity.Factory;
import project.ys.glass_system.model.t.entity.GlassModel;
import project.ys.glass_system.model.t.entity.TestRank;
import project.ys.glass_system.model.t.entity.Warehouse;
import project.ys.glass_system.service.t.impl.AutoProduceServiceImpl;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

import static project.ys.glass_system.util.UuidUtil.getNum19;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = GlassSystemApplication.class)
public class NewGlassTest {

    @Resource
    GlassModelDao modelDao;

    @Resource
    FactoryDao factoryDao;
    @Resource
    WarehouseDao warehouseDao;
    @Resource
    TestRankDao rankDao;

    @Resource
    ProduceNoteDao noteDao;

    @Resource
    ScheduleDao scheduleDao;
    @Resource
    ProduceDao produceDao;
    @Resource
    WarehouseItemDao warehouseItemDao;

    @Resource
    AutoProduceServiceImpl produceService;

//    @Before
    public void before() {
        if (modelDao.findAll().size() <= 0)
            testCreateModel();
        if (factoryDao.findAll().size() <= 0) {
            testCreateFactory();
        }
        testCreateWareHouse();
        if (rankDao.findAll().size() <= 0)
            testCreateRank();
        System.out.println("=============================\n");
    }

    //    @Test
    public void testCreateModel() {
        modelDao.save(new GlassModel("光伏玻璃"));
        modelDao.save(new GlassModel("浮法玻璃"));
    }


    //    @Test
    public void testCreateFactory() {
        factoryDao.save(new Factory("浙江昱虹玻璃有限公司", getNum19(), "浙江省", "浙江杭州"));
        factoryDao.save(new Factory("浙江天驰玻璃有限公司", getNum19(), "浙江省", "浙江金华"));
        factoryDao.save(new Factory("浙江盛华玻璃有限公司", getNum19(), "浙江省", "浙江台州"));

    }

    //    @Test
    public void testCreateWareHouse() {
        List<Factory> factories = factoryDao.findAll();
        for (Factory factory : factories) {
            if (warehouseDao.findByOwn(factory).size() <= 0) {
                for (int i = 1; i <= 2; i++) {
                    warehouseDao.save(new Warehouse(factory.getName() + i + "号仓库", getNum19(), factory));
                }
            }
        }
    }

    //    @Test
    public void testCreateRank() {
        rankDao.save(new TestRank("一级品"));
        rankDao.save(new TestRank("二级品"));
        rankDao.save(new TestRank("三级品"));
    }

//    @Test
    public void testProduceSchedule() {
        System.out.print("生产准备：");
        produceService.produceStart(LocalDate.now(), true);
        for (int i = 2; i > 0; i--) {
            System.out.print(i + " ");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.print("\n生产开始：");
        produceService.produce(true);
        for (int i = 2; i > 0; i--) {
            try {
                System.out.print(i + " ");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.print("\n进行仓储：");
        produceService.store(true);
        for (int i = 2; i > 0; i--) {
            try {
                System.out.print(i + " ");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.print("\n质检准备：");
        produceService.testStart(LocalDate.now(), true);
        for (int i = 2; i > 0; i--) {
            try {
                System.out.print(i + " ");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.print("\n质检开始：");
        produceService.test(true);

    }

    @Test
    public void testOther() {

    }

    @Test
    public void testEmpty() {

    }

//    @After
    public void after() {
        System.out.println("\n=============================");
    }
}
