package com.duoc.bancoservice.batch;

import com.duoc.bancoservice.model.Interes;
import com.duoc.bancoservice.model.EstadoFinanciero;
import com.duoc.bancoservice.repository.InteresRepository;
import com.duoc.bancoservice.repository.EstadoFinancieroRepository;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {

    @Bean
    public Job importarInteresesJob(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            InteresRepository repository) throws Exception {

        InteresReader reader = new InteresReader();
        InteresProcessor processor = new InteresProcessor();
        InteresWriter writer = new InteresWriter(repository);

        Step semana1 = crearStepInteres(
                "importarInteresesSemana1", 1,
                jobRepository, transactionManager, reader, processor, writer);

        Step semana2 = crearStepInteres(
                "importarInteresesSemana2", 2,
                jobRepository, transactionManager, reader, processor, writer);

        Step semana3 = crearStepInteres(
                "importarInteresesSemana3", 3,
                jobRepository, transactionManager, reader, processor, writer);

        return new JobBuilder("importarInteresesJob", jobRepository)
                .start(semana1)
                .next(semana2)
                .next(semana3)
                .build();
    }

    private Step crearStepInteres(
            String nombre,
            int semana,
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            InteresReader reader,
            InteresProcessor processor,
            InteresWriter writer) throws Exception {

        return new StepBuilder(nombre, jobRepository)
                .<Interes, Interes>chunk(10, transactionManager)
                .reader(reader.crearReader(semana))
                .processor(processor)
                .writer(writer)
                .faultTolerant()
                .skip(IllegalArgumentException.class)
                .skipLimit(10000)
                .build();
    }

    @Bean
    public Job importarEstadosAnualesJob(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            EstadoFinancieroRepository repository) throws Exception {

        EstadoFinancieroReader reader = new EstadoFinancieroReader();
        EstadoFinancieroProcessor processor = new EstadoFinancieroProcessor();
        EstadoFinancieroWriter writer =
                new EstadoFinancieroWriter(repository);

        Step semana1 = crearStepAnual(
                "importarEstadosAnualesSemana1", 1,
                jobRepository, transactionManager, reader, processor, writer);

        Step semana2 = crearStepAnual(
                "importarEstadosAnualesSemana2", 2,
                jobRepository, transactionManager, reader, processor, writer);

        Step semana3 = crearStepAnual(
                "importarEstadosAnualesSemana3", 3,
                jobRepository, transactionManager, reader, processor, writer);

        return new JobBuilder("importarEstadosAnualesJob", jobRepository)
                .start(semana1)
                .next(semana2)
                .next(semana3)
                .build();
    }

    private Step crearStepAnual(
            String nombre,
            int semana,
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            EstadoFinancieroReader reader,
            EstadoFinancieroProcessor processor,
            EstadoFinancieroWriter writer) throws Exception {

        return new StepBuilder(nombre, jobRepository)
                .<EstadoFinanciero, EstadoFinanciero>chunk(10, transactionManager)
                .reader(reader.crearReader(semana))
                .processor(processor)
                .writer(writer)
                .faultTolerant()
                .skip(IllegalArgumentException.class)
                .skipLimit(10000)
                .build();
    }
}
