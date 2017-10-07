package com.zuoc.websocket.protocol.codec;


import com.alibaba.fastjson.JSONObject;
import com.zuoc.websocket.protocol.WebsocketEncoder;
import com.zuoc.websocket.protocol.WebsocketResponse;

import org.springframework.web.socket.TextMessage;

/**
 * Created by zuoc on 2017/3/16.
 */
public class TextMessageWebsocketEncoder implements WebsocketEncoder<TextMessage> {

    @Override
    public TextMessage encode(WebsocketResponse response) {
        return new TextMessage(JSONObject.toJSONString(response));
    }
}
