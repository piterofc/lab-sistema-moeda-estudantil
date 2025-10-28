package com.universidade.moedaestudantil.model;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
public class Transacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Double quantidade;
    private String motivo;
    private LocalDateTime data;
    private String tipo; // "ENVIO", "RECEBIMENTO", "RESGATE"
    
    @ManyToOne
    private Professor professor;
    
    @ManyToOne
    private Aluno aluno;
    
    @ManyToOne
    private Vantagem vantagem;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Double quantidade) {
        this.quantidade = quantidade;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Vantagem getVantagem() {
        return vantagem;
    }

    public void setVantagem(Vantagem vantagem) {
        this.vantagem = vantagem;
    }

}