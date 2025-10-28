package com.universidade.moedaestudantil.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
public class InstituicaoEnsino {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nome;
    private String cnpj;
    private String endereco;
    private String telefone;
    private String email;
    
    @OneToMany(mappedBy = "instituicao")
    private List<Aluno> alunos = new ArrayList<>();
    
    @OneToMany(mappedBy = "instituicao")
    private List<Professor> professores = new ArrayList<>();
    
    // Construtores
    public InstituicaoEnsino() {}
    
    public InstituicaoEnsino(String nome, String cnpj, String endereco) {
        this.nome = nome;
        this.cnpj = cnpj;
        this.endereco = endereco;
    }

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

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Aluno> getAlunos() {
        return alunos;
    }

    public void setAlunos(List<Aluno> alunos) {
        this.alunos = alunos;
    }

    public List<Professor> getProfessores() {
        return professores;
    }

    public void setProfessores(List<Professor> professores) {
        this.professores = professores;
    }

}