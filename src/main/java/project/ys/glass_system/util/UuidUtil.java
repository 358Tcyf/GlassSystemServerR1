package project.ys.glass_system.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author ys
 * @date on 2019/1/13 0:40
 */
public class UuidUtil {
    public static String getNum19() {
        String numStr = "";
        String trandStr = String.valueOf((Math.random() * 9 + 1) * 1000000);
        String dataStr = new SimpleDateFormat("yyyyMMddHHMMSS").format(new Date());
        numStr = trandStr.substring(0, 4);
        numStr = numStr + dataStr;
        return numStr;
    }
    public static String getNum14(String front) {
        String numStr = front;
        String dataStr = new SimpleDateFormat("yyyyMMddHHMMSS").format(new Date());
        numStr += dataStr;
        return numStr;
    }

    public static String getUUID32() {
        return UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
    }
}
