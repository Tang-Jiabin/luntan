package com.example.luntan;

import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;


@Configuration
public class WebConfigurer implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");

//        registry.addResourceHandler("/upload/**").addResourceLocations("file:D:/upload/");+ResourceUtils.getURL("jar:").getPath().replace("jar:","fileupload")+ File.separator
        try {
            registry.addResourceHandler("/upload/**").addResourceLocations("file:"+ResourceUtils.getURL("jar:").getPath().replace("jar:","fileupload")+ File.separator);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
