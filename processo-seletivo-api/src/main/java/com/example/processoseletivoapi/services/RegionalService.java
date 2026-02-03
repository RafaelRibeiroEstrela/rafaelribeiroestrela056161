package com.example.processoseletivoapi.services;

import com.example.processoseletivoapi.clients.RegionalClient;
import com.example.processoseletivoapi.mappers.RegionalMapper;
import com.example.processoseletivoapi.models.Regional;
import com.example.processoseletivoapi.repositories.RegionalRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RegionalService {

    private final RegionalRepository repository;
    private final RegionalClient client;
    private final RegionalMapper mapper;

    public RegionalService(RegionalRepository repository, RegionalClient client, RegionalMapper mapper) {
        this.repository = repository;
        this.client = client;
        this.mapper = mapper;
    }

    public void sync() {
        List<Regional> regionaisApi = client.findAll().stream().map(mapper::responseToModel).toList();
        List<Regional> regionaisDb = repository.findAllLocked();
        if (regionaisDb.isEmpty()) {
            regionaisApi.forEach(obj -> obj.setAtivo(true));
            repository.saveAll(regionaisApi);
        } else {
            saveUpdate(regionaisApi, regionaisDb);
            deactivateAbsent(regionaisApi, regionaisDb);
        }
    }

    public List<Regional> findAll() {
        return repository.findAll();
    }

    private void saveUpdate(List<Regional> regionaisApi, List<Regional> regionaisDb) {
        List<Regional> list = new ArrayList<>();
        for (Regional regional : regionaisApi) {
            Optional<Regional> optional = regionaisDb.stream().filter(obj -> obj.getRegionalId().equals(regional.getRegionalId())).findFirst();
            if (optional.isEmpty()) {
                regional.setAtivo(true);
                list.add(regional);
            } else {
                Regional model = optional.get();
                if (!regional.getNome().equals(model.getNome())) {
                    model.setAtivo(false);
                    regional.setAtivo(true);
                    list.add(model);
                    list.add(regional);
                }
            }
        }
        repository.saveAll(list);
    }

    private void deactivateAbsent(List<Regional> regionaisApi, List<Regional> regionaisDb) {
        regionaisDb.forEach(obj -> {
            if (!regionaisApi.contains(obj)) {
                obj.setAtivo(false);
                repository.save(obj);
            }
        });
    }
}
