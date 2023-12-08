package com.mengyunzhi.wechat.vo;

/**
 * 文本响应
 */
public class TextResponse {
    private String type = "text";
    private String content;

    public TextResponse(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
