package com.duoc.bancoservice.batch;

import com.duoc.bancoservice.model.Interes;
import org.springframework.batch.infrastructure.item.ItemProcessor;

public class InteresProcessor implements ItemProcessor<Interes, Interes> {

    @Override
    public Interes process(Interes interes) {

        if (interes.getCuentaId() == null) {
            throw new IllegalArgumentException("Cuenta invalida");
        }

        if (interes.getNombre() == null || interes.getNombre().isBlank()) {
            throw new IllegalArgumentException("Nombre invalido");
        }

        if (interes.getSaldo() == null || interes.getSaldo() < 0) {
            throw new IllegalArgumentException("Saldo invalido");
        }

        if (interes.getEdad() == null
                || interes.getEdad() < 0
                || interes.getEdad() > 99) {
            throw new IllegalArgumentException("Edad invalida");
        }

        if (interes.getTipo() == null
                || !interes.getTipo().matches("(?i)ahorro|prestamo|hipoteca")) {
            throw new IllegalArgumentException("Tipo de cuenta invalido");
        }

        interes.setNombre(interes.getNombre().trim());
        interes.setTipo(interes.getTipo().toLowerCase().trim());

        return interes;
    }
}