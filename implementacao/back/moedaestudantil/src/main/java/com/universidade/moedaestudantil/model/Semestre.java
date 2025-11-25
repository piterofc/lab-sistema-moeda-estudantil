package com.universidade.moedaestudantil.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Representa um período semestral do sistema.
 * Usado para controlar quando as moedas são atribuídas aos professores.
 */
@Entity
@Table(name = "semestres")
public class Semestre {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Ano é obrigatório")
    @Column(nullable = false, length = 4)
    private String ano;
    
    @NotBlank(message = "Período é obrigatório")
    @Column(nullable = false, length = 1)
    private String periodo; // "1" ou "2" (primeiro ou segundo semestre)
    
    @NotNull(message = "Data de início é obrigatória")
    @Column(nullable = false)
    private LocalDateTime dataInicio;
    
    @Column
    private LocalDateTime dataFim;
    
    @Column(nullable = false)
    private Boolean ativo = true;
    
    @Column
    private LocalDateTime dataCriacao;
    
    @PrePersist
    protected void onCreate() {
        if (dataCriacao == null) {
            dataCriacao = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public LocalDateTime getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDateTime dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDateTime getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDateTime dataFim) {
        this.dataFim = dataFim;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
    
    /**
     * Retorna a representação do semestre (ex: "2024.1")
     */
    public String getRepresentacao() {
        return ano + "." + periodo;
    }
}

