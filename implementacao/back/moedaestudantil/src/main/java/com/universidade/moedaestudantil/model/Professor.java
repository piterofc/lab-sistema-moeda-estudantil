package com.universidade.moedaestudantil.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "professores")
@DiscriminatorValue("PROFESSOR")
public class Professor extends Usuario {

    @NotBlank(message = "CPF é obrigatório")
    @Column(nullable = false, unique = true)
    private String cpf;

    @NotBlank(message = "Departamento é obrigatório")
    @Column(nullable = false)
    private String departamento;

    @Min(value = 0, message = "Saldo não pode ser negativo")
    @Column(nullable = false)
    private Double saldo = 0.0;

    @NotNull(message = "Instituição de ensino é obrigatória")
    @ManyToOne
    @JoinColumn(name = "instituicao_id", nullable = false)
    @JsonBackReference
    private InstituicaoEnsino instituicao;

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public InstituicaoEnsino getInstituicao() {
        return instituicao;
    }

    public void setInstituicao(InstituicaoEnsino instituicao) {
        this.instituicao = instituicao;
    }

}