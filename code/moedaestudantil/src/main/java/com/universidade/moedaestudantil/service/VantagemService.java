package com.universidade.moedaestudantil.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.universidade.moedaestudantil.model.Vantagem;
import com.universidade.moedaestudantil.repository.VantagemRepository;

@Service
@Transactional
public class VantagemService {

    private final VantagemRepository repo;

    public VantagemService(VantagemRepository repo) { this.repo = repo; }

    public List<Vantagem> findAll() { return repo.findAll(); }
    public Optional<Vantagem> findById(Long id) { return repo.findById(id); }
    public Vantagem save(Vantagem v) { return repo.save(v); }
    public void deleteById(Long id) { repo.deleteById(id); }
}