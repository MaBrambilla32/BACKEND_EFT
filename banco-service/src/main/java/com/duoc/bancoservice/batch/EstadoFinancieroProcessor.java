package com.duoc.bancoservice.batch;

import com.duoc.bancoservice.model.EstadoFinanciero;
import org.springframework.batch.infrastructure.item.ItemProcessor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class EstadoFinancieroProcessor
        implements ItemProcessor<EstadoFinanciero, EstadoFinanciero> {

    private static final List<DateTimeFormatter> FORMATOS = List.of(
            DateTimeFormatter.ISO_LOCAL_DATE,
            DateTimeFormatter.ofPattern("yyyy/MM/dd"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy")
    );

    @Override
    public EstadoFinanciero process(EstadoFinanciero estado) {

        if (estado.getCuentaId() == null || estado.getCuentaId() <= 0) {
            throw new IllegalArgumentException("Cuenta invalida");
        }

        if (estado.getMonto() == null
                || !Double.isFinite(estado.getMonto())) {
            throw new IllegalArgumentException("Monto invalido");
        }

        if (estado.getTransaccion() == null
                || estado.getTransaccion().isBlank()) {
            throw new IllegalArgumentException("Transaccion invalida");
        }

        if (estado.getDescripcion() == null
                || estado.getDescripcion().isBlank()) {
            throw new IllegalArgumentException("Descripcion invalida");
        }

        estado.setFecha(normalizarFecha(estado.getFecha()));
        estado.setTransaccion(estado.getTransaccion().trim().toLowerCase());
        estado.setDescripcion(estado.getDescripcion().trim());

        return estado;
    }

    private String normalizarFecha(String fecha) {

        if (fecha == null || fecha.isBlank()) {
            throw new IllegalArgumentException("Fecha invalida");
        }

        for (DateTimeFormatter formato : FORMATOS) {
            try {
                return LocalDate.parse(fecha.trim(), formato).toString();
            } catch (DateTimeParseException ignored) {
                // intenta con el siguiente formato
            }
        }

        throw new IllegalArgumentException("Formato de fecha invalido");
    }
}