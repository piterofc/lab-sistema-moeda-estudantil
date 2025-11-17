package com.universidade.moedaestudantil.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.universidade.moedaestudantil.model.Aluno;
import com.universidade.moedaestudantil.model.Professor;
import com.universidade.moedaestudantil.model.Transacao;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
    List<Transacao> findByAluno(Aluno aluno);
    List<Transacao> findByProfessor(Professor professor);
}