package com.zuoc.websocket.transport;

import org.springframework.web.socket.CloseStatus;

/**
 * channel 监听器
 * Created by zuoc on 2017/3/17.
 */
public interface WebsocketChannelListener {

    void beforeOpen(WebsocketChannel websocketChannel);

    void afterOpen(WebsocketChannel websocketChannel);

    void beforeClose(WebsocketChannel websocketChannel, CloseStatus closeStatus);

    void afterClose(WebsocketChannel websocketChannel, CloseStatus closeStatus);

    void beforeSend(WebsocketChannel websocketChannel, Object message);

    void afterSend(WebsocketChannel websocketChannel, Object message);
}
