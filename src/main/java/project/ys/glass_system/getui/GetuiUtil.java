package project.ys.glass_system.getui;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.ITemplate;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.TransmissionTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static project.ys.glass_system.util.DateUtils.format1;
import static project.ys.glass_system.util.LocalDateUtils.dateToStr;

/**
 * @author Simple
 * @date on 2019/1/2 22:12
 */
public class GetuiUtil {
    //定义常量, appId、appKey、masterSecret 采用本文档 "第二步 获取访问凭证 "中获得的应用配置
    private static String appId = "hAyc3SmOff6OQNRRCTIQ6A";
    private static String appkey = "NM18kjwn8T7jpBEMMwpMN1";
    private static String masterSecret = "5IusAdWQrT6Mdxg3YVjHd5";
    private static String url = "http://sdk.open.api.igexin.com/apiex.htm";


    /*
     * 发送给全体
     * */
    public static void sendMessage(ITemplate template) {
        IGtPush push = new IGtPush(url, appkey, masterSecret);
        List<String> appIds = new ArrayList<String>();
        appIds.add(appId);

        AppMessage message = new AppMessage();
        message.setData(template);
        message.setAppIdList(appIds);
        message.setOffline(true);
        message.setOfflineExpireTime(1000 * 60 * 60 * 24);
        IPushResult ret = push.pushMessageToApp(message);
        System.out.println(ret.getResponse().toString());
    }

    /*
     * type: 0 ->向单个clientid用户推送消息
     * type: 1->向单个别名用户推送消息
     * 某用户发生了一笔交易，银行及时下发一条推送消息给该用户
     * */
    public static void sendSingleMessage(int type, String str, ITemplate template) {
        IGtPush push = new IGtPush(url, appkey, masterSecret);
        SingleMessage message = new SingleMessage();
        message.setData(template);
        message.setOffline(true);
        message.setOfflineExpireTime(1000 * 60 * 60 * 24);
        Target target = new Target();
        target.setAppId(appId);
        if (type == 0)
            target.setClientId(str);
        if (type == 1)
            target.setAlias(str);
        IPushResult ret = null;
        try {
            ret = push.pushMessageToSingle(message, target);
        } catch (RequestException e) {
            e.printStackTrace();
            ret = push.pushMessageToSingle(message, target, e.getRequestId());
        }
        if (ret != null) {
            System.out.println(dateToStr(LocalDateTime.now(), format1) +"目标用户："+str + "的推送已发出");
        } else {
            System.out.println("服务器响应异常");
        }

    }

    public static TransmissionTemplate transmissionTemplate(String content) {
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(appId);
        template.setAppkey(appkey);
        template.setTransmissionType(2);
        template.setTransmissionContent(content);
        return template;
    }

}
