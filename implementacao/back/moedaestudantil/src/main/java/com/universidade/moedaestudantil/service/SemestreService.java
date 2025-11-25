package com.universidade.moedaestudantil.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.universidade.moedaestudantil.model.Semestre;
import com.universidade.moedaestudantil.repository.SemestreRepository;

@Service
@Transactional
public class SemestreService {
    
    private final SemestreRepository repo;
    
    public SemestreService(SemestreRepository repo) {
        this.repo = repo;
    }
    
    public List<Semestre> findAll() {
        return repo.findAll();
    }
    
    public Optional<Semestre> findById(Long id) {
        return repo.findById(id);
    }
    
    public Optional<Semestre> findByAnoEPeriodo(String ano, String periodo) {
        return repo.findByAnoAndPeriodo(ano, periodo);
    }
    
    public Optional<Semestre> findSemestreAtivo() {
        return repo.findByAtivoTrue();
    }
    
    public Semestre save(Semestre semestre) {
        return repo.save(semestre);
    }
    
    /**
     * Cria ou retorna o semestre atual baseado na data.
     * Se não existir, cria um novo semestre ativo.
     */
    public Semestre obterOuCriarSemestreAtual() {
        int anoAtual = LocalDateTime.now().getYear();
        int mesAtual = LocalDateTime.now().getMonthValue();
        
        // Primeiro semestre: janeiro a junho (1-6)
        // Segundo semestre: julho a dezembro (7-12)
        String periodo = (mesAtual <= 6) ? "1" : "2";
        
        Optional<Semestre> semestreExistente = repo.findByAnoAndPeriodo(String.valueOf(anoAtual), periodo);
        
        if (semestreExistente.isPresent()) {
            return semestreExistente.get();
        }
        
        // Criar novo semestre
        Semestre novoSemestre = new Semestre();
        novoSemestre.setAno(String.valueOf(anoAtual));
        novoSemestre.setPeriodo(periodo);
        novoSemestre.setDataInicio(LocalDateTime.now());
        novoSemestre.setAtivo(true);
        
        // Desativar semestres anteriores
        repo.findByAtivoTrue().ifPresent(s -> {
            s.setAtivo(false);
            s.setDataFim(LocalDateTime.now());
            repo.save(s);
        });
        
        return repo.save(novoSemestre);
    }
}

