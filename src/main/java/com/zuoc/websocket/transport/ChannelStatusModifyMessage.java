package com.zuoc.websocket.transport;

import java.util.Date;

/**
 * 管道状态变更消息
 * Created by zuoc on 2017/3/17.
 */
public class ChannelStatusModifyMessage {

    private String name;

    private Status status;

    private Date time;

    public ChannelStatusModifyMessage(String name, Status status) {
        this.name = name;
        this.status = status;
        this.time = new Date();
    }

    public String getName() {
        return name;
    }

    public Status getStatus() {
        return status;
    }

    public Date getTime() {
        return time;
    }

    public enum Status {
        JOIN, EXIT
    }
}
