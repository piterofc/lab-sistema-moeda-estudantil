package com.universidade.moedaestudantil.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.universidade.moedaestudantil.model.Aluno;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {
}