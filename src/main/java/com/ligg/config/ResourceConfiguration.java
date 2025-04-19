package com.ligg.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ResourceConfiguration implements WebMvcConfigurer {

    @Value("${upload.path}")
    private String uploadPath;

    @Value("${video.upload.path}")
    private String videoUploadPath;

    @Value("${video.cover.path}")
    private String coverUploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 视频文件资源映射
        registry.addResourceHandler("/videos/**")
                .addResourceLocations("file:" + videoUploadPath + "/");

        // 视频封面资源映射
        registry.addResourceHandler("/videos/cover/**")
                .addResourceLocations("file:" + coverUploadPath + "/");

        // 用户上传的图片资源映射
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + uploadPath + "/");
    }
} 