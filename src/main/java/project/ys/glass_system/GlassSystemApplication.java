package project.ys.glass_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import project.ys.glass_system.util.ApplicationContextUtils;

@SpringBootApplication
public class GlassSystemApplication {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(GlassSystemApplication.class, args);
        ApplicationContextUtils.setApplicationContext(ctx);
//        SpringApplication.run(GlassSystemApplication.class, args);    //必须注释，否则上面两句无法执行
        System.out.println("\n\n\n==============================提示：服务器启动==============================\n\n\n");
//        onceProductNew();

    }

}

