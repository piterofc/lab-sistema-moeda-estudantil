package com.universidade.moedaestudantil.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.universidade.moedaestudantil.model.Aluno;
import com.universidade.moedaestudantil.service.AlunoService;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    private final AlunoService service;

    public AlunoController(AlunoService service) { this.service = service; }

    @GetMapping
    public List<Aluno> all() { return service.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Aluno> get(@PathVariable Long id) {
        return service.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Aluno> create(@RequestBody Aluno a) {
        Aluno saved = service.save(a);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Aluno> update(@PathVariable Long id, @RequestBody Aluno a) {
        return service.findById(id).map(existing -> {
            a.setId(id);
            return ResponseEntity.ok(service.save(a));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}