
package com.duoc.bancoservice.batch;

import com.duoc.bancoservice.model.Transaccion;
import org.springframework.batch.infrastructure.item.ItemProcessor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;

public class MovimientoProcessor implements ItemProcessor<Transaccion, Transaccion> {

    private static final List<DateTimeFormatter> FORMATOS = List.of(
            DateTimeFormatter.ISO_LOCAL_DATE,
            DateTimeFormatter.ofPattern("yyyy/MM/dd"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy")
    );

    @Override
    public Transaccion process(Transaccion transaccion) {

        if (transaccion.getFecha() == null || transaccion.getFecha().isBlank()) {
            throw new IllegalArgumentException("fecha obligatoria");
        }

        if (transaccion.getMonto() == null || transaccion.getMonto() == 0) {
            throw new IllegalArgumentException("monto invalido");
        }

        if (transaccion.getTipo() == null || transaccion.getTipo().isBlank()) {
            throw new IllegalArgumentException("tipo obligatorio");
        }

        String tipo = transaccion.getTipo().trim().toLowerCase(Locale.ROOT);

        if (!tipo.equals("debito") && !tipo.equals("credito")) {
            throw new IllegalArgumentException("tipo de movimiento invalido");
        }

        if (!fechaValida(transaccion.getFecha().trim())) {
            throw new IllegalArgumentException("fecha invalida");
        }

        transaccion.setFecha(transaccion.getFecha().trim());
        transaccion.setTipo(tipo);
        transaccion.setEstado("PROCESADA");

        return transaccion;
    }

    private boolean fechaValida(String fecha) {

        for (DateTimeFormatter formato : FORMATOS) {
            try {
                LocalDate.parse(fecha, formato);
                return true;
            } catch (DateTimeParseException ignored) {
            }
        }

        return false;
    }
}
