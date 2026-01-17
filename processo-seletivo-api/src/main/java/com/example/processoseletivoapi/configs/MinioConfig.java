package com.example.processoseletivoapi.configs;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    @Value("${minio.url}")
    private String url;
    @Value("${minio.username}")
    private String username;
    @Value("${minio.password}")
    private String password;

    @Bean
    public MinioClient getMinioClient() {
        return MinioClient.builder()
                .endpoint(url)
                .credentials(username, password)
                .build();
    }
}
