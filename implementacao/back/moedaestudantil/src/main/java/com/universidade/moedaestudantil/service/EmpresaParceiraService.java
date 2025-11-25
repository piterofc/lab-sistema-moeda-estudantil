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

    public EmpresaParceiraService(EmpresaParceiraRepository repo) { 
        this.repo = repo; 
    }

    public List<EmpresaParceira> findAll() { 
        return repo.findAll(); 
    }
    
    public Optional<EmpresaParceira> findById(Long id) { 
        return repo.findById(id); 
    }
    
    public Optional<EmpresaParceira> findByEmail(String email) {
        return repo.findByEmail(email);
    }
    
    public Optional<EmpresaParceira> findByCnpj(String cnpj) {
        return repo.findByCnpj(cnpj);
    }
    
    /**
     * Salva uma empresa parceira validando regras de negócio
     */
    public EmpresaParceira save(EmpresaParceira empresa) {
        // Validação de email duplicado (exceto se for atualização do mesmo registro)
        if (empresa.getId() == null) {
            // Nova empresa
            if (repo.existsByEmail(empresa.getEmail())) {
                throw new IllegalArgumentException("Email já cadastrado no sistema");
            }
            if (repo.existsByCnpj(empresa.getCnpj())) {
                throw new IllegalArgumentException("CNPJ já cadastrado no sistema");
            }
        } else {
            // Atualização - verifica se email/CNPJ não pertencem a outra empresa
            Optional<EmpresaParceira> empresaComEmail = repo.findByEmail(empresa.getEmail());
            if (empresaComEmail.isPresent() && !empresaComEmail.get().getId().equals(empresa.getId())) {
                throw new IllegalArgumentException("Email já cadastrado para outra empresa");
            }
            Optional<EmpresaParceira> empresaComCnpj = repo.findByCnpj(empresa.getCnpj());
            if (empresaComCnpj.isPresent() && !empresaComCnpj.get().getId().equals(empresa.getId())) {
                throw new IllegalArgumentException("CNPJ já cadastrado para outra empresa");
            }
        }
        
        return repo.save(empresa);
    }
    
    public void deleteById(Long id) { 
        if (!repo.existsById(id)) {
            throw new IllegalArgumentException("Empresa parceira não encontrada com ID: " + id);
        }
        repo.deleteById(id); 
    }
}