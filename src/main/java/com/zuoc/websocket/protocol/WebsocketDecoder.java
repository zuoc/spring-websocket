package com.zuoc.websocket.protocol;

/**
 * websocket 私有协议解码器
 * Created by zuoc on 2017/3/16.
 */
public interface WebsocketDecoder<FRAME extends Object> {

    WebsocketRequest decode(FRAME frame);
}
