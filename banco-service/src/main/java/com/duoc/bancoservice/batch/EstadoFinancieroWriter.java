package com.duoc.bancoservice.batch;

import com.duoc.bancoservice.model.EstadoFinanciero;
import com.duoc.bancoservice.repository.EstadoFinancieroRepository;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;

public class EstadoFinancieroWriter implements ItemWriter<EstadoFinanciero> {

    private final EstadoFinancieroRepository repository;

    public EstadoFinancieroWriter(EstadoFinancieroRepository repository) {
        this.repository = repository;
    }

    @Override
    public void write(Chunk<? extends EstadoFinanciero> chunk) {
        repository.saveAll(chunk.getItems());
    }
}