package project.ys.glass_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import project.ys.glass_system.utils.ApplicationContextUtil;

import static project.ys.glass_system.quartz.ProductQuartz.onceProduct;

@SpringBootApplication
public class GlassSystemApplication {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(GlassSystemApplication.class, args);
        ApplicationContextUtil.setApplicationContext(ctx);
//        SpringApplication.run(GlassSystemApplication.class, args);
        onceProduct();
    }

}

