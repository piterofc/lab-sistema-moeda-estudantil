package com.universidade.moedaestudantil.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "alunos")
public class Aluno extends Usuario {
    @NotBlank(message = "CPF é obrigatório")
    @Column(nullable = false, unique = true)
    private String cpf;
    
    @NotBlank(message = "RG é obrigatório")
    @Column(nullable = false)
    private String rg;
    
    @NotBlank(message = "Endereço é obrigatório")
    @Column(nullable = false)
    private String endereco;
    
    @Min(value = 0, message = "Saldo não pode ser negativo")
    @Column(nullable = false)
    private Double saldo = 0.0;
    
    @NotBlank(message = "Curso é obrigatório")
    @Column(nullable = false)
    private String curso;
    
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
    public String getRg() {
        return rg;
    }
    public void setRg(String rg) {
        this.rg = rg;
    }
    public String getEndereco() {
        return endereco;
    }
    public void setEndereco(String endereco) {
        this.endereco = endereco;
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
    public String getCurso() {
        return curso;
    }
    public void setCurso(String curso) {
        this.curso = curso;
    }
}