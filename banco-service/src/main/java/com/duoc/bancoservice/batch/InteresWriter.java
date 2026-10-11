package com.duoc.bancoservice.batch;

import com.duoc.bancoservice.model.Interes;
import com.duoc.bancoservice.repository.InteresRepository;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;

public class InteresWriter implements ItemWriter<Interes> {

    private final InteresRepository repository;

    public InteresWriter(InteresRepository repository) {
        this.repository = repository;
    }

    @Override
    public void write(Chunk<? extends Interes> chunk) {
        repository.saveAll(chunk.getItems());
    }
}