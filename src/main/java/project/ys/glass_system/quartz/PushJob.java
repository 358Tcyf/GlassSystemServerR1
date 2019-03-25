package project.ys.glass_system.quartz;

import com.alibaba.fastjson.JSON;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import project.ys.glass_system.model.p.entity.PushSet;
import project.ys.glass_system.service.impl.PushServiceImpl;
import project.ys.glass_system.service.impl.SetServiceImpl;
import project.ys.glass_system.util.ApplicationContextUtils;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static project.ys.glass_system.getui.GetuiUtil.sendMessage;
import static project.ys.glass_system.getui.GetuiUtil.transmissionTemplate;
import static project.ys.glass_system.model.p.entity.PushSet.getTime;

public class PushJob implements Job {


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        PushServiceImpl pushService = ApplicationContextUtils.getBean(PushServiceImpl.class);
        SetServiceImpl setService = ApplicationContextUtils.getBean(SetServiceImpl.class);
        PushSet set = setService.getPushSet("P0001");
        int pushTime = getTime(set.getTime());
        int nowTime = LocalTime.now().getHour();
        System.out.println(pushTime);
        if ((nowTime + 1) % pushTime == 0) {
            System.out.println("系统于" + LocalDateTime.now() + "发布了推送");
            LocalDateTime time = LocalDateTime.now();
            String push = JSON.toJSONString(pushService.packDailyData(time.toLocalDate(), set.getTags()));
            sendMessage(transmissionTemplate(push));
        } else {
            System.out.println("现在是" + nowTime + 1 + "时");
            int h = 0;
            for (h = 0; h < nowTime + 1; h += pushTime) ;
            System.out.println("系统将在" + h + "时发送推送");
        }


    }
}
