package com.duoc.bancoservice.batch;

import com.duoc.bancoservice.model.EstadoFinanciero;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.batch.infrastructure.item.support.IteratorItemReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class EstadoFinancieroReader {

    public ItemReader<EstadoFinanciero> crearReader(int semana)
            throws IOException {

        if (semana < 1 || semana > 3) {
            throw new IllegalArgumentException("Semana invalida");
        }

        List<EstadoFinanciero> registros = new ArrayList<>();

        Path archivo = Path.of("../fin_legacy_data/data")
                .resolve("semana_" + semana)
                .resolve("estados_financieros_anuales.csv");

        try (BufferedReader reader = Files.newBufferedReader(
                archivo, StandardCharsets.UTF_8)) {

            String linea;
            reader.readLine();

            while ((linea = reader.readLine()) != null) {

                String[] datos = linea.split(",", -1);

                if (datos.length != 5) {
                    continue;
                }

                EstadoFinanciero estado = new EstadoFinanciero();

                try {
                    estado.setCuentaId(Long.parseLong(datos[0].trim()));
                    estado.setMonto(Double.parseDouble(datos[3].trim()));
                } catch (NumberFormatException e) {
                    estado.setCuentaId(null);
                    estado.setMonto(null);
                }

                estado.setFecha(datos[1].trim());
                estado.setTransaccion(datos[2].trim().toLowerCase());
                estado.setDescripcion(datos[4].trim());

                registros.add(estado);
            }
        }

        return new IteratorItemReader<>(registros);
    }
}