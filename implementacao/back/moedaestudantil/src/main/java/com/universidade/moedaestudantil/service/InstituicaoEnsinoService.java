package com.universidade.moedaestudantil.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.universidade.moedaestudantil.model.InstituicaoEnsino;
import com.universidade.moedaestudantil.repository.InstituicaoEnsinoRepository;

@Service
@Transactional
public class InstituicaoEnsinoService {

    private final InstituicaoEnsinoRepository repo;

    public InstituicaoEnsinoService(InstituicaoEnsinoRepository repo) { this.repo = repo; }

    public List<InstituicaoEnsino> findAll() { return repo.findAll(); }
    public Optional<InstituicaoEnsino> findById(Long id) { return repo.findById(id); }
    public InstituicaoEnsino save(InstituicaoEnsino i) { return repo.save(i); }
    public void deleteById(Long id) { repo.deleteById(id); }
}