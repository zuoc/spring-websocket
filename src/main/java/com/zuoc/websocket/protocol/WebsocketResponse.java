package com.zuoc.websocket.protocol;

import java.io.Serializable;

/**
 * websocket 私有响应协议
 * Created by zuoc on 2017/3/16.
 */
public class WebsocketResponse implements Serializable {

    private Object payload;

    public WebsocketResponse(Object payload) {
        this.payload = payload;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }
}
