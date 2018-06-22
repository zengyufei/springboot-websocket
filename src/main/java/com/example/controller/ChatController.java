package com.example.controller;

import com.example.model.ConnectionType;
import com.example.model.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * Created by user on 21/5/16.
 */
@Controller
public class ChatController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatController.class);

    @MessageMapping(ConnectionType.chat_connection_uri) // 访问链接 =  ConnectionType.connection_uri_prefix + ConnectionType.chat_connection_uri
    @SendTo("/topic/chatOut")
    public TextMessage echo(TextMessage msg) {
        LOGGER.info(msg.toString());
        return msg;
    }
}
