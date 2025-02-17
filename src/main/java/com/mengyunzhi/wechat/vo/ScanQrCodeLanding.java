package com.mengyunzhi.wechat.vo;

/**
 * 用户扫码时触发的首陆请求
 */
public class ScanQrCodeLanding {
    /**
     * 场景值
     */
    private String scene;

    /**
     * 扫码微信用户openid
     */
    private String openid;

    /**
     * 二维码对应的appId
     */
    private String appId;

    public ScanQrCodeLanding() {
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }


    @Override
    public String toString() {
        return "ScanQrCodeLanding{" +
                "openid='" + openid + '\'' +
                ", scene='" + scene + '\'' +
                ", appId='" + appId + '\'' +
                '}';
    }
}
