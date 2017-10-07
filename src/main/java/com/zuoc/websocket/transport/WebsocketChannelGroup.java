package com.zuoc.websocket.transport;

import org.springframework.web.socket.CloseStatus;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * channel group
 * Created by zuoc on 2017/3/16.
 */
public class WebsocketChannelGroup<CHANNEL extends WebsocketChannel> {

    private final ConcurrentMap<String, CHANNEL> channels = new ConcurrentHashMap<>();

    public CHANNEL getChannel(final String channelName) {
        return channels.get(channelName);
    }

    public boolean isJoin(final String channelName) {
        return channels.containsKey(channelName);
    }

    public void join(final CHANNEL websocketChannel) {
        if (!isJoin(websocketChannel.name())) {
            channels.putIfAbsent(websocketChannel.name(), websocketChannel);
            // 推送状态变更
            sendAll(new ChannelStatusModifyMessage(websocketChannel.name(), ChannelStatusModifyMessage.Status.JOIN));
        }
    }

    public void remove(final String channelName) {
        if (isJoin(channelName)) {
            channels.remove(channelName);
            // 推送状态变更
            sendAll(new ChannelStatusModifyMessage(channelName, ChannelStatusModifyMessage.Status.EXIT));
        }
    }

    public void replace(CHANNEL websocketChannel) {
        if (isJoin(websocketChannel.name())) {
            remove(websocketChannel.name());
        }
        join(websocketChannel);
    }

    public Set<String> channelNames() {
        return channels.keySet();
    }

    public void send(final Object message, final String... channelNames) {
        for (final String channelName : channelNames) {
            if (isJoin(channelName)) {
                channels.get(channelName).send(message);
            }
        }
    }

    public void sendAll(final Object message) {
        send(message, channelNames().toArray(new String[0]));
    }

    public void close(final CloseStatus closeStatus, final String... channelNames) {
        for (final String channelName : channelNames) {
            if (isJoin(channelName)) {
                try {
                    channels.get(channelName).close(closeStatus);
                } finally {
                    remove(channelName);
                }
            }
        }
    }

    public void closeAll(final CloseStatus closeStatus) {
        close(closeStatus, channelNames().toArray(new String[0]));
    }

    private static class WebsocketChannelWrapper {

        private WebsocketChannel channel;

        public WebsocketChannelWrapper(WebsocketChannel channel) {
            this.channel = channel;
        }

        public WebsocketChannel get() {
            return this.channel;
        }
    }

}
