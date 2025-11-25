package com.universidade.moedaestudantil.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.universidade.moedaestudantil.model.Semestre;

public interface SemestreRepository extends JpaRepository<Semestre, Long> {
    Optional<Semestre> findByAnoAndPeriodo(String ano, String periodo);
    Optional<Semestre> findByAtivoTrue();
}

