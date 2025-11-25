package com.universidade.moedaestudantil.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.universidade.moedaestudantil.model.Professor;
import com.universidade.moedaestudantil.service.ProfessorService;
import com.universidade.moedaestudantil.service.InstituicaoEnsinoService;
import com.universidade.moedaestudantil.service.MoedaSemestralService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controller administrativo para operações administrativas:
 * - Cadastro de professores (pré-cadastro)
 * - Atribuição de moedas semestrais
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    
    private final ProfessorService professorService;
    private final InstituicaoEnsinoService instituicaoService;
    private final PasswordEncoder passwordEncoder;
    private final MoedaSemestralService moedaSemestralService;

    /**
     * Cadastra um novo professor (pré-cadastro administrativo)
     */
    @PostMapping("/professores")
    public ResponseEntity<?> cadastrarProfessor(@Valid @RequestBody ProfessorRegisterRequest req) {
        try {
            Professor professor = new Professor();
            professor.setNome(req.getNome());
            professor.setEmail(req.getEmail());
            professor.setCpf(req.getCpf());
            professor.setDepartamento(req.getDepartamento());
            professor.setSenha(passwordEncoder.encode(req.getSenha()));
            
            if (req.getInstituicaoId() != null) {
                instituicaoService.findById(req.getInstituicaoId())
                    .orElseThrow(() -> new IllegalArgumentException("Instituição não encontrada"));
                professor.setInstituicao(instituicaoService.findById(req.getInstituicaoId()).get());
            } else {
                return ResponseEntity.badRequest().body(new ErrorResponse("Instituição é obrigatória para cadastro de professor"));
            }
            
            Professor saved = professorService.save(professor);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erro ao cadastrar professor: " + e.getMessage()));
        }
    }

    /**
     * Cadastra múltiplos professores em lote (útil para importação da lista da instituição)
     */
    @PostMapping("/professores/lote")
    public ResponseEntity<?> cadastrarProfessoresLote(@Valid @RequestBody List<ProfessorRegisterRequest> professores) {
        try {
            List<Professor> professoresSalvos = professores.stream()
                .map(req -> {
                    Professor professor = new Professor();
                    professor.setNome(req.getNome());
                    professor.setEmail(req.getEmail());
                    professor.setCpf(req.getCpf());
                    professor.setDepartamento(req.getDepartamento());
                    professor.setSenha(passwordEncoder.encode(req.getSenha()));
                    
                    if (req.getInstituicaoId() != null) {
                        instituicaoService.findById(req.getInstituicaoId())
                            .orElseThrow(() -> new IllegalArgumentException("Instituição não encontrada: " + req.getInstituicaoId()));
                        professor.setInstituicao(instituicaoService.findById(req.getInstituicaoId()).get());
                    } else {
                        throw new IllegalArgumentException("Instituição é obrigatória para todos os professores");
                    }
                    
                    return professorService.save(professor);
                })
                .toList();
            
            return ResponseEntity.status(HttpStatus.CREATED).body(professoresSalvos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erro ao cadastrar professores: " + e.getMessage()));
        }
    }

    // DTO para cadastro de professor
    public static class ProfessorRegisterRequest {
        private String nome;
        private String email;
        private String senha;
        private String cpf;
        private String departamento;
        private Long instituicaoId;

        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getSenha() { return senha; }
        public void setSenha(String senha) { this.senha = senha; }
        public String getCpf() { return cpf; }
        public void setCpf(String cpf) { this.cpf = cpf; }
        public String getDepartamento() { return departamento; }
        public void setDepartamento(String departamento) { this.departamento = departamento; }
        public Long getInstituicaoId() { return instituicaoId; }
        public void setInstituicaoId(Long instituicaoId) { this.instituicaoId = instituicaoId; }
    }

    /**
     * Endpoint para atribuir moedas semestrais manualmente (útil para testes ou ajustes)
     * Atribui 1000 moedas a todos os professores que ainda não receberam no semestre atual.
     */
    @PostMapping("/moedas/atribuir-semestrais")
    public ResponseEntity<?> atribuirMoedasSemestrais() {
        try {
            int professoresAtualizados = moedaSemestralService.atribuirMoedasSemestrais();
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Moedas semestrais atribuídas com sucesso");
            response.put("professoresAtualizados", professoresAtualizados);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erro ao atribuir moedas: " + e.getMessage()));
        }
    }

    /**
     * Endpoint para atribuir moedas para um semestre específico
     */
    @PostMapping("/moedas/atribuir-semestre/{semestreId}")
    public ResponseEntity<?> atribuirMoedasParaSemestre(@PathVariable Long semestreId) {
        try {
            int professoresAtualizados = moedaSemestralService.atribuirMoedasParaSemestre(semestreId);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Moedas atribuídas para o semestre com sucesso");
            response.put("professoresAtualizados", professoresAtualizados);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erro ao atribuir moedas: " + e.getMessage()));
        }
    }

    public static class ErrorResponse {
        private String message;
        public ErrorResponse(String message) { this.message = message; }
        public String getMessage() { return message; }
    }
}

