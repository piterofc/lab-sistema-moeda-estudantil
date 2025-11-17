// Carregar dados ao inicializar
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

    // Carregar apenas vantagens (aluno já está logado)
    await carregarVantagens();
    
    // Pre-selecionar aluno logado
    const alunoSelect = document.getElementById('aluno');
    alunoSelect.style.display = 'none';
    const alunoLabel = document.querySelector('label[for="aluno"]');
    if (alunoLabel) alunoLabel.style.display = 'none';
    
    // Criar input hidden com ID do aluno
    const hiddenInput = document.createElement('input');
    hiddenInput.type = 'hidden';
    hiddenInput.id = 'aluno';
    hiddenInput.value = user.id;
    document.getElementById('resgateForm').appendChild(hiddenInput);
    
    // Carregar dados do aluno para exibir saldo
    await carregarDadosAluno(user.id);
    
    const form = document.getElementById('resgateForm');
    form.addEventListener('submit', handleSubmit);
    
    // Atualizar informações da vantagem ao selecionar
    const vantagemSelect = document.getElementById('vantagem');
    vantagemSelect.addEventListener('change', atualizarInfoVantagem);
});

// Carregar dados do aluno logado
async function carregarDadosAluno(alunoId) {
    try {
        const aluno = await api.getAluno(alunoId);
        // Exibir saldo do aluno em algum lugar se necessário
        // Por enquanto, será validado no backend
    } catch (error) {
        console.error('Erro ao carregar dados do aluno:', error);
    }
}

// Carregar lista de vantagens
async function carregarVantagens() {
    const selectVantagem = document.getElementById('vantagem');
    
    try {
        const vantagens = await api.getVantagens();
        
        selectVantagem.innerHTML = '<option value="">Selecione uma vantagem...</option>';
        
        if (vantagens && vantagens.length > 0) {
            vantagens.forEach(vantagem => {
                const option = document.createElement('option');
                option.value = vantagem.id;
                option.textContent = `${vantagem.descricao} - ${vantagem.custo} moedas`;
                option.dataset.descricao = vantagem.descricao;
                option.dataset.custo = vantagem.custo;
                option.dataset.empresa = vantagem.empresa?.nome || 'Empresa';
                selectVantagem.appendChild(option);
            });
        } else {
            showMessage('ℹ️ Nenhuma vantagem disponível.', 'info');
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
        infoDiv.classList.remove('hidden');
    } else {
        infoDiv.classList.add('hidden');
    }
}

// Manipular envio do formulário
async function handleSubmit(event) {
    event.preventDefault();
    
    const form = event.target;
    const formData = new FormData(form);
    
    // Validar campos
    const alunoId = formData.get('aluno');
    const vantagemId = formData.get('vantagem');
    
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
    
    // Preparar transação de resgate
    const transacao = {
        tipo: 'RESGATE',
        quantidade: vantagem.custo,
        motivo: `Resgate da vantagem: ${vantagem.descricao}`,
        aluno: {
            id: aluno.id
        },
        vantagem: {
            id: vantagem.id
        }
    };
    
    // Enviar requisição
    try {
        showLoading(true);
        const resultado = await api.createTransacao(transacao);
        
        showMessage(`✅ Vantagem resgatada com sucesso! Código do cupom: ${resultado.vantagem?.codigoCupom || 'Gerado'}. Um email será enviado com o código.`, 'success');
        form.reset();
        document.getElementById('vantagem-info').classList.add('hidden');
        
        // Recarregar dados do aluno para atualizar saldo
        const currentUser = Auth.getUser();
        if (currentUser) {
            await carregarDadosAluno(currentUser.id);
        }
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

