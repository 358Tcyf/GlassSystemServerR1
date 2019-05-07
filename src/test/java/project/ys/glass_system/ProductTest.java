package project.ys.glass_system;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import project.ys.glass_system.model.s.entity.Products;
import project.ys.glass_system.service.impl.GlassServiceImpl;
import project.ys.glass_system.service.impl.ProductServiceImpl;
import project.ys.glass_system.service.impl.SaleServiceImpl;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = GlassSystemApplication.class)
public class ProductTest {

    @Resource
    ProductServiceImpl productService;

    @Resource
    GlassServiceImpl glassService;

    @Resource
    SaleServiceImpl saleService;


    @Before
    public void before() {
        System.out.println("=============================\n");
    }

    @Test
    public void testAddGlass() {
//        glassService.addVirtualGlass(new GlassItem("有色透明有机玻璃", 180));
//        glassService.addVirtualGlass(new GlassItem("透明有机玻璃", 180));
//        glassService.addVirtualGlass(new GlassItem("珠光有机玻璃", 220));
//        glassService.addVirtualGlass(new GlassItem("压花有机玻璃", 200));
    }

    @Test
    public void testAddCustomer() {
//        for (int i = 0; i < 20; i++) {
//            String name = getChineseName();
//            String phone = getTelephone();
//            String email = getEmail(5, 10);
//
//            glassService.addVirtualCustomer(new Customers(name, phone, email));
//        }
    }

    @Test
    public void testLocalDateTime() {
//        System.out.println(LocalDate.now());
//        System.out.println(LocalDateTime.now());
//        System.out.println(LocalTime.now());
//        LocalDateTime dateTime = LocalDateTime.parse(dateToStr(LocalDate.now(), DATE_FORMAT)+"T15:00:00");
//        Date statTime = localDateTimeToDate(dateTime);
//        System.out.println(LocalDateTime.parse(dateToStr(LocalDate.now(), DATE_FORMAT) + "T15:00:00"));
//        System.out.println(statTime);
    }

    @Test
    public void testVirtualProduce() {
        List<Products> products = new ArrayList<>();
//        Products onceProduce = glassService.virtualProduce("有色透明有机玻璃");
//        products.add(glassService.virtualProduce("有色透明有机玻璃"));
//        products.add(glassService.virtualProduce("有色透明有机玻璃"));
//        products.add(glassService.virtualProduce("有色透明有机玻璃"));
//        System.out.println(products);
//        productService.dailyProduct(products);

    }

    @Test
    public void testVirtualSale() {
//        Customers customer = glassService.getCustomer("13707302123");
//        List<OrderItems> orderItems = new ArrayList<>();
//        orderItems.add(glassService.virtualSale("有色透明有机玻璃"));
//        orderItems.add(glassService.virtualSale("有色透明有机玻璃"));
//        Orders orders = new Orders(LocalDateTime.now(), customer);
//        saleService.ordersOfDay(saleService.ordersOfCustomer(orders, orderItems));
    }

    @After
    public void after() {
        System.out.println("\n=============================");
    }
}
