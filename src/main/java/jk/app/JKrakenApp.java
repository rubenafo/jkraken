package jk.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages="jk.api, jk.wsocket")
public class JKrakenApp {

    public static void main(String[] args) {
        SpringApplication.run(JKrakenApp.class, args);
    }
}