package com.zuoc.websocket.protocol.handshake;

import java.util.Map;

/**
 * 私有握手协议处理器
 * Created by zuoc on 2017/10/7.
 */
public interface WebsocketHandshakeHandler {

    Map<String, Object> doHandshake(WebsocketHandshake websocketHandshake) throws WebsocketHandshakeFailureException;

}
