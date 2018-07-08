package com.itheima.controller;

import com.itheima.webdemo.MyServer;
import com.itheima.webdemo.WebdemoApplication;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class TestController {
    private  MyServer myServer ;

    public void init(){
        myServer=new MyServer(Integer.parseInt(WebdemoApplication.port)+1);
    }

}
