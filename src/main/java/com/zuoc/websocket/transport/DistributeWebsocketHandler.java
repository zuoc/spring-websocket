package com.zuoc.websocket.transport;


import com.zuoc.websocket.protocol.WebsocketDecoder;
import com.zuoc.websocket.protocol.WebsocketRequest;
import com.zuoc.websocket.protocol.codec.TextMessageWebsocketDecoder;
import com.zuoc.websocket.protocol.handshake.WebsocketHandshake;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * websocket 分发器,通过配置 {@link WebsocketChannelPipeline} 处理业务逻辑,通常继承 {@link
 * AbstractWebsocketChannelPipeline}
 * Created by zuoc on 2017/3/16.
 */
public class DistributeWebsocketHandler extends TextWebSocketHandler {

    private WebsocketDecoder<TextMessage> decoder = new TextMessageWebsocketDecoder();

    private WebsocketChannelPipeline websocketChannelPipeline;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public DistributeWebsocketHandler(WebsocketChannelPipeline websocketChannelPipeline) {
        this.websocketChannelPipeline = websocketChannelPipeline;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        final WebsocketRequest websocketRequest = decoder.decode(message);
        if (websocketRequest instanceof WebsocketHandshake) {
            websocketChannelPipeline.onOpen(session, (WebsocketHandshake) websocketRequest);
        } else {
            websocketChannelPipeline.onMessage(session, websocketRequest);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if (logger.isErrorEnabled()) {
            logger.error("websocket 传输异常 ", exception);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        websocketChannelPipeline.onClose(session, status);
    }
}
