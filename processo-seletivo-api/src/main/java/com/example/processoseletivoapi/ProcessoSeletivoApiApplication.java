package com.example.processoseletivoapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

@SpringBootApplication
@EnableFeignClients
@EnableWebSecurity
@EnableWebSocketMessageBroker
@EnableAsync
public class ProcessoSeletivoApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProcessoSeletivoApiApplication.class, args);
    }

}
