package com.universidade.moedaestudantil.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.universidade.moedaestudantil.model.Transacao;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
}