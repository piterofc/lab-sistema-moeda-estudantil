package com.universidade.moedaestudantil.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.universidade.moedaestudantil.model.EmpresaParceira;
import com.universidade.moedaestudantil.model.Vantagem;
import com.universidade.moedaestudantil.repository.EmpresaParceiraRepository;
import com.universidade.moedaestudantil.repository.VantagemRepository;

@Service
@Transactional
public class VantagemService {

    private final VantagemRepository repo;
    private final EmpresaParceiraRepository empresaRepo;

    public VantagemService(VantagemRepository repo, EmpresaParceiraRepository empresaRepo) { 
        this.repo = repo;
        this.empresaRepo = empresaRepo;
    }

    public List<Vantagem> findAll() { 
        return repo.findAll(); 
    }
    
    public Optional<Vantagem> findById(Long id) { 
        return repo.findById(id); 
    }
    
    /**
     * Salva uma vantagem validando regras de negócio
     */
    public Vantagem save(Vantagem vantagem) {
        // Validar que a empresa existe
        if (vantagem.getEmpresa() == null || vantagem.getEmpresa().getId() == null) {
            throw new IllegalArgumentException("Empresa é obrigatória para cadastrar uma vantagem");
        }
        
        // Buscar empresa no banco de dados
        EmpresaParceira empresa = empresaRepo.findById(vantagem.getEmpresa().getId())
                .orElseThrow(() -> new IllegalArgumentException("Empresa não encontrada com ID: " + vantagem.getEmpresa().getId()));
        
        // Garantir que a empresa está associada corretamente
        vantagem.setEmpresa(empresa);
        
        // Validar custo positivo
        if (vantagem.getCusto() == null || vantagem.getCusto() <= 0) {
            throw new IllegalArgumentException("Custo deve ser um valor positivo");
        }
        
        // Validar descrição
        if (vantagem.getDescricao() == null || vantagem.getDescricao().trim().isEmpty()) {
            throw new IllegalArgumentException("Descrição é obrigatória");
        }
        
        return repo.save(vantagem);
    }
    
    public void deleteById(Long id) { 
        if (!repo.existsById(id)) {
            throw new IllegalArgumentException("Vantagem não encontrada com ID: " + id);
        }
        repo.deleteById(id); 
    }
}