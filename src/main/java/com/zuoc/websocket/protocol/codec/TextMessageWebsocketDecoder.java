package com.zuoc.websocket.protocol.codec;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zuoc.websocket.protocol.WebsocketDecoder;
import com.zuoc.websocket.protocol.handshake.WebsocketHandshake;
import com.zuoc.websocket.protocol.WebsocketRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.socket.TextMessage;

/**
 * Created by zuoc on 2017/3/16.
 */
public class TextMessageWebsocketDecoder implements WebsocketDecoder<TextMessage> {

    private final String TOKEN = "token";

    @Override
    public WebsocketRequest decode(TextMessage textMessage) {
        JSONObject jsonObject = JSON.parseObject(textMessage.getPayload());
        if (StringUtils.isNotBlank(jsonObject.getString(TOKEN))) {
            return JSONObject.parseObject(textMessage.getPayload(), WebsocketHandshake.class);
        } else {
            return JSONObject.parseObject(textMessage.getPayload(), WebsocketRequest.class);
        }
    }
}
