package com.universidade.moedaestudantil.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.universidade.moedaestudantil.model.InstituicaoEnsino;
import com.universidade.moedaestudantil.service.InstituicaoEnsinoService;

@RestController
@RequestMapping("/api/instituicoes")
public class InstituicaoEnsinoController {

    private final InstituicaoEnsinoService service;

    public InstituicaoEnsinoController(InstituicaoEnsinoService service) { this.service = service; }

    @GetMapping
    public List<InstituicaoEnsino> all() { return service.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<InstituicaoEnsino> get(@PathVariable Long id) {
        return service.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<InstituicaoEnsino> create(@RequestBody InstituicaoEnsino i) { return ResponseEntity.ok(service.save(i)); }

    @PutMapping("/{id}")
    public ResponseEntity<InstituicaoEnsino> update(@PathVariable Long id, @RequestBody InstituicaoEnsino i) {
        return service.findById(id).map(e -> {
            i.setId(id);
            return ResponseEntity.ok(service.save(i));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}