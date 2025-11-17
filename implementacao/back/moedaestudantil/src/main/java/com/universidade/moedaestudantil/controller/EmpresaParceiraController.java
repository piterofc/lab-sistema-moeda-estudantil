package com.universidade.moedaestudantil.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.universidade.moedaestudantil.model.EmpresaParceira;
import com.universidade.moedaestudantil.service.EmpresaParceiraService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/empresas")
public class EmpresaParceiraController {

    private final EmpresaParceiraService service;

    public EmpresaParceiraController(EmpresaParceiraService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<EmpresaParceira>> all() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpresaParceira> get(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EmpresaParceira> create(@Valid @RequestBody EmpresaParceira empresa) {
        EmpresaParceira saved = service.save(empresa);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmpresaParceira> update(@PathVariable Long id, @Valid @RequestBody EmpresaParceira empresa) {
        return service.findById(id)
                .map(existing -> {
                    empresa.setId(id);
                    return ResponseEntity.ok(service.save(empresa));
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