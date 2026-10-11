package com.duoc.bancoservice.batch;

import com.duoc.bancoservice.model.Transaccion;
import com.duoc.bancoservice.repository.TransaccionRepository;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.IOException;

@Configuration
public class MovimientoJobConfig {

    @Bean
    public MovimientoReader movimientoReader() {
        return new MovimientoReader();
    }

    @Bean
    public MovimientoProcessor movimientoProcessor() {
        return new MovimientoProcessor();
    }

    @Bean
    public MovimientoWriter movimientoWriter(
            TransaccionRepository repository) {
        return new MovimientoWriter(repository);
    }

    @Bean
    public TaskExecutor batchTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);
        executor.setMaxPoolSize(3);
        executor.setQueueCapacity(3);
        executor.setThreadNamePrefix("batch-semana-");
        executor.initialize();
        return executor;
    }

    @Bean
    public Step importarSemana1Step(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            MovimientoReader reader,
            MovimientoProcessor processor,
            MovimientoWriter writer) throws IOException {

        return crearStep("importarSemana1Step", 1, jobRepository,
                transactionManager, reader, processor, writer);
    }

    @Bean
    public Step importarSemana2Step(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            MovimientoReader reader,
            MovimientoProcessor processor,
            MovimientoWriter writer) throws IOException {

        return crearStep("importarSemana2Step", 2, jobRepository,
                transactionManager, reader, processor, writer);
    }

    @Bean
    public Step importarSemana3Step(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            MovimientoReader reader,
            MovimientoProcessor processor,
            MovimientoWriter writer) throws IOException {

        return crearStep("importarSemana3Step", 3, jobRepository,
                transactionManager, reader, processor, writer);
    }

    private Step crearStep(
            String nombre,
            int semana,
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            MovimientoReader reader,
            MovimientoProcessor processor,
            MovimientoWriter writer) throws IOException {

        return new StepBuilder(nombre, jobRepository)
                .<Transaccion, Transaccion>chunk(10, transactionManager)
                .reader(reader.crearReader(semana))
                .processor(processor)
                .writer(writer)
                .faultTolerant()
                .skip(IllegalArgumentException.class)
                .skipLimit(1000)
                .retry(TransientDataAccessException.class)
                .retryLimit(3)
                .build();
    }

    @Bean
    public Job importarMovimientosJob(
            JobRepository jobRepository,
            Step importarSemana1Step,
            Step importarSemana2Step,
            Step importarSemana3Step,
            TaskExecutor batchTaskExecutor) {

        SimpleFlow flujoSemana1 = new FlowBuilder<SimpleFlow>("flujoSemana1")
                .start(importarSemana1Step)
                .build();

        SimpleFlow flujoSemana2 = new FlowBuilder<SimpleFlow>("flujoSemana2")
                .start(importarSemana2Step)
                .build();

        SimpleFlow flujoSemana3 = new FlowBuilder<SimpleFlow>("flujoSemana3")
                .start(importarSemana3Step)
                .build();

        return new JobBuilder("importarMovimientosJob", jobRepository)
                .start(flujoSemana1)
                .split(batchTaskExecutor)
                .add(flujoSemana2, flujoSemana3)
                .end()
                .build();
    }
}
