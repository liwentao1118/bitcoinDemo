package com.itheima.handbyhand.Server;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.handbyhand.bean.Block;
import com.itheima.handbyhand.bean.MessageBean;
import com.itheima.handbyhand.bean.NoteBook;
import com.itheima.handbyhand.bean.Transaction;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URI;
import java.util.ArrayList;

public class MyClient extends WebSocketClient {

private String name;
    public MyClient(URI serverUri,String name) {
        super(serverUri);
        this.name = name;

    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("客户端__" + name + "__打开了连接");
    }

    @Override
    public void onMessage(String message) {

        System.out.println("客户端__" + name + "__收到了消息:" + message);
        try {
            if (!StringUtils.isEmpty(message)){
            ObjectMapper objectMapper= new ObjectMapper();
                MessageBean bean = objectMapper.readValue(message, MessageBean.class);
                NoteBook noteBook = NoteBook.getInstance();
                if (bean.getType()==1){
                    JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class,Block.class);
                    ArrayList<Block> list = objectMapper.readValue(bean.getMessage(), javaType);
                    noteBook.comparaData(list);
                }else if (bean.getType()==2){
                    Transaction transaction = objectMapper.readValue(bean.getMessage(), Transaction.class);
                    if (transaction.verify()){
                        noteBook.addNote(bean.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("客户端__" + name + "__关闭了连接");
    }

    @Override
    public void onError(Exception ex) {
        System.out.println("客户端__" + name + "__发生错误");
    }
}
