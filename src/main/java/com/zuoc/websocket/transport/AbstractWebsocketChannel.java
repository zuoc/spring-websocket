package com.zuoc.websocket.transport;


import com.zuoc.websocket.protocol.WebsocketEncoder;
import com.zuoc.websocket.protocol.WebsocketResponse;
import com.zuoc.websocket.protocol.codec.TextMessageWebsocketEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

/**
 * Created by zuoc on 2017/3/16.
 */
public abstract class AbstractWebsocketChannel implements WebsocketChannel {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private WebSocketSession webSocketSession;

    private String channelName;

    private final WebsocketChannelContext websocketChannelContext = new WebsocketChannelContext();

    private final WebsocketEncoder<TextMessage> encoder = new TextMessageWebsocketEncoder();

    private WebsocketChannelListener channelListener = new WebsocketChannelListenerAdapter();

    public AbstractWebsocketChannel(WebSocketSession webSocketSession) {
        this.webSocketSession = webSocketSession;
        this.channelName = webSocketSession.getId();
    }

    public void setListener(final WebsocketChannelListener channelListener) {
        this.channelListener = channelListener;
    }

    @Override
    public final String name() {
        return channelName;
    }

    @Override
    public final void open() {
        channelListener.beforeOpen(this);

        websocketChannelContext.setAll(webSocketSession.getAttributes());

        channelListener.afterOpen(this);
    }

    @Override
    public final boolean isOpen() {
        return webSocketSession != null && webSocketSession.isOpen();
    }

    @Override
    public final void close(CloseStatus closeStatus) {
        if (isOpen()) {
            try {
                channelListener.beforeClose(this, closeStatus);

                webSocketSession.close(closeStatus);

                channelListener.afterClose(this, closeStatus);
            } catch (IOException e) {
                logger.error("channel 连接关闭异常 {}", channelName, e);
                return;
            }
        }
    }

    @Override
    public final void send(Object message) {
        if (!isOpen()) {
            logger.error("channel {} 已经关闭,无法发送消息", channelName);
            return;
        }
        try {
            channelListener.beforeSend(this, message);

            webSocketSession.sendMessage(encoder.encode(new WebsocketResponse(message)));

            channelListener.afterSend(this, message);
        } catch (IOException e) {
            logger.error("发送消息失败 channel : {}, message : {}", channelName, message);
        }
    }

    @Override
    public final WebsocketChannelContext context() {
        return websocketChannelContext;
    }
}
