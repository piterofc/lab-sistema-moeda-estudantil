package com.universidade.moedaestudantil.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.universidade.moedaestudantil.model.EmpresaParceira;
import com.universidade.moedaestudantil.service.EmpresaParceiraService;

@RestController
@RequestMapping("/api/empresas")
public class EmpresaParceiraController {

    private final EmpresaParceiraService service;

    public EmpresaParceiraController(EmpresaParceiraService service) { this.service = service; }

    @GetMapping
    public List<EmpresaParceira> all() { return service.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<EmpresaParceira> get(@PathVariable Long id) {
        return service.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EmpresaParceira> create(@RequestBody EmpresaParceira e) { return ResponseEntity.ok(service.save(e)); }

    @PutMapping("/{id}")
    public ResponseEntity<EmpresaParceira> update(@PathVariable Long id, @RequestBody EmpresaParceira e) {
        return service.findById(id).map(existing -> {
            e.setId(id);
            return ResponseEntity.ok(service.save(e));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}