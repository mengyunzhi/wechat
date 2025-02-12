package com.mengyunzhi.wechat;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WechatProxyTest {

    @Test
    void getTmpQrCode() {
        // 团队微信代理服务器请求地址及请求前缀
        String wechatProxyRequestUri = "http://localhost:8081/request";
        // 用户扫码后的回调地址
        String callbackHost = "http://localhost:8080/wechat";
        String instanceName = "当前服务实例名称";
        // 对应的微信公众号appid，需要在团队微信代码服务器中进行配置
        String appid = "wx53bf06122618f768";
        // 用户扫码后，微信服务器回调的场景值，每个图片应该唯一
        String scene = "1234";
        WechatProxy wechatProxy = new WechatProxy(wechatProxyRequestUri, callbackHost, instanceName, appid);
        String imageUrl = wechatProxy.getTmpQrCode(scene, 6000);
//        String imageUrl = wechatProxy.getTmpQrCode(scene, "callbackPath", 6000);
        System.out.println("微信扫码图片地址为：" + imageUrl);
    }
}
