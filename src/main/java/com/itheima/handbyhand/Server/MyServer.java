package com.itheima.handbyhand.Server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.handbyhand.bean.Block;
import com.itheima.handbyhand.bean.MessageBean;
import com.itheima.handbyhand.bean.NoteBook;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.ArrayList;

public class MyServer extends WebSocketServer {
    private  int port;
    public MyServer(int port) {
        super(new InetSocketAddress(port));
        this.port = port;
    }
    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {

        System.out.println("webSocket服务器_"+port+"_打开了连接");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("webSocket服务器_"+port+"_关闭了连接");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        try {
            if ("请求数据".equals(message)){
                NoteBook noteBook = NoteBook.getInstance();
                ArrayList<Block> blocks = noteBook.showList();
                ObjectMapper objectMapper = new ObjectMapper();
                String s = objectMapper.writeValueAsString(blocks);
                MessageBean bean = new MessageBean(1,s);
                String msg = objectMapper.writeValueAsString(bean);
                broadcast(msg);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.out.println("webSocket服务器_"+port+"_发生了错误");
    }

    @Override
    public void onStart() {
        System.out.println("webSocket服务器_"+port+"_启动成功");
    }
    public void startServer() {
        new Thread(this).start();
    }
}
