package com.duoc.bancoservice.batch;

import com.duoc.bancoservice.model.Transaccion;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.batch.infrastructure.item.support.IteratorItemReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MovimientoReader {

    public ItemReader<Transaccion> crearReader(int semana) throws IOException {

        if (semana < 1 || semana > 3) {
            throw new IllegalArgumentException("Semana invalida");
        }

        List<Transaccion> movimientos = new ArrayList<>();

        Path archivo = Path.of("../fin_legacy_data/data")
                .resolve("semana_" + semana)
                .resolve("movimientos_financieros_diarios.csv");

        try (BufferedReader reader = Files.newBufferedReader(
                archivo, StandardCharsets.UTF_8)) {

            String linea;
            reader.readLine();

            while ((linea = reader.readLine()) != null) {

                String[] datos = linea.split(",", -1);

                if (datos.length != 4) {
                    continue;
                }

                Transaccion transaccion = new Transaccion();
                transaccion.setFecha(datos[1].trim());

                try {
                    transaccion.setMonto(Double.parseDouble(datos[2].trim()));
                } catch (NumberFormatException e) {
                    transaccion.setMonto(null);
                }

                transaccion.setTipo(datos[3].trim());
                movimientos.add(transaccion);
            }
        }

        return new IteratorItemReader<>(movimientos);
    }
}
