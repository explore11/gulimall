package com.song.gulimall.gulimallthirdparty.utils;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.teaopenapi.models.Config;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/* *
 * @program: gulimall
 * @description
 * @author: swq
 * @create: 2021-03-25 14:57
 **/
@ConfigurationProperties("gulimall.sms")
@Component
@Data
public class SmsComponent {
    private String smsAccessKeyId;
    private String smsAccessKeySecret;
    private String smsEndpoint;
    private String smsSignName;
    private String smsTemplateCode;

    // 创建客户端
    public static Client createSmsClient(String accessKeyId, String accessKeySecret, String endpoint) throws Exception {
        Config config = new Config()
                // 您的AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 您的AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = endpoint;
        return new Client(config);
    }

    /* *
     * 发送验证码
     * @throws Exception
     */
    public void sendCode(String phoneNumbers, String code) throws Exception {
        Client client = SmsComponent.createSmsClient(smsAccessKeyId, smsAccessKeySecret, smsEndpoint);
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setPhoneNumbers(phoneNumbers)
                .setSignName(smsSignName)
                .setTemplateCode(smsTemplateCode)
                .setTemplateParam("{\"code\":\"" + code + "\"}");
        // 复制代码运行请自行打印 API 的返回值
        client.sendSms(sendSmsRequest);
    }
}
