// Carregar dados ao inicializar
document.addEventListener('DOMContentLoaded', async () => {
    const currentUser = await auth.getCurrentUser() || null;

    // Sempre carregar vantagens. Não existe seleção de aluno: o aluno é o usuário autenticado.
    await carregarVantagens();
    if (currentUser) {
        await prepararAlunoAtual(currentUser);
    } else {
        showMessage('❌ Usuário não autenticado. Faça login para resgatar vantagens.', 'error');
    }

    const form = document.getElementById('resgateForm');
    form.addEventListener('submit', handleSubmit);
    
    // Atualizar informações da vantagem ao selecionar
    const vantagemSelect = document.getElementById('vantagem');
    vantagemSelect.addEventListener('change', atualizarInfoVantagem);
});

// Carregar lista de alunos
// Não existe carregamento de lista de alunos no cliente: o aluno é sempre o usuário autenticado.


// Prepara a interface quando o usuário atual é um aluno
async function prepararAlunoAtual(user) {
    let alunoDados = await api.getAluno(user.id);

    // inserir um display legível com nome e saldo no placeholder
    const placeholder = document.getElementById('current-aluno-placeholder');
    if (placeholder) {
        placeholder.innerHTML = `<p><strong>Aluno:</strong> ${user.nome} (${user.matricula || user.id}) — Saldo: ${alunoDados.saldo || 0} moedas</p>`;
    }

    // garantir hidden input com id do aluno
    let hidden = document.getElementById('hidden-aluno-id');
    const form = document.getElementById('resgateForm');
    if (!hidden && form) {
        hidden = document.createElement('input');
        hidden.type = 'hidden';
        hidden.id = 'hidden-aluno-id';
        hidden.name = 'aluno';
        form.appendChild(hidden);
    }
    if (hidden) hidden.value = user.id;
}

// Carregar lista de vantagens
async function carregarVantagens() {
    const selectVantagem = document.getElementById('vantagem');
    
    try {
        const vantagens = await api.getVantagens();
        
        if (vantagens && vantagens.length > 0) {
            selectVantagem.innerHTML = '<option value="">Selecione uma vantagem...</option>';
        
            vantagens.forEach(vantagem => {
                const option = document.createElement('option');
                option.value = vantagem.id;
                option.textContent = `${vantagem.descricao} (${vantagem.empresaNome}) - ${vantagem.custo} moedas`;
                option.dataset.descricao = vantagem.descricao;
                option.dataset.custo = vantagem.custo;
                option.dataset.empresa = vantagem.empresaNome || 'Empresa';
                selectVantagem.appendChild(option);
            });
        } else {
            selectVantagem.innerHTML = '<option value="">ℹ️ Nenhuma vantagem disponível.</option>';
            selectVantagem.disabled = true;
            // Desabilitar botão de submit também
            const submitBtn = document.querySelector('#resgateForm button[type="submit"]');
            if (submitBtn) submitBtn.disabled = true;
        }
    } catch (error) {
        console.error('Erro ao carregar vantagens:', error);
        const errorMsg = error.message || 'Erro desconhecido ao carregar vantagens';
        showMessage(`❌ ${errorMsg}`, 'error');
    }
}

// Atualizar informações da vantagem selecionada
function atualizarInfoVantagem() {
    const selectVantagem = document.getElementById('vantagem');
    const selectedOption = selectVantagem.options[selectVantagem.selectedIndex];
    const infoDiv = document.getElementById('vantagem-info');
    
    if (selectedOption.value) {
        document.getElementById('vantagem-descricao').textContent = selectedOption.dataset.descricao;
        document.getElementById('vantagem-custo').textContent = selectedOption.dataset.custo;
        document.getElementById('vantagem-empresa').textContent = selectedOption.dataset.empresa;
        infoDiv.style.display = 'block';
    } else {
        infoDiv.style.display = 'none';
    }
}

// Manipular envio do formulário
async function handleSubmit(event) {
    event.preventDefault();
    
    const form = event.target;
    const formData = new FormData(form);
    
    // Validar campos
    const vantagemId = formData.get('vantagem');
    let alunoId = formData.get('aluno') || document.getElementById('hidden-aluno-id')?.value;
    const currentUser = (typeof auth !== 'undefined' && auth.getCurrentUser) ? auth.getCurrentUser() : null;

    // Se o usuário autenticado for ALUNO, forçamos o uso do seu próprio ID (defesa em profundidade)
    if (currentUser && currentUser.tipo === 'ALUNO') {
        alunoId = currentUser.id;
    }
    
    if (!alunoId) {
        showMessage('❌ Por favor, selecione um aluno.', 'error');
        return;
    }
    
    if (!vantagemId) {
        showMessage('❌ Por favor, selecione uma vantagem.', 'error');
        return;
    }
    
    // Buscar dados completos
    let aluno, vantagem;
    try {
        [aluno, vantagem] = await Promise.all([
            api.getAluno(alunoId),
            api.getVantagem(vantagemId)
        ]);
    } catch (error) {
        const errorMsg = error.message || 'Erro desconhecido ao buscar dados';
        showMessage(`❌ ${errorMsg}`, 'error');
        return;
    }
    
    // Verificar saldo
    if (aluno.saldo < vantagem.custo) {
        showMessage(`❌ Saldo insuficiente! Você possui ${aluno.saldo} moedas, mas precisa de ${vantagem.custo} moedas.`, 'error');
        return;
    }
    
    // Preparar transação de resgate (enviar IDs, não objetos)
    const transacao = {
        tipo: 'RESGATE',
        quantidade: vantagem.custo,
        motivo: `Resgate da vantagem ID ${vantagem.id}`,
        aluno: { id: alunoId },
        vantagem: { id: vantagemId }
    };
    
    // Enviar requisição
    try {
        showLoading(true);
        const resultado = await api.createTransacao(transacao);
        
        showMessage(`✅ Vantagem resgatada com sucesso! Código do cupom: ${resultado.vantagem?.codigoCupom || 'Gerado'}. Um email será enviado com o código.`, 'success');
        form.reset();
        document.getElementById('vantagem-info').style.display = 'none';
    } catch (error) {
        console.error('Erro ao resgatar vantagem:', error);
        const errorMsg = error.message || 'Erro desconhecido ao resgatar vantagem';
        showMessage(`❌ ${errorMsg}`, 'error');
    } finally {
        showLoading(false);
    }
}

// Funções auxiliares
function showMessage(message, type = 'info') {
    const messageDiv = document.getElementById('message');
    messageDiv.innerHTML = message;
    messageDiv.className = `message ${type}`;
    messageDiv.style.display = 'block';
    
    if (type !== 'error') {
        setTimeout(() => {
            hideMessage();
        }, 8000);
    }
}

function hideMessage() {
    const messageDiv = document.getElementById('message');
    messageDiv.style.display = 'none';
}

function showLoading(show) {
    const form = document.getElementById('resgateForm');
    const submitBtn = form.querySelector('button[type="submit"]');
    
    if (show) {
        submitBtn.disabled = true;
        submitBtn.textContent = 'Processando...';
    } else {
        submitBtn.disabled = false;
        submitBtn.textContent = 'Resgatar Vantagem';
    }
}

