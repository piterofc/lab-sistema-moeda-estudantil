// Carregar professores ao inicializar
document.addEventListener('DOMContentLoaded', async () => {
    await carregarProfessores();
    
    const consultarBtn = document.getElementById('consultarBtn');
    consultarBtn.addEventListener('click', consultarExtrato);
});

// Carregar lista de professores
async function carregarProfessores() {
    const selectProfessor = document.getElementById('professor');
    
    try {
        const professores = await api.getProfessores();
        
        selectProfessor.innerHTML = '<option value="">Selecione um professor...</option>';
        
        if (professores && professores.length > 0) {
            professores.forEach(professor => {
                const option = document.createElement('option');
                option.value = professor.id;
                option.textContent = `${professor.nome} (${professor.departamento})`;
                selectProfessor.appendChild(option);
            });
        } else {
            showMessage('ℹ️ Nenhum professor cadastrado. Os professores devem ser pré-cadastrados no sistema.', 'info');
        }
    } catch (error) {
        console.error('Erro ao carregar professores:', error);
        const errorMsg = error.message || 'Erro desconhecido ao carregar professores';
        showMessage(`❌ ${errorMsg}`, 'error');
    }
}

// Consultar extrato
async function consultarExtrato() {
    const professorId = document.getElementById('professor').value;
    
    if (!professorId) {
        showMessage('❌ Por favor, selecione um professor.', 'error');
        return;
    }
    
    try {
        showLoading(true);
        hideMessage();
        
        const extrato = await api.getExtratoProfessor(professorId);
        
        // Exibir saldo
        document.getElementById('saldo-valor').textContent = `${extrato.saldo || 0} moedas`;
        
        // Exibir transações
        exibirTransacoes(extrato.transacoes || []);
        
        // Mostrar container
        document.getElementById('extrato-container').style.display = 'block';
        
    } catch (error) {
        console.error('Erro ao consultar extrato:', error);
        const errorMsg = error.message || 'Erro desconhecido ao consultar extrato';
        showMessage(`❌ ${errorMsg}`, 'error');
        document.getElementById('extrato-container').style.display = 'none';
    } finally {
        showLoading(false);
    }
}

// Exibir transações
function exibirTransacoes(transacoes) {
    const container = document.getElementById('transacoes-list');
    
    if (transacoes.length === 0) {
        container.innerHTML = '<div class="empty-state"><p>Nenhuma transação encontrada.</p></div>';
        return;
    }
    
    // Ordenar por data (mais recente primeiro)
    transacoes.sort((a, b) => new Date(b.data) - new Date(a.data));
    
    container.innerHTML = '';
    
    transacoes.forEach(transacao => {
        const item = criarItemTransacao(transacao);
        container.appendChild(item);
    });
}

// Criar item de transação
function criarItemTransacao(transacao) {
    const item = document.createElement('div');
    item.className = 'transacao-item';
    
    const data = new Date(transacao.data).toLocaleString('pt-BR');
    const quantidade = transacao.quantidade || 0;
    const motivo = transacao.motivo || 'Sem motivo informado';
    const alunoNome = transacao.aluno ? transacao.aluno.nome : 'Aluno não informado';
    
    item.innerHTML = `
        <div class="transacao-header">
            <span class="transacao-tipo envio">📤 Envio de Moedas</span>
            <span class="transacao-data">${data}</span>
        </div>
        <div class="transacao-valor envio">
            - ${quantidade} moedas
        </div>
        <div class="transacao-motivo">
            <strong>Para:</strong> ${alunoNome}
        </div>
        <div class="transacao-detalhes">
            <strong>Motivo:</strong> ${motivo}
        </div>
    `;
    
    return item;
}

// Funções auxiliares
function showLoading(show) {
    const loading = document.getElementById('loading');
    if (show) {
        loading.classList.add('active');
    } else {
        loading.classList.remove('active');
    }
}

function showMessage(message, type = 'info') {
    const messageDiv = document.getElementById('message');
    messageDiv.innerHTML = message;
    messageDiv.className = `message ${type}`;
    messageDiv.style.display = 'block';
    
    if (type !== 'error') {
        setTimeout(() => {
            hideMessage();
        }, 5000);
    }
}

function hideMessage() {
    const messageDiv = document.getElementById('message');
    messageDiv.style.display = 'none';
}

