package com.jacob.springcloud.dto;


public class WebSocketResponse {
    private String content;

    public WebSocketResponse(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
