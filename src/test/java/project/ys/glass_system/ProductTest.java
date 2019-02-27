package project.ys.glass_system;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import project.ys.glass_system.model.s.entity.Products;
import project.ys.glass_system.service.ProductService;
import project.ys.glass_system.service.impl.ProductServiceImpl;
import project.ys.glass_system.service.impl.UserServiceImpl;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static project.ys.glass_system.utils.DateUtil.accurateToDate;
import static project.ys.glass_system.utils.DateUtil.format1;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = GlassSystemApplication.class)
public class ProductTest {


    @Resource
    ProductServiceImpl productService;

    @Test
    public void testProductOnce() {
        SimpleDateFormat sdf = new SimpleDateFormat(format1);
        Date date = null;
        try {
            date = sdf.parse("2019-01-01 10:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(accurateToDate(date));
        Products products = new Products(date);
        productService.onceProduct(products);
    }
}
