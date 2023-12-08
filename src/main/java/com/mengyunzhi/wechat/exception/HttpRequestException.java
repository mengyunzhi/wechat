package com.mengyunzhi.wechat.exception;

/**
 * HTTP请求异常
 */
public class HttpRequestException extends RuntimeException {
    public HttpRequestException(String message) {
        super(message);
    }
}
