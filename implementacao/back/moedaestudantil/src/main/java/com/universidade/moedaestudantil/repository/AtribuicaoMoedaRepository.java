package com.universidade.moedaestudantil.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.universidade.moedaestudantil.model.AtribuicaoMoeda;
import com.universidade.moedaestudantil.model.Professor;
import com.universidade.moedaestudantil.model.Semestre;

public interface AtribuicaoMoedaRepository extends JpaRepository<AtribuicaoMoeda, Long> {
    Optional<AtribuicaoMoeda> findByProfessorAndSemestre(Professor professor, Semestre semestre);
    boolean existsByProfessorAndSemestre(Professor professor, Semestre semestre);
}

