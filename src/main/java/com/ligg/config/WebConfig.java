package com.ligg.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置类
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Value("${video.upload.path}")
    private String videoUploadPath;
    
    @Value("${video.cover.path}")
    private String coverUploadPath;
    
    /**
     * 添加静态资源映射
     * @param registry 资源处理器注册表
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 视频文件映射
        registry.addResourceHandler("/videos/**")
                .addResourceLocations("file:" + videoUploadPath + "/");
        
        // 封面图片映射
        registry.addResourceHandler("/videos/cover/**")
                .addResourceLocations("file:" + coverUploadPath + "/");
    }
}