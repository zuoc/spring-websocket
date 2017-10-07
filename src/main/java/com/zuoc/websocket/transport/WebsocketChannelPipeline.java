package com.zuoc.websocket.transport;


import com.zuoc.websocket.protocol.WebsocketRequest;
import com.zuoc.websocket.protocol.handshake.WebsocketHandshake;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

/**
 * Created by zuoc on 2017/3/16.
 */
public interface WebsocketChannelPipeline {

    void onOpen(WebSocketSession webSocketSession, WebsocketHandshake handshake);

    void onClose(WebSocketSession webSocketSession, CloseStatus closeStatus);

    void onMessage(WebSocketSession webSocketSession, WebsocketRequest websocketRequest);

}
