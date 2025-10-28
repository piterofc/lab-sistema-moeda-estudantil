package com.universidade.moedaestudantil.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.universidade.moedaestudantil.model.Professor;
import com.universidade.moedaestudantil.service.ProfessorService;

@RestController
@RequestMapping("/api/professores")
public class ProfessorController {

    private final ProfessorService service;

    public ProfessorController(ProfessorService service) { this.service = service; }

    @GetMapping
    public List<Professor> all() { return service.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Professor> get(@PathVariable Long id) {
        return service.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Professor> create(@RequestBody Professor p) { return ResponseEntity.ok(service.save(p)); }

    @PutMapping("/{id}")
    public ResponseEntity<Professor> update(@PathVariable Long id, @RequestBody Professor p) {
        return service.findById(id).map(e -> {
            p.setId(id);
            return ResponseEntity.ok(service.save(p));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}