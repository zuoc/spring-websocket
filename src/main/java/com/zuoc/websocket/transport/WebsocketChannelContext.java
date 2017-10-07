package com.zuoc.websocket.transport;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * channel 上下文,保存一些上下文信息
 * Created by zuoc on 2017/3/17.
 */
public class WebsocketChannelContext {

    private final ConcurrentMap<String, Object> context = new ConcurrentHashMap<>();

    public void set(final String key, final Object value) {
        context.put(key, value);
    }

    public void setAll(final Map<String, Object> attrs) {
        context.putAll(attrs);
    }

    public Object get(final String key) {
        return context.get(key);
    }

    public void remove(final String key) {
        context.remove(key);
    }

    public boolean contains(final String key) {
        return context.containsKey(key);
    }


}
