package com.zuoc.websocket.transport;

import org.springframework.web.socket.CloseStatus;

/**
 * websocket 通信管道,管理底层通信
 * Created by zuoc on 2017/3/16.
 */
public interface WebsocketChannel {

    /**
     * 管道名称
     */
    String name();

    /**
     * 打开管道
     */
    void open();

    /**
     * 管道是否打开
     */
    boolean isOpen();


    /**
     * 关闭管道
     */
    void close(CloseStatus closeStatus);

    /**
     * 发送数据
     */
    void send(Object message);

    /**
     * 管道上下文
     */
    WebsocketChannelContext context();

}
