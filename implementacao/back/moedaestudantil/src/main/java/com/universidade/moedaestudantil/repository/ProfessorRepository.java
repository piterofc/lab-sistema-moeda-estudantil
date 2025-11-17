package com.universidade.moedaestudantil.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.universidade.moedaestudantil.model.Professor;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {
}