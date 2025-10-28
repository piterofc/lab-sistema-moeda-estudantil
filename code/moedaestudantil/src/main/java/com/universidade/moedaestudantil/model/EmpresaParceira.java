package com.universidade.moedaestudantil.model;

import java.util.List;

import jakarta.persistence.*;

@Entity
public class EmpresaParceira {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nome;
    private String cnpj;
    private String email;
    private String senha;
    
    @OneToMany(mappedBy = "empresa")
    private List<Vantagem> vantagens;

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

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public List<Vantagem> getVantagens() {
        return vantagens;
    }

    public void setVantagens(List<Vantagem> vantagens) {
        this.vantagens = vantagens;
    }

}