package com.mengyunzhi.wechat.exception;

/**
 * 二维码未找到异常
 */
public class QrCodeCanNotGetException extends RuntimeException {
    public QrCodeCanNotGetException(String message) {
        super(message);
    }
}
