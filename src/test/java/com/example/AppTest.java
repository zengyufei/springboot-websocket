package com.example;

import com.example.model.ConnectionType;
import com.example.model.TextMessage;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AppTest extends TestCase {

    private static final Logger log = LoggerFactory.getLogger(AppTest.class);

    //连接成功数
    public static int successNum = 0;
    //连接失败数
    public static int errorNum = 0;

    @LocalServerPort
    private int port;
    private WebSocketStompClient stompClient;

    @Before
    public void setup() {
        List<Transport> transports = new ArrayList<>();
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        WebSocketClient socketClient = new SockJsClient(transports);
        stompClient = new WebSocketStompClient(socketClient);
    }

    @Test
    public void getGreeting() throws ExecutionException, InterruptedException {
        StompSession sess = newConnect(1);

        // 跟踪发送消息返回状态，必须启用该项

        TextMessage msg = new TextMessage();
        msg.setMessage("sssdasdasdasds");
        StompSession.Receiptable recpt = sess.send(ConnectionType.chat_send_message_uri, msg);

        recpt.addReceiptTask(() -> System.out.println("js endpoint send msg success."));
        sess.disconnect();
    }

    /**
     * 创建websocket连接
     * @param i
     * @return
     */
    private StompSession newConnect(int i) throws ExecutionException, InterruptedException {
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        ThreadPoolTaskScheduler task = new ThreadPoolTaskScheduler();
        task.initialize();
        stompClient.setTaskScheduler(task);

        String url = "ws://localhost:"+String.valueOf(port).concat(ConnectionType.chat_connection_uri);
        TestConnectHandler handler = new TestConnectHandler();
        ListenableFuture<StompSession> ret = stompClient.connect(url, handler);

        StompSession sess = ret.get();
        sess.setAutoReceipt(true);
        return sess;
    }

    private static class TestConnectHandler extends StompSessionHandlerAdapter {
        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            log.info("connection success !!!");
        }

        @Override
        public void handleTransportError(StompSession session, Throwable exception) {
            System.err.println(exception.toString());
        }
    }

}
