package com.universidade.moedaestudantil.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.universidade.moedaestudantil.model.AtribuicaoMoeda;
import com.universidade.moedaestudantil.model.Professor;
import com.universidade.moedaestudantil.model.Semestre;
import com.universidade.moedaestudantil.repository.AtribuicaoMoedaRepository;
import com.universidade.moedaestudantil.repository.ProfessorRepository;

/**
 * Serviço responsável por atribuir moedas semestrais aos professores.
 * A cada semestre, todos os professores ativos recebem 1000 moedas (acumuláveis).
 */
@Service
@Transactional
public class MoedaSemestralService {
    
    private static final Double MOEDAS_POR_SEMESTRE = 1000.0;
    
    private final ProfessorRepository professorRepo;
    private final SemestreService semestreService;
    private final AtribuicaoMoedaRepository atribuicaoRepo;
    
    public MoedaSemestralService(
            ProfessorRepository professorRepo,
            SemestreService semestreService,
            AtribuicaoMoedaRepository atribuicaoRepo) {
        this.professorRepo = professorRepo;
        this.semestreService = semestreService;
        this.atribuicaoRepo = atribuicaoRepo;
    }
    
    /**
     * Atribui 1000 moedas a todos os professores ativos do semestre atual.
     * Evita atribuições duplicadas verificando se já foi atribuído para o semestre.
     * 
     * @return Número de professores que receberam moedas
     */
    public int atribuirMoedasSemestrais() {
        // Obter ou criar semestre atual
        Semestre semestreAtual = semestreService.obterOuCriarSemestreAtual();
        
        // Buscar todos os professores
        List<Professor> professores = professorRepo.findAll();
        
        int professoresAtualizados = 0;
        
        for (Professor professor : professores) {
            // Verificar se já recebeu moedas neste semestre
            if (!atribuicaoRepo.existsByProfessorAndSemestre(professor, semestreAtual)) {
                // Adicionar 1000 moedas ao saldo atual (acumulável)
                Double novoSaldo = professor.getSaldo() + MOEDAS_POR_SEMESTRE;
                professor.setSaldo(novoSaldo);
                professorRepo.save(professor);
                
                // Registrar a atribuição
                AtribuicaoMoeda atribuicao = new AtribuicaoMoeda();
                atribuicao.setProfessor(professor);
                atribuicao.setSemestre(semestreAtual);
                atribuicao.setQuantidade(MOEDAS_POR_SEMESTRE);
                atribuicao.setDataAtribuicao(LocalDateTime.now());
                atribuicaoRepo.save(atribuicao);
                
                professoresAtualizados++;
            }
        }
        
        return professoresAtualizados;
    }
    
    /**
     * Atribui moedas para um semestre específico (útil para semestres passados ou futuros).
     */
    public int atribuirMoedasParaSemestre(Long semestreId) {
        Semestre semestre = semestreService.findById(semestreId)
            .orElseThrow(() -> new IllegalArgumentException("Semestre não encontrado"));
        
        List<Professor> professores = professorRepo.findAll();
        int professoresAtualizados = 0;
        
        for (Professor professor : professores) {
            if (!atribuicaoRepo.existsByProfessorAndSemestre(professor, semestre)) {
                Double novoSaldo = professor.getSaldo() + MOEDAS_POR_SEMESTRE;
                professor.setSaldo(novoSaldo);
                professorRepo.save(professor);
                
                AtribuicaoMoeda atribuicao = new AtribuicaoMoeda();
                atribuicao.setProfessor(professor);
                atribuicao.setSemestre(semestre);
                atribuicao.setQuantidade(MOEDAS_POR_SEMESTRE);
                atribuicao.setDataAtribuicao(LocalDateTime.now());
                atribuicaoRepo.save(atribuicao);
                
                professoresAtualizados++;
            }
        }
        
        return professoresAtualizados;
    }
    
    /**
     * Job agendado que executa no primeiro dia de cada semestre.
     * Verifica se é início de semestre e atribui moedas automaticamente.
     * 
     * Executa diariamente às 00:00 para verificar se é início de semestre.
     */
    @Scheduled(cron = "0 0 0 * * ?") // Diariamente à meia-noite
    public void verificarEAtribuirMoedasSemestrais() {
        try {
            // Verificar se é início de semestre (primeiro dia de janeiro ou julho)
            LocalDateTime agora = LocalDateTime.now();
            int mes = agora.getMonthValue();
            int dia = agora.getDayOfMonth();
            
            // Primeiro dia de janeiro (semestre 1) ou primeiro dia de julho (semestre 2)
            if ((mes == 1 && dia == 1) || (mes == 7 && dia == 1)) {
                int professoresAtualizados = atribuirMoedasSemestrais();
                System.out.println("Moedas semestrais atribuídas automaticamente. Professores atualizados: " + professoresAtualizados);
            }
        } catch (Exception e) {
            System.err.println("Erro ao executar atribuição automática de moedas: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

