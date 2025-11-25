// Carregar alunos ao inicializar
document.addEventListener('DOMContentLoaded', async () => {
    // Se o usuário atual for ALUNO, buscar extrato automaticamente
    const currentUser = await auth.getCurrentUser() || null;

    console.log('Usuário atual:', currentUser);

    if (currentUser && currentUser.tipo === 'ALUNO') {
        // esconder a seleção de aluno (não faz sentido escolher outro aluno
        const selectWrapper = document.getElementById('aluno-select-wrapper');
        if (selectWrapper) selectWrapper.style.display = 'none';

        // preencher hidden input com o id do aluno (defesa em profundidade)
        const hidden = document.getElementById('hidden-aluno-id');
        if (hidden) hidden.value = currentUser.id;

        // ocultar botão de consulta (pois carregaremos automaticamente)
        const consultarBtn = document.getElementById('consultarBtn');
        if (consultarBtn) consultarBtn.style.display = 'none';

        try {
            showLoading(true);
            hideMessage();

            const extrato = await api.getExtratoAluno(currentUser.id);

            // Exibir saldo
            document.getElementById('saldo-valor').textContent = `${extrato.saldo || 0} moedas`;

            // Exibir transações
            exibirTransacoes(extrato.transacoes || []);

            // Mostrar container
            document.getElementById('extrato-container').style.display = 'block';
        } catch (error) {
            console.error('Erro ao consultar extrato do usuário atual:', error);
            const errorMsg = error.message || 'Erro desconhecido ao consultar extrato';
            showMessage(`❌ ${errorMsg}`, 'error');
            document.getElementById('extrato-container').style.display = 'none';
        } finally {
            showLoading(false);
        }

        return;
    }

    // Não existe seleção de aluno: sempre carregar o extrato do usuário autenticado
    if (!currentUser) {
        showMessage('❌ Usuário não autenticado. Faça login para ver seu extrato.', 'error');
        return;
    }

    // preencher hidden input (caso exista) e placeholder
    const hidden = document.getElementById('hidden-aluno-id');
    if (hidden) hidden.value = currentUser.id;
    const placeholder = document.getElementById('current-aluno-placeholder');
    if (placeholder) placeholder.innerHTML = `<p><strong>Aluno:</strong> ${currentUser.nome} (${currentUser.matricula || currentUser.id})</p>`;

    // carregar extrato do usuário autenticado
    try {
        showLoading(true);
        hideMessage();

        const extrato = await api.getExtratoAluno(currentUser.id);

        document.getElementById('saldo-valor').textContent = `${extrato.saldo || 0} moedas`;
        exibirTransacoes(extrato.transacoes || []);
        document.getElementById('extrato-container').style.display = 'block';
    } catch (error) {
        console.error('Erro ao consultar extrato do usuário atual:', error);
        const errorMsg = error.message || 'Erro desconhecido ao consultar extrato';
        showMessage(`❌ ${errorMsg}`, 'error');
        document.getElementById('extrato-container').style.display = 'none';
    } finally {
        showLoading(false);
    }
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
async function consultarExtrato(passedAlunoId) {
    const currentUser = (typeof auth !== 'undefined' && auth.getCurrentUser) ? auth.getCurrentUser() : null;

    let alunoId = passedAlunoId || document.getElementById('aluno')?.value || document.getElementById('hidden-aluno-id')?.value;

    // Defesa em profundidade: se o usuário autenticado for ALUNO, sempre usar seu próprio ID
    if (currentUser && currentUser.tipo === 'ALUNO') {
        alunoId = currentUser.id;
    }

    if (!alunoId) {
        showMessage('❌ Por favor, selecione um aluno.', 'error');
        return;
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

