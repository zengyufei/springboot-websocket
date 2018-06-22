/**
 * Copyright (C): 恒大集团©版权所有 Evergrande Group
 * FileName: ConnectionType
 * Author:   zengyufei
 * Date:     2018/06/22
 * Description: TODO
 */
package com.example.model;

/**
 * TODO
 * @author zengyufei
 * @since 1.0.0
 */
public interface ConnectionType {

    String send_message_uri_prefix = "/app";

    String chat_connection_uri = "/chatIn";
    String chat_send_message_uri = send_message_uri_prefix.concat(chat_connection_uri);

}
