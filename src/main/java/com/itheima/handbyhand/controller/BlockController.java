package com.itheima.handbyhand.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.handbyhand.HandbyhandApplication;
import com.itheima.handbyhand.Server.MyClient;
import com.itheima.handbyhand.Server.MyServer;
import com.itheima.handbyhand.bean.Block;
import com.itheima.handbyhand.bean.MessageBean;
import com.itheima.handbyhand.bean.NoteBook;
import com.itheima.handbyhand.bean.Transaction;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;

@RestController
public class BlockController {
    private NoteBook noteBook = NoteBook.getInstance();
    private MyServer server;

    @PostMapping("/addGenesis")
    public String addGenesis(String genesis){
        try {
            noteBook.addGenesis(genesis);
            return "添加封面成功";
        } catch (Exception e) {
            return "fail"+e.getMessage();
        }
    }
    @PostMapping("/addNote")
    public String addNote(Transaction transaction){
        try {
            if (transaction.verify()){
                ObjectMapper objectMapper = new ObjectMapper();
                String note = objectMapper.writeValueAsString(transaction);
                MessageBean bean = new MessageBean(2,note);
                String msg = objectMapper.writeValueAsString(bean);
                server.broadcast(msg);
                noteBook.addNote(note);
                return "添加记录成功";
            }else {
                throw new RuntimeException("交易数据校验失败");
            }


        } catch (Exception e) {
            return "fail"+e.getMessage();
        }
    }
    @GetMapping("/showList")
    public ArrayList<Block> showList(){
        return noteBook.showList();
    }
    @GetMapping("/check")
    public  String check(){
        String check = noteBook.check();
        if (StringUtils.isEmpty(check)){
            return "数据是安全的";
        }
        return check;
    }






    @PostConstruct
    public void init() {
        server = new MyServer(Integer.parseInt(HandbyhandApplication.port) + 1);
        server.startServer();
    }

    // 节点注册
    private HashSet<String> set = new HashSet<>();
    ArrayList<MyClient>list = new ArrayList<MyClient>();

    @PostMapping("/regist")
    public String regist(String node) {
        set.add(node);
        return "添加成功";
    }

    // 连接
    @GetMapping("/conn")
    public String conn() {
        try {
            for (String s : set) {
                URI uri = new URI("ws://localhost:" + s);
                MyClient client = new MyClient(uri, "客户端"+s);
                System.out.println(s);
                client.connect();
                list.add(client);
            }
            return "连接成功";
        } catch (URISyntaxException e) {

            return"连接失败";
        }

    }
    // 广播

    @PostMapping("/broadcast")
    public String broadcast(String msg) {
        server.broadcast(msg);
        return "广播成功";
    }

@GetMapping("/syncData")
    public String sync(){
    for (MyClient myClient : list) {
        myClient.send("请求数据");
     }
       return "发送消息成功";
    }

}
