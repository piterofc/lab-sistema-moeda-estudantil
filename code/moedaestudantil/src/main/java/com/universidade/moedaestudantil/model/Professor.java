package com.universidade.moedaestudantil.model;

import jakarta.persistence.*;

@Entity
public class Professor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nome;
    private String cpf;
    private String departamento;
    private Double saldo = 0.0;
    
    @ManyToOne
    private InstituicaoEnsino instituicao;
    private String senha;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
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
    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }

}