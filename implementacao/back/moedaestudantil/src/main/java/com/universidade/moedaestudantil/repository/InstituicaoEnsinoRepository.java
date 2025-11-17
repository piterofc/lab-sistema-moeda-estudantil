package com.universidade.moedaestudantil.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.universidade.moedaestudantil.model.InstituicaoEnsino;

public interface InstituicaoEnsinoRepository extends JpaRepository<InstituicaoEnsino, Long> {
}