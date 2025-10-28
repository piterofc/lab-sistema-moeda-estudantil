package com.universidade.moedaestudantil.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.universidade.moedaestudantil.model.EmpresaParceira;
import com.universidade.moedaestudantil.repository.EmpresaParceiraRepository;

@Service
@Transactional
public class EmpresaParceiraService {

    private final EmpresaParceiraRepository repo;

    public EmpresaParceiraService(EmpresaParceiraRepository repo) { this.repo = repo; }

    public List<EmpresaParceira> findAll() { return repo.findAll(); }
    public Optional<EmpresaParceira> findById(Long id) { return repo.findById(id); }
    public EmpresaParceira save(EmpresaParceira e) { return repo.save(e); }
    public void deleteById(Long id) { repo.deleteById(id); }
}