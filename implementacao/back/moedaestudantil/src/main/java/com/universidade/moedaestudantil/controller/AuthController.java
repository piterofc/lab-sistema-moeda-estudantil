package com.universidade.moedaestudantil.controller;

import java.net.URI;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.universidade.moedaestudantil.config.security.TokenService;
import com.universidade.moedaestudantil.model.Aluno;
import com.universidade.moedaestudantil.model.EmpresaParceira;
import com.universidade.moedaestudantil.model.Usuario;
import com.universidade.moedaestudantil.repository.UsuarioRepository;
import com.universidade.moedaestudantil.service.AlunoService;
import com.universidade.moedaestudantil.service.EmpresaParceiraService;
import com.universidade.moedaestudantil.service.InstituicaoEnsinoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    private final AlunoService alunoService;
    private final EmpresaParceiraService empresaService;
    private final InstituicaoEnsinoService instituicaoService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        Optional<Usuario> opt = usuarioRepository.findByEmail(req.getEmail());
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Credenciais inválidas"));
        }
        Usuario user = opt.get();
        if (!passwordEncoder.matches(req.getSenha(), user.getSenha())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Credenciais inválidas"));
        }

        String token = tokenService.generateToken(user);
        String tipo = "USER";
        if (user instanceof Aluno) tipo = "ALUNO";
        else if (user instanceof EmpresaParceira) tipo = "EMPRESA";

        return ResponseEntity.ok(new AuthResponse(token, tipo, user.getNome(), user.getEmail()));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {
        try {
            String tipo = req.getTipo() == null ? "" : req.getTipo().toUpperCase().trim();
            Optional<Usuario> usuario = usuarioRepository.findByEmail(req.getEmail());

            if (usuario.isPresent()) {
                return ResponseEntity.badRequest().body(new ErrorResponse("Email já cadastrado"));
            }

            if ("EMPRESA".equals(tipo)) {
                EmpresaParceira empresa = new EmpresaParceira();
                empresa.setNome(req.getNome());
                empresa.setEmail(req.getEmail());
                empresa.setCnpj(req.getCnpj());
                empresa.setSenha(passwordEncoder.encode(req.getSenha()));
                EmpresaParceira saved = empresaService.save(empresa);
                return ResponseEntity.created(URI.create("/api/empresas/" + saved.getId())).body(saved);
            } else if ("ALUNO".equals(tipo)) {
                // Campos obrigatórios para aluno (validações serão aplicadas pelo service/entidade)
                Aluno aluno = new Aluno();
                aluno.setNome(req.getNome());
                aluno.setEmail(req.getEmail());
                aluno.setCpf(req.getCpf());
                aluno.setRg(req.getRg());
                aluno.setEndereco(req.getEndereco());
                aluno.setCurso(req.getCurso());
                aluno.setSenha(passwordEncoder.encode(req.getSenha()));
                if (req.getInstituicaoId() != null) {
                    instituicaoService.findById(req.getInstituicaoId())
                        .orElseThrow(() -> new IllegalArgumentException("Instituição não encontrada"));
                    aluno.setInstituicao(instituicaoService.findById(req.getInstituicaoId()).get());
                }
                Aluno saved = alunoService.save(aluno);
                return ResponseEntity.created(URI.create("/api/alunos/" + saved.getId())).body(saved);
            } else {
                return ResponseEntity.badRequest().body(new ErrorResponse("Tipo inválido. Use 'ALUNO' ou 'EMPRESA'"));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Erro ao registrar usuário"));
        }
    }

    // DTOs
    public static class LoginRequest {
        private String email;
        private String senha;
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getSenha() { return senha; }
        public void setSenha(String senha) { this.senha = senha; }
    }

    public static class RegisterRequest {
        private String tipo; // "ALUNO" ou "EMPRESA"
        private String nome;
        private String email;
        private String senha;
        // Empresa
        private String cnpj;
        // Aluno
        private String cpf;
        private String rg;
        private String endereco;
        private String curso;
        private Long instituicaoId;

        public String getTipo() { return tipo; }
        public void setTipo(String tipo) { this.tipo = tipo; }
        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getSenha() { return senha; }
        public void setSenha(String senha) { this.senha = senha; }
        public String getCnpj() { return cnpj; }
        public void setCnpj(String cnpj) { this.cnpj = cnpj; }
        public String getCpf() { return cpf; }
        public void setCpf(String cpf) { this.cpf = cpf; }
        public String getRg() { return rg; }
        public void setRg(String rg) { this.rg = rg; }
        public String getEndereco() { return endereco; }
        public void setEndereco(String endereco) { this.endereco = endereco; }
        public String getCurso() { return curso; }
        public void setCurso(String curso) { this.curso = curso; }
        public Long getInstituicaoId() { return instituicaoId; }
        public void setInstituicaoId(Long instituicaoId) { this.instituicaoId = instituicaoId; }
    }

    public static class AuthResponse {
        private String token;
        private String tipo;
        private String nome;
        private String email;
        public AuthResponse(String token, String tipo, String nome, String email) {
            this.token = token; this.tipo = tipo; this.nome = nome; this.email = email;
        }
        public String getToken() { return token; }
        public String getTipo() { return tipo; }
        public String getNome() { return nome; }
        public String getEmail() { return email; }
    }

    public static class ErrorResponse {
        private String message;
        public ErrorResponse(String message) { this.message = message; }
        public String getMessage() { return message; }
    }
}