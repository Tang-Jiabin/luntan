package com.example.luntan.common;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Configuration
@ConfigurationProperties(prefix = "wechat")
public class WechatConfig implements InitializingBean {

    private String appid;

    private String secret;

    public static String APP_ID;

    public static String SECRET;

    @Override
    public void afterPropertiesSet() throws Exception {
        APP_ID = appid;
        SECRET = secret;
    }
}
