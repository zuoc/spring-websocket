package com.zuoc.websocket.protocol;

/**
 * websocket 私有协议编码器
 * Created by zuoc on 2017/3/16.
 */
public interface WebsocketEncoder<FRAME extends Object> {

    FRAME encode(WebsocketResponse response);
}
