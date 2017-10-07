package com.zuoc.websocket.protocol.handshake;

import com.zuoc.websocket.protocol.WebsocketRequest;

/**
 * websocket 私有握手协议
 * Created by zuoc on 2017/3/16.
 */
public class WebsocketHandshake extends WebsocketRequest {

    private String token;

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
