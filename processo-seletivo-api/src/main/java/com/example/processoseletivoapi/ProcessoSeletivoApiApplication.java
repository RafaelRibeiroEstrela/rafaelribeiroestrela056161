package com.example.processoseletivoapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ProcessoSeletivoApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProcessoSeletivoApiApplication.class, args);
    }

}
