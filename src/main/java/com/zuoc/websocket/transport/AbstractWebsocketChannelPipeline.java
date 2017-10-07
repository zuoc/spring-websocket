package com.zuoc.websocket.transport;


import com.google.common.util.concurrent.ThreadFactoryBuilder;

import com.zuoc.websocket.protocol.handshake.WebsocketHandshake;
import com.zuoc.websocket.protocol.WebsocketRequest;
import com.zuoc.websocket.protocol.handshake.WebsocketHandshakeFailureException;
import com.zuoc.websocket.protocol.handshake.WebsocketHandshakeHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zuoc on 2017/3/16.
 */
public abstract class AbstractWebsocketChannelPipeline<CHANNEL extends WebsocketChannel> implements WebsocketChannelPipeline {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private WebsocketHandshakeHandler handshakeHandler;

    private final WebsocketChannelGroup<CHANNEL> channelGroup = new WebsocketChannelGroup<>();

    private ExecutorService executorService = Executors.newCachedThreadPool(
            new ThreadFactoryBuilder()
                    .setNameFormat("websocket-worker-threads-%d")
                    .setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                        @Override
                        public void uncaughtException(Thread t, Throwable e) {
                            if (logger.isErrorEnabled()) {
                                logger.error("[{}] 处理 websocket 业务异常", t.getName(), e);
                            }
                        }
                    })
                    .build());

    public AbstractWebsocketChannelPipeline(WebsocketHandshakeHandler handshakeHandler) {
        this.handshakeHandler = handshakeHandler;
    }

    @Override
    public void onOpen(WebSocketSession webSocketSession, WebsocketHandshake handshake) {
        final Map<String, Object> handshakAttributes = new HashMap<>();
        try {
            handshakAttributes.putAll(handshakeHandler.doHandshake(handshake));
        } catch (WebsocketHandshakeFailureException e) {
            if (logger.isErrorEnabled()) {
                logger.error("websocket 握手验证失败");
            }

            onClose(webSocketSession, CloseStatus.PROTOCOL_ERROR);
            return;
        }

        webSocketSession.getAttributes().putAll(handshakAttributes);

        final CHANNEL websocketChannel = newChannel(webSocketSession);
        websocketChannel.open();

        if (channelGroup.isJoin(websocketChannel.name())) {
            channelGroup.replace(websocketChannel);
        } else {
            channelGroup.join(websocketChannel);
        }

        try {
            onOpen0(websocketChannel);
        } finally {
            if (!websocketChannel.isOpen()) {
                channelGroup.remove(websocketChannel.name());
            }
        }


    }

    protected abstract void onOpen0(CHANNEL websocketChannel);

    protected abstract CHANNEL newChannel(WebSocketSession webSocketSession);

    @Override
    public void onClose(WebSocketSession webSocketSession, CloseStatus closeStatus) {
        final String channelName = webSocketSession.getId();
        if (channelGroup.isJoin(channelName)) {
            final CHANNEL channel = channelGroup.getChannel(channelName);
            channelGroup.close(closeStatus, channel.name());
            onClose0(channel, closeStatus);
        } else {
            if (webSocketSession.isOpen()) {
                try {
                    webSocketSession.close(closeStatus);
                } catch (IOException e) {
                    logger.error("未知 channel {} 关闭异常", channelName, e);
                }
            }

        }
    }

    protected abstract void onClose0(CHANNEL websocketChannel, CloseStatus closeStatus);

    @Override
    public void onMessage(WebSocketSession webSocketSession, final WebsocketRequest websocketRequest) {
        final String channelName = webSocketSession.getId();
        if (!channelGroup.isJoin(channelName)) {
            onClose(webSocketSession, CloseStatus.NOT_ACCEPTABLE);
            return;
        }
        final CHANNEL channel = channelGroup.getChannel(channelName);
        if (channel != null && channel.isOpen()) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        onMessage0(channel, websocketRequest);
                    } finally {
                        if (!channel.isOpen()) {
                            channelGroup.remove(channelName);
                        }
                    }
                }
            });
        } else {
            channelGroup.remove(channelName);
        }
    }

    protected abstract void onMessage0(CHANNEL websocketChannel, WebsocketRequest websocketRequest);

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }
}
