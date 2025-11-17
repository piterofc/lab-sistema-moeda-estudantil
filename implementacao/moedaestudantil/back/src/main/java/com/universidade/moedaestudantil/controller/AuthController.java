package com.universidade.moedaestudantil.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.universidade.moedaestudantil.model.Aluno;
import com.universidade.moedaestudantil.model.EmpresaParceira;
import com.universidade.moedaestudantil.model.Professor;
import com.universidade.moedaestudantil.repository.AlunoRepository;
import com.universidade.moedaestudantil.repository.EmpresaParceiraRepository;
import com.universidade.moedaestudantil.repository.ProfessorRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AlunoRepository alunoRepo;
    private final ProfessorRepository professorRepo;
    private final EmpresaParceiraRepository empresaRepo;

    public AuthController(AlunoRepository alunoRepo, ProfessorRepository professorRepo, 
                         EmpresaParceiraRepository empresaRepo) {
        this.alunoRepo = alunoRepo;
        this.professorRepo = professorRepo;
        this.empresaRepo = empresaRepo;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String login = request.getLogin();
        String senha = request.getSenha();

        if (login == null || senha == null || login.trim().isEmpty() || senha.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Login e senha são obrigatórios"));
        }

        // Tenta encontrar como Aluno (por email)
        Optional<Aluno> alunoOpt = alunoRepo.findByEmail(login);
        if (alunoOpt.isPresent()) {
            Aluno aluno = alunoOpt.get();
            if (aluno.getSenha().equals(senha)) {
                return ResponseEntity.ok(createSuccessResponse("ALUNO", aluno.getId(), aluno.getNome(), aluno.getEmail()));
            }
        }

        // Tenta encontrar como Professor (por CPF)
        Optional<Professor> professorOpt = professorRepo.findByCpf(login);
        if (professorOpt.isPresent()) {
            Professor professor = professorOpt.get();
            if (professor.getSenha().equals(senha)) {
                return ResponseEntity.ok(createSuccessResponse("PROFESSOR", professor.getId(), professor.getNome(), professor.getCpf()));
            }
        }

        // Tenta encontrar como Empresa (por email)
        Optional<EmpresaParceira> empresaOpt = empresaRepo.findByEmail(login);
        if (empresaOpt.isPresent()) {
            EmpresaParceira empresa = empresaOpt.get();
            if (empresa.getSenha().equals(senha)) {
                return ResponseEntity.ok(createSuccessResponse("EMPRESA", empresa.getId(), empresa.getNome(), empresa.getEmail()));
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(createErrorResponse("Credenciais inválidas"));
    }

    private Map<String, Object> createSuccessResponse(String tipo, Long id, String nome, String login) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("tipo", tipo);
        response.put("id", id);
        response.put("nome", nome);
        response.put("login", login);
        return response;
    }

    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        return response;
    }

    // Classe interna para receber dados de login
    public static class LoginRequest {
        private String login;
        private String senha;

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getSenha() {
            return senha;
        }

        public void setSenha(String senha) {
            this.senha = senha;
        }
    }
}

