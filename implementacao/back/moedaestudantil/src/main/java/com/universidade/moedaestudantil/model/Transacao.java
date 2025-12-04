package com.universidade.moedaestudantil.model;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "transacoes")
public class Transacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // @NotNull(message = "Quantidade é obrigatória")
    @Positive(message = "Quantidade deve ser positiva")
    @Column(nullable = true)
    private Double quantidade;
    
    @NotBlank(message = "Motivo é obrigatório")
    @Column(nullable = false, length = 500)
    private String motivo;
    
    @Column(nullable = false)
    private LocalDateTime data;
    
    @NotBlank(message = "Tipo é obrigatório")
    @Column(nullable = false, length = 20)
    private String tipo; // "ENVIO", "RECEBIMENTO", "RESGATE"
    
    @ManyToOne
    @JoinColumn(name = "professor_id")
    private Professor professor;
    
    @ManyToOne
    @JoinColumn(name = "aluno_id")
    private Aluno aluno;
    
    @ManyToOne
    @JoinColumn(name = "vantagem_id")
    private Vantagem vantagem;

    @Column(nullable = true)
    private String vantagemCupom;

    @PrePersist
    protected void onCreate() {
        if (data == null) {
            data = LocalDateTime.now();
        }
    }

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

    public String getVantagemCupom() {
        return vantagemCupom;
    }

    public void setVantagemCupom(String vantagemCupom) {
        this.vantagemCupom = vantagemCupom;
    }

}