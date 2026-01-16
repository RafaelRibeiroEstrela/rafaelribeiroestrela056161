package com.example.processoseletivoapi.clients;

import com.example.processoseletivoapi.configs.UnsafeFeignSslConfig;
import com.example.processoseletivoapi.responses.RegionalResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "regionalClient", url = "https://integrador-argus-api.geia.vip/v1/regionais", configuration = UnsafeFeignSslConfig.class)
public interface RegionalClient {

    @GetMapping
    List<RegionalResponse> findAll();
}
