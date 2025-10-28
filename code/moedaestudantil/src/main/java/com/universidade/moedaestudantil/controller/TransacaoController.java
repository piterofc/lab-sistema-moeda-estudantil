package com.universidade.moedaestudantil.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.universidade.moedaestudantil.model.Transacao;
import com.universidade.moedaestudantil.service.TransacaoService;

@RestController
@RequestMapping("/api/transacoes")
public class TransacaoController {

    private final TransacaoService service;

    public TransacaoController(TransacaoService service) { this.service = service; }

    @GetMapping
    public List<Transacao> all() { return service.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Transacao> get(@PathVariable Long id) {
        return service.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Transacao> create(@RequestBody Transacao t) { return ResponseEntity.ok(service.save(t)); }

    @PutMapping("/{id}")
    public ResponseEntity<Transacao> update(@PathVariable Long id, @RequestBody Transacao t) {
        return service.findById(id).map(existing -> {
            t.setId(id);
            return ResponseEntity.ok(service.save(t));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}