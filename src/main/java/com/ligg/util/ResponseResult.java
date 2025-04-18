package com.ligg.util;

import lombok.Data;

/**
 * 响应结果包装类
 * @param <T> 数据类型
 */
@Data
public class ResponseResult<T> {
    
    /**
     * 是否成功
     */
    private boolean success;
    
    /**
     * 状态码
     */
    private int code;
    
    /**
     * 消息
     */
    private String message;
    
    /**
     * 数据
     */
    private T data;
    
    /**
     * 私有构造函数
     */
    private ResponseResult() {}
    
    /**
     * 成功响应
     * @param data 数据
     * @param <T> 数据类型
     * @return 响应结果
     */
    public static <T> ResponseResult<T> success(T data) {
        ResponseResult<T> result = new ResponseResult<>();
        result.setSuccess(true);
        result.setCode(200);
        result.setMessage("操作成功");
        result.setData(data);
        return result;
    }
    
    /**
     * 成功响应（无数据）
     * @param <T> 数据类型
     * @return 响应结果
     */
    public static <T> ResponseResult<T> success() {
        return success(null);
    }
    
    /**
     * 失败响应
     * @param message 错误消息
     * @param <T> 数据类型
     * @return 响应结果
     */
    public static <T> ResponseResult<T> error(String message) {
        ResponseResult<T> result = new ResponseResult<>();
        result.setSuccess(false);
        result.setCode(500);
        result.setMessage(message);
        return result;
    }
    
    /**
     * 失败响应
     * @param code 错误码
     * @param message 错误消息
     * @param <T> 数据类型
     * @return 响应结果
     */
    public static <T> ResponseResult<T> error(int code, String message) {
        ResponseResult<T> result = new ResponseResult<>();
        result.setSuccess(false);
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
} 