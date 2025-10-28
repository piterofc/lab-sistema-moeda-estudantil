package com.universidade.moedaestudantil.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.universidade.moedaestudantil.model.Transacao;
import com.universidade.moedaestudantil.repository.TransacaoRepository;

@Service
@Transactional
public class TransacaoService {

    private final TransacaoRepository repo;

    public TransacaoService(TransacaoRepository repo) { this.repo = repo; }

    public List<Transacao> findAll() { return repo.findAll(); }
    public Optional<Transacao> findById(Long id) { return repo.findById(id); }
    public Transacao save(Transacao t) { return repo.save(t); }
    public void deleteById(Long id) { repo.deleteById(id); }
}