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

    public synchronized void sync() {
        List<Regional> regionaisApi = client.findAll().stream().map(mapper::responseToModel).toList();
        List<Regional> regionaisDb = repository.findAll();
        if (regionaisDb.isEmpty()) {
            repository.saveAll(regionaisApi);
        } else {
            salvarAtualizar(regionaisApi, regionaisDb);
            inativarAusentes(regionaisApi, regionaisDb);
        }
    }

    public List<Regional> findAll() {
        return repository.findAll();
    }

    private void salvarAtualizar(List<Regional> regionaisApi, List<Regional> regionaisDb) {
        List<Regional> list = new ArrayList<>();
        for (Regional regional : regionaisApi) {
            Optional<Regional> optional = regionaisDb.stream().filter(obj -> obj.getRegionalId().equals(regional.getRegionalId())).findFirst();
            if (optional.isEmpty()) {
                regional.setAtivo(true);
                list.add(regional);
            } else {
                Regional model = optional.get();
                if (!regional.getNome().trim().equals(model.getNome().trim())) {
                    model.setAtivo(false);
                    regional.setAtivo(true);
                    list.add(model);
                    list.add(regional);
                }
            }
        }
        repository.saveAll(list);
    }

    private void inativarAusentes(List<Regional> regionaisApi, List<Regional> regionaisDb) {
        regionaisDb.forEach(obj -> {
            if (!regionaisApi.contains(obj)) {
                obj.setAtivo(false);
                repository.save(obj);
            }
        });
    }
}
