package com.zuoc.websocket.protocol;

import java.io.Serializable;

/**
 * websocket 私有请求协议
 * Created by zuoc on 2017/3/16.
 */
public class WebsocketRequest implements Serializable {

    private Object payload;

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }
}
