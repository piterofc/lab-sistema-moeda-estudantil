package com.universidade.moedaestudantil.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.universidade.moedaestudantil.model.Aluno;
import com.universidade.moedaestudantil.repository.AlunoRepository;

@Service
@Transactional
public class AlunoService {

    private final AlunoRepository repo;

    public AlunoService(AlunoRepository repo) {
        this.repo = repo;
    }

    public List<Aluno> findAll() { return repo.findAll(); }
    public Optional<Aluno> findById(Long id) { return repo.findById(id); }
    public Aluno save(Aluno a) { return repo.save(a); }
    public void deleteById(Long id) { repo.deleteById(id); }
}