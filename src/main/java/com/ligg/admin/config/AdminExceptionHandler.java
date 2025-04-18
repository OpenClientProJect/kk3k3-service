package com.ligg.admin.config;

import com.ligg.common.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 管理员全局异常处理器
 */
@RestControllerAdvice(basePackages = "com.ligg.admin.controller")
public class AdminExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(AdminExceptionHandler.class);
    
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        logger.error("系统异常", e);
        return Result.error("系统异常，请联系管理员");
    }
} 