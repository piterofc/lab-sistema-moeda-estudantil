package com.universidade.moedaestudantil.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.universidade.moedaestudantil.model.Professor;
import com.universidade.moedaestudantil.model.Transacao;
import com.universidade.moedaestudantil.service.ProfessorService;
import com.universidade.moedaestudantil.service.TransacaoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/professores")
public class ProfessorController {

    private final ProfessorService service;
    private final TransacaoService transacaoService;

    public ProfessorController(ProfessorService service, TransacaoService transacaoService) {
        this.service = service;
        this.transacaoService = transacaoService;
    }

    @GetMapping
    public ResponseEntity<List<Professor>> all() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Professor> get(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Professor> create(@Valid @RequestBody Professor professor) {
        Professor saved = service.save(professor);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Professor> update(@PathVariable Long id, @Valid @RequestBody Professor professor) {
        return service.findById(id)
                .map(existing -> {
                    professor.setId(id);
                    return ResponseEntity.ok(service.save(professor));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.findById(id).isPresent()) {
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/extrato")
    public ResponseEntity<?> getExtrato(@PathVariable Long id) {
        return service.findById(id)
                .map(professor -> {
                    List<Transacao> transacoes = transacaoService.findByProfessorId(id);
                    return ResponseEntity.ok(new ExtratoResponse(professor.getSaldo(), transacoes));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Classe interna para resposta do extrato
    public static class ExtratoResponse {
        private Double saldo;
        private List<Transacao> transacoes;

        public ExtratoResponse(Double saldo, List<Transacao> transacoes) {
            this.saldo = saldo;
            this.transacoes = transacoes;
        }

        public Double getSaldo() {
            return saldo;
        }

        public List<Transacao> getTransacoes() {
            return transacoes;
        }
    }
}