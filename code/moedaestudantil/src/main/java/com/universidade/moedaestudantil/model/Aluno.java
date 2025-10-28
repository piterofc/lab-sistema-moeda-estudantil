package com.universidade.moedaestudantil.model;

import jakarta.persistence.*;

@Entity
public class Aluno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nome;
    private String email;
    private String cpf;
    private String rg;
    private String endereco;
    private Double saldo = 0.0;
    private String curso;
    private String senha;
    
    @ManyToOne
    private InstituicaoEnsino instituicao;

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
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
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
    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }
}