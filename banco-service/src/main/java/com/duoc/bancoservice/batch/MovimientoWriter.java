
package com.duoc.bancoservice.batch;

import com.duoc.bancoservice.model.Transaccion;
import com.duoc.bancoservice.repository.TransaccionRepository;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;

public class MovimientoWriter implements ItemWriter<Transaccion> {

    private final TransaccionRepository repository;

    public MovimientoWriter(TransaccionRepository repository) {
        this.repository = repository;
    }

    @Override
    public void write(Chunk<? extends Transaccion> chunk) {
        repository.saveAll(chunk.getItems());
    }
}
