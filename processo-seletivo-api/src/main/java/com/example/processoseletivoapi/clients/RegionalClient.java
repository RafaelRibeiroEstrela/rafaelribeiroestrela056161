package com.example.processoseletivoapi.clients;

import com.example.processoseletivoapi.responses.RegionalResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(path = "https://integrador-argus-api.geia.vip/v1/regionais", contextId = "regionalClient")
public interface RegionalClient {

    @GetMapping
    List<RegionalResponse> findAll();
}
