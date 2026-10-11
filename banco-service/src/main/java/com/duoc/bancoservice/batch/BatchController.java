package com.duoc.bancoservice.batch;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/batch")
public class BatchController {

    private final JobLauncher jobLauncher;
    private final Job importarMovimientosJob;
    private final Job importarInteresesJob;
    private final Job importarEstadosAnualesJob;

    public BatchController(
            JobLauncher jobLauncher,
            Job importarMovimientosJob,
            Job importarInteresesJob,
            Job importarEstadosAnualesJob) {

        this.jobLauncher = jobLauncher;
        this.importarMovimientosJob = importarMovimientosJob;
        this.importarInteresesJob = importarInteresesJob;
        this.importarEstadosAnualesJob = importarEstadosAnualesJob;
    }

    @PostMapping("/movimientos")
    public ResponseEntity<String> importarMovimientos() throws Exception {
        return ejecutarJob(importarMovimientosJob);
    }

    @PostMapping("/intereses")
    public ResponseEntity<String> importarIntereses() throws Exception {
        return ejecutarJob(importarInteresesJob);
    }

    @PostMapping("/estados-anuales")
    public ResponseEntity<String> importarEstadosAnuales() throws Exception {
        return ejecutarJob(importarEstadosAnualesJob);
    }

    private ResponseEntity<String> ejecutarJob(Job job) throws Exception {

        JobParameters parametros = new JobParametersBuilder()
                .addLong("fechaEjecucion", System.currentTimeMillis())
                .toJobParameters();

        JobExecution ejecucion = jobLauncher.run(job, parametros);

        StringBuilder resultado = new StringBuilder();

        resultado.append("Job: ")
                .append(job.getName())
                .append("\nEstado: ")
                .append(ejecucion.getStatus())
                .append("\n");

        ejecucion.getStepExecutions().forEach(step -> {
            resultado.append("\nStep: ")
                    .append(step.getStepName())
                    .append("\nLeidos: ")
                    .append(step.getReadCount())
                    .append("\nEscritos: ")
                    .append(step.getWriteCount())
                    .append("\nOmitidos: ")
                    .append(step.getSkipCount())
                    .append("\n");
        });

        return ResponseEntity.ok(resultado.toString());
    }
}
