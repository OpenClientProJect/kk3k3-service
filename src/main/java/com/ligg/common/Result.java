package com.ligg.common;

import java.io.Serializable;

/**
 * 统一响应结果
 */
public class Result implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 消息
     */
    private String message;

    /**
     * 数据
     */
    private Object data;

    /**
     * 成功结果
     * @return Result
     */
    public static Result success() {
        return success(null);
    }

    /**
     * 成功结果
     * @param data 数据
     * @return Result
     */
    public static Result success(Object data) {
        return new Result(200, "success", data);
    }

    /**
     * 错误结果
     * @param message 错误消息
     * @return Result
     */
    public static Result error(String message) {
        return new Result(500, message, null);
    }

    /**
     * 错误结果
     * @param code 错误码
     * @param message 错误消息
     * @return Result
     */
    public static Result error(Integer code, String message) {
        return new Result(code, message, null);
    }

    /**
     * 构造方法
     * @param code 状态码
     * @param message 消息
     * @param data 数据
     */
    public Result(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
} 