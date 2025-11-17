// Carregar alunos ao inicializar
document.addEventListener('DOMContentLoaded', async () => {
    // Verificar autenticação - apenas alunos podem acessar
    if (!Auth.protectRoute(['ALUNO'])) {
        return;
    }

    const user = Auth.getUser();
    if (!user) {
        window.location.href = 'login.html';
        return;
    }

    // Ocultar select e consultar automaticamente
    const alunoSelect = document.getElementById('aluno');
    const consultarBtn = document.getElementById('consultarBtn');
    const formGroup = alunoSelect.closest('.form-group');
    
    if (formGroup) formGroup.style.display = 'none';
    if (consultarBtn) consultarBtn.style.display = 'none';
    
    // Consultar extrato automaticamente
    await consultarExtrato(user.id);
});

// Carregar lista de alunos
async function carregarAlunos() {
    const selectAluno = document.getElementById('aluno');
    
    try {
        const alunos = await api.getAlunos();
        
        selectAluno.innerHTML = '<option value="">Selecione um aluno...</option>';
        
        if (alunos && alunos.length > 0) {
            alunos.forEach(aluno => {
                const option = document.createElement('option');
                option.value = aluno.id;
                option.textContent = `${aluno.nome} (${aluno.curso})`;
                selectAluno.appendChild(option);
            });
        } else {
            showMessage('ℹ️ Nenhum aluno cadastrado. <a href="cadastro-aluno.html">Cadastre um aluno primeiro</a>.', 'info');
        }
    } catch (error) {
        console.error('Erro ao carregar alunos:', error);
        const errorMsg = error.message || 'Erro desconhecido ao carregar alunos';
        showMessage(`❌ ${errorMsg}`, 'error');
    }
}

// Consultar extrato
async function consultarExtrato(alunoId = null) {
    if (!alunoId) {
        alunoId = document.getElementById('aluno')?.value;
    }
    
    if (!alunoId) {
        // Se não tem alunoId, usar do usuário logado
        const user = Auth.getUser();
        if (user && user.id) {
            alunoId = user.id;
        } else {
            showMessage('❌ Por favor, selecione um aluno.', 'error');
            return;
        }
    }
    
    try {
        showLoading(true);
        hideMessage();
        
        const extrato = await api.getExtratoAluno(alunoId);
        
        // Exibir saldo
        document.getElementById('saldo-valor').textContent = `${extrato.saldo || 0} moedas`;
        
        // Exibir transações
        exibirTransacoes(extrato.transacoes || []);
        
        // Mostrar container
        document.getElementById('extrato-container').classList.add('show');
        
    } catch (error) {
        console.error('Erro ao consultar extrato:', error);
        const errorMsg = error.message || 'Erro desconhecido ao consultar extrato';
        showMessage(`❌ ${errorMsg}`, 'error');
        document.getElementById('extrato-container').classList.remove('show');
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
    
    const tipo = transacao.tipo || 'DESCONHECIDO';
    const tipoClass = tipo.toLowerCase();
    const tipoLabel = {
        'ENVIO': '📤 Recebimento',
        'RECEBIMENTO': '📥 Recebimento',
        'RESGATE': '🎁 Resgate'
    }[tipo] || tipo;
    
    const data = new Date(transacao.data).toLocaleString('pt-BR');
    const quantidade = transacao.quantidade || 0;
    const motivo = transacao.motivo || 'Sem motivo informado';
    
    let detalhes = '';
    if (tipo === 'ENVIO' || tipo === 'RECEBIMENTO') {
        detalhes = transacao.professor ? `De: ${transacao.professor.nome}` : '';
    } else if (tipo === 'RESGATE') {
        detalhes = transacao.vantagem ? `Vantagem: ${transacao.vantagem.descricao}` : '';
        if (transacao.vantagem?.codigoCupom) {
            detalhes += ` | Código: ${transacao.vantagem.codigoCupom}`;
        }
    }
    
    item.innerHTML = `
        <div class="transacao-header">
            <span class="transacao-tipo ${tipoClass}">${tipoLabel}</span>
            <span class="transacao-data">${data}</span>
        </div>
        <div class="transacao-valor ${tipoClass}">
            ${tipo === 'RESGATE' ? '-' : '+'} ${quantidade} moedas
        </div>
        <div class="transacao-motivo">
            <strong>Motivo:</strong> ${motivo}
        </div>
        ${detalhes ? `<div class="transacao-detalhes">${detalhes}</div>` : ''}
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

