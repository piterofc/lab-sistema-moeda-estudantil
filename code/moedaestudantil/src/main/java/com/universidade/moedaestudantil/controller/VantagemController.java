package com.universidade.moedaestudantil.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.universidade.moedaestudantil.model.Vantagem;
import com.universidade.moedaestudantil.service.VantagemService;

@RestController
@RequestMapping("/api/vantagens")
public class VantagemController {

    private final VantagemService service;

    public VantagemController(VantagemService service) { this.service = service; }

    @GetMapping
    public List<Vantagem> all() { return service.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Vantagem> get(@PathVariable Long id) {
        return service.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Vantagem> create(@RequestBody Vantagem v) { return ResponseEntity.ok(service.save(v)); }

    @PutMapping("/{id}")
    public ResponseEntity<Vantagem> update(@PathVariable Long id, @RequestBody Vantagem v) {
        return service.findById(id).map(existing -> {
            v.setId(id);
            return ResponseEntity.ok(service.save(v));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}