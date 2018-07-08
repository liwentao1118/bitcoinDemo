package com.itheima.webdemo;

import java.net.URI;
import java.net.URISyntaxException;

public class Test {
    public static void main(String[] args) throws Exception {
        MyServer myServer = new MyServer(7000);

        myServer.startServer();
        URI uri = new URI("ws://localhost:7000");
        MyClient myClient = new MyClient(uri,"client1");
        MyClient myClient2 = new MyClient(uri,"client2");

        myClient.connect();
        myClient2.connect();

        Thread.sleep(1000);

        myServer.broadcast("发送给客户端的消息");
        myClient.send("这是客户端1发过来的消息");


    }
}
