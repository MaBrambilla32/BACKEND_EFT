package com.duoc.bancoservice.repository;

import com.duoc.bancoservice.model.EstadoFinanciero;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstadoFinancieroRepository
        extends JpaRepository<EstadoFinanciero, Long> {
}