package com.itheima.handbyhand;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import javax.sound.sampled.Port;
import java.util.Scanner;

@SpringBootApplication
public class HandbyhandApplication {
    public static String port ;
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        port= scanner.nextLine();
        new SpringApplicationBuilder(HandbyhandApplication.class).properties("server.port="+port).run(args);
    }
}
