package com.itheima.webdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.Scanner;

@SpringBootApplication
public class WebdemoApplication {
    public static String port;

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        port = scanner.nextLine();
        new SpringApplicationBuilder(WebdemoApplication.class).properties("server.port="+port).run(args);
    }
}
