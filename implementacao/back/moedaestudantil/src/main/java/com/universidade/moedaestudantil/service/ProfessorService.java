package com.universidade.moedaestudantil.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.universidade.moedaestudantil.model.Professor;
import com.universidade.moedaestudantil.repository.ProfessorRepository;

@Service
@Transactional
public class ProfessorService {

    private final ProfessorRepository repo;

    public ProfessorService(ProfessorRepository repo) { this.repo = repo; }

    public List<Professor> findAll() { return repo.findAll(); }
    public Optional<Professor> findById(Long id) { return repo.findById(id); }
    public Professor save(Professor p) { return repo.save(p); }
    public void deleteById(Long id) { repo.deleteById(id); }
}