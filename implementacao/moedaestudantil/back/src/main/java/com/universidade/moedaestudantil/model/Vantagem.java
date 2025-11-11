package com.universidade.moedaestudantil.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "vantagens")
public class Vantagem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Descrição é obrigatória")
    @Column(nullable = false, length = 500)
    private String descricao;
    
    @NotNull(message = "Custo é obrigatório")
    @Positive(message = "Custo deve ser positivo")
    @Column(nullable = false)
    private Double custo;
    
    @Column(name = "foto_url")
    private String fotoUrl;
    
    @Column(name = "codigo_cupom", unique = true)
    private String codigoCupom;
    
    @NotNull(message = "Empresa é obrigatória")
    @ManyToOne
    @JoinColumn(name = "empresa_id", nullable = false)
    @JsonBackReference
    private EmpresaParceira empresa;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getCusto() {
        return custo;
    }

    public void setCusto(Double custo) {
        this.custo = custo;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    public String getCodigoCupom() {
        return codigoCupom;
    }

    public void setCodigoCupom(String codigoCupom) {
        this.codigoCupom = codigoCupom;
    }

    public EmpresaParceira getEmpresa() {
        return empresa;
    }

    public void setEmpresa(EmpresaParceira empresa) {
        this.empresa = empresa;
    }

}