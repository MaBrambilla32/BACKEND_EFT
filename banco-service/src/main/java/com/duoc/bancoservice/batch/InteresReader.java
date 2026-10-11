package com.duoc.bancoservice.batch;

import com.duoc.bancoservice.model.Interes;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.batch.infrastructure.item.support.IteratorItemReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class InteresReader {

    public ItemReader<Interes> crearReader(int semana) throws IOException {

        if (semana < 1 || semana > 3) {
            throw new IllegalArgumentException("Semana invalida");
        }

        List<Interes> intereses = new ArrayList<>();

        Path archivo = Path.of("../fin_legacy_data/data")
                .resolve("semana_" + semana)
                .resolve("intereses_trimestrales.csv");

        try (BufferedReader reader = Files.newBufferedReader(
                archivo, StandardCharsets.UTF_8)) {

            String linea;
            reader.readLine();

            while ((linea = reader.readLine()) != null) {

                String[] datos = linea.split(",", -1);

                if (datos.length != 5) {
                    continue;
                }

                Interes interes = new Interes();

                try {
                    interes.setCuentaId(Long.parseLong(datos[0].trim()));
                    interes.setSaldo(Double.parseDouble(datos[2].trim()));
                    interes.setEdad(
                            datos[3].trim().isEmpty()
                                    ? null
                                    : Integer.parseInt(datos[3].trim())
                    );
                } catch (NumberFormatException e) {
                    interes.setEdad(null);
                }

                interes.setNombre(datos[1].trim());
                interes.setTipo(datos[4].trim());

                intereses.add(interes);
            }
        }

        return new IteratorItemReader<>(intereses);
    }
}