package com.universidade.moedaestudantil.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.universidade.moedaestudantil.model.InstituicaoEnsino;
import com.universidade.moedaestudantil.service.InstituicaoEnsinoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/instituicoes")
public class InstituicaoEnsinoController {

    private final InstituicaoEnsinoService service;

    public InstituicaoEnsinoController(InstituicaoEnsinoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<InstituicaoEnsino>> all() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InstituicaoEnsino> get(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<InstituicaoEnsino> create(@Valid @RequestBody InstituicaoEnsino instituicao) {
        InstituicaoEnsino saved = service.save(instituicao);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InstituicaoEnsino> update(@PathVariable Long id, @Valid @RequestBody InstituicaoEnsino instituicao) {
        return service.findById(id)
                .map(existing -> {
                    instituicao.setId(id);
                    return ResponseEntity.ok(service.save(instituicao));
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
}