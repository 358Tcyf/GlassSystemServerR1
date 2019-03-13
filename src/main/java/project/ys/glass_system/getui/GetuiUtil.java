package project.ys.glass_system.getui;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.ITemplate;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.TransmissionTemplate;

import java.util.ArrayList;
import java.util.List;

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
        message.setOfflineExpireTime(1000 * 60*60*24);
        IPushResult ret = push.pushMessageToApp(message);
        System.out.println(ret.getResponse().toString());

    }

    public static TransmissionTemplate transmissionTemplateDemo() {
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(appId);
        template.setAppkey(appkey);
        template.setTransmissionType(2);
        template.setTransmissionContent("请输入需要透传的内容");
        return template;
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
