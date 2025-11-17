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

    public List<Aluno> findAll() { 
        return repo.findAll(); 
    }
    
    public Optional<Aluno> findById(Long id) { 
        return repo.findById(id); 
    }
    
    public Optional<Aluno> findByEmail(String email) {
        return repo.findByEmail(email);
    }
    
    public Optional<Aluno> findByCpf(String cpf) {
        return repo.findByCpf(cpf);
    }
    
    /**
     * Salva um aluno validando regras de negócio
     */
    public Aluno save(Aluno aluno) {
        // Validação de email duplicado (exceto se for atualização do mesmo registro)
        if (aluno.getId() == null) {
            // Novo aluno
            if (repo.existsByEmail(aluno.getEmail())) {
                throw new IllegalArgumentException("Email já cadastrado no sistema");
            }
            if (repo.existsByCpf(aluno.getCpf())) {
                throw new IllegalArgumentException("CPF já cadastrado no sistema");
            }
        } else {
            // Atualização - verifica se email/CPF não pertencem a outro aluno
            Optional<Aluno> alunoComEmail = repo.findByEmail(aluno.getEmail());
            if (alunoComEmail.isPresent() && !alunoComEmail.get().getId().equals(aluno.getId())) {
                throw new IllegalArgumentException("Email já cadastrado para outro aluno");
            }
            Optional<Aluno> alunoComCpf = repo.findByCpf(aluno.getCpf());
            if (alunoComCpf.isPresent() && !alunoComCpf.get().getId().equals(aluno.getId())) {
                throw new IllegalArgumentException("CPF já cadastrado para outro aluno");
            }
        }
        
        // Garante que saldo inicial seja 0 se não especificado
        if (aluno.getSaldo() == null) {
            aluno.setSaldo(0.0);
        }
        
        return repo.save(aluno);
    }
    
    public void deleteById(Long id) { 
        if (!repo.existsById(id)) {
            throw new IllegalArgumentException("Aluno não encontrado com ID: " + id);
        }
        repo.deleteById(id); 
    }
}