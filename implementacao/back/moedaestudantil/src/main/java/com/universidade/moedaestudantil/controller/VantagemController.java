package com.universidade.moedaestudantil.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.universidade.moedaestudantil.model.Vantagem;
import com.universidade.moedaestudantil.service.VantagemService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/vantagens")
public class VantagemController {

    private final VantagemService service;

    public VantagemController(VantagemService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Vantagem>> all() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vantagem> get(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Vantagem> create(@Valid @RequestBody Vantagem vantagem) {
        Vantagem saved = service.save(vantagem);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vantagem> update(@PathVariable Long id, @Valid @RequestBody Vantagem vantagem) {
        return service.findById(id)
                .map(existing -> {
                    vantagem.setId(id);
                    return ResponseEntity.ok(service.save(vantagem));
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