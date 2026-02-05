package com.example.processoseletivoapi.controllers;

import com.example.processoseletivoapi.mappers.AlbumMapper;
import com.example.processoseletivoapi.models.Album;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class AlbumWebSoket {

    private final SimpMessagingTemplate messagingTemplate;
    private final AlbumMapper mapper;

    public AlbumWebSoket(SimpMessagingTemplate messagingTemplate, AlbumMapper mapper) {
        this.messagingTemplate = messagingTemplate;
        this.mapper = mapper;
    }
    
    public void publish(Album model) {
        final String endpoint = "/topic/albuns/novos";
        messagingTemplate.convertAndSend(endpoint, mapper.modelToResponse(model));
    }
}
