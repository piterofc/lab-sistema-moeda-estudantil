package com.universidade.moedaestudantil.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.universidade.moedaestudantil.model.Aluno;
import com.universidade.moedaestudantil.model.Professor;
import com.universidade.moedaestudantil.model.Transacao;
import com.universidade.moedaestudantil.model.Vantagem;
import com.universidade.moedaestudantil.repository.AlunoRepository;
import com.universidade.moedaestudantil.repository.ProfessorRepository;
import com.universidade.moedaestudantil.repository.TransacaoRepository;
import com.universidade.moedaestudantil.repository.VantagemRepository;

@Service
@Transactional
public class TransacaoService {

    private final TransacaoRepository transacaoRepo;
    private final AlunoRepository alunoRepo;
    private final ProfessorRepository professorRepo;
    private final VantagemRepository vantagemRepo;

    @Autowired
    private EmailService emailService;

    public TransacaoService(TransacaoRepository transacaoRepo, AlunoRepository alunoRepo,
                           ProfessorRepository professorRepo, VantagemRepository vantagemRepo) {
        this.transacaoRepo = transacaoRepo;
        this.alunoRepo = alunoRepo;
        this.professorRepo = professorRepo;
        this.vantagemRepo = vantagemRepo;
    }

    public List<Transacao> findAll() {
        return transacaoRepo.findAll();
    }

    public Optional<Transacao> findById(Long id) {
        return transacaoRepo.findById(id);
    }

    public Transacao save(Transacao transacao) {
        return transacaoRepo.save(transacao);
    }

    public void deleteById(Long id) {
        transacaoRepo.deleteById(id);
    }

    public List<Transacao> findByAlunoId(Long alunoId) {
        Aluno aluno = alunoRepo.findById(alunoId)
                .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado"));
        return transacaoRepo.findByAluno(aluno);
    }

    public List<Transacao> findByProfessorId(Long professorId) {
        Professor professor = professorRepo.findById(professorId)
                .orElseThrow(() -> new IllegalArgumentException("Professor não encontrado"));
        return transacaoRepo.findByProfessor(professor);
    }

    /**
     * Processa uma transação validando regras de negócio e atualizando saldos
     */
    public Transacao processarTransacao(Transacao transacao) {
        String tipo = transacao.getTipo();

        if ("ENVIO".equals(tipo)) {
            return processarEnvio(transacao);
        } else if ("RESGATE".equals(tipo)) {
            return processarResgate(transacao);
        } else {
            // Para outros tipos, apenas salva
            return save(transacao);
        }
    }

    private Transacao processarEnvio(Transacao transacao) {
        if (transacao.getProfessor() == null || transacao.getAluno() == null) {
            throw new IllegalArgumentException("Professor e aluno são obrigatórios para transação de ENVIO");
        }

        Professor professor = professorRepo.findById(transacao.getProfessor().getId())
                .orElseThrow(() -> new IllegalArgumentException("Professor não encontrado"));

        Aluno aluno = alunoRepo.findById(transacao.getAluno().getId())
                .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado"));

        if (professor.getSaldo() < transacao.getQuantidade()) {
            throw new IllegalArgumentException("Professor não possui saldo suficiente. Saldo atual: " + professor.getSaldo());
        }

        // Atualiza saldos
        professor.setSaldo(professor.getSaldo() - transacao.getQuantidade());
        aluno.setSaldo(aluno.getSaldo() + transacao.getQuantidade());

        // Salva as alterações
        professorRepo.save(professor);
        alunoRepo.save(aluno);

        // Configura a transação
        transacao.setProfessor(professor);
        transacao.setAluno(aluno);
        transacao.setTipo("ENVIO");

        save(transacao);

        // Enviar email de notificação ao aluno
        String assunto = "Notificação de Recebimento de Moeda Estudantil";
        String conteudo = String.format("Olá %s,\n\nVocê recebeu %d moedas estudantis do professor %s.\nMotivo especificado: %s\n\nAtenciosamente,\nSistema de Moeda Estudantil",
                aluno.getNome(), transacao.getQuantidade(), professor.getNome(), transacao.getMotivo());

        try {
            emailService.sendMail(aluno.getEmail(), assunto, conteudo);
        } catch (Exception e) {
            System.err.println("Falha ao enviar email para o aluno: " + e.getMessage());
        }

        return transacao;
    }

    private Transacao processarResgate(Transacao transacao) {
        if (transacao.getAluno() == null || transacao.getVantagem() == null) {
            throw new IllegalArgumentException("Aluno e vantagem são obrigatórios para transação de RESGATE");
        }

        Aluno aluno = alunoRepo.findById(transacao.getAluno().getId())
                .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado"));

        Vantagem vantagem = vantagemRepo.findById(transacao.getVantagem().getId())
                .orElseThrow(() -> new IllegalArgumentException("Vantagem não encontrada"));

        if (aluno.getSaldo() < vantagem.getCusto()) {
            throw new IllegalArgumentException("Aluno não possui saldo suficiente. Saldo atual: " + aluno.getSaldo() + ", Custo: " + vantagem.getCusto());
        }

        // Atualiza saldo do aluno
        aluno.setSaldo(aluno.getSaldo() - vantagem.getCusto());
        alunoRepo.save(aluno);

        /*
        // Gera código do cupom se não existir
        if (vantagem.getCodigoCupom() == null || vantagem.getCodigoCupom().isEmpty()) {
            vantagem.setCodigoCupom("CUPOM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            vantagemRepo.save(vantagem);
        }
        */

        // Configura a transação
        transacao.setAluno(aluno);
        transacao.setVantagem(vantagem);
        transacao.setQuantidade(vantagem.getCusto());
        transacao.setTipo("RESGATE");
        transacao.setVantagemCupom("CUPOM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        System.out.println("Transação de resgate processada. Cupom gerado: " + transacao.getVantagemCupom());

        // Enviar email de notificação ao aluno
        String assunto = "Notificação de Resgate de Vantagem";

        String conteudoAluno = String.format("Olá %s,\n\nVocê resgatou a vantagem: %s.\nCusto: %d moedas estudantis.\nCupom: %s\n\nAtenciosamente,\nSistema de Moeda Estudantil",
                aluno.getNome(), vantagem.getDescricao(), vantagem.getCusto(), transacao.getVantagemCupom());

        String conteudoEmpresa = String.format("Atenção %s,\n\nO aluno %s resgatou a vantagem: %s.\nCusto: %d moedas estudantis.\nCupom: %s\n\nAtenciosamente,\nSistema de Moeda Estudantil",
                vantagem.getEmpresa().getNome(), aluno.getNome(), vantagem.getDescricao(), vantagem.getCusto(), transacao.getVantagemCupom());

        // Email para o aluno
        try {
            emailService.sendMail(aluno.getEmail(), assunto, conteudoAluno);
        } catch (Exception e) {
            System.err.println("Falha ao enviar email para o aluno: " + e.getMessage());
        }

        // Email para a empresa parceira
        try {
            emailService.sendMail(vantagem.getEmpresa().getEmail(), assunto, conteudoEmpresa);
        } catch (Exception e) {
            System.err.println("Falha ao enviar email para a empresa parceira: " + e.getMessage());
        }

        return save(transacao);
    }
}