// Carregar dados ao inicializar
document.addEventListener('DOMContentLoaded', async () => {
    // carregar apenas alunos; o professor é o usuário autenticado
    await carregarAlunos();

    const currentUser = (typeof auth !== 'undefined' && auth.getCurrentUser) ? await auth.getCurrentUser() : null;
    if (currentUser) {
        await prepararProfessorAtual(currentUser);
    } else {
        showMessage('❌ Usuário não autenticado. Faça login para enviar moedas.', 'error');
    }

    const form = document.getElementById('envioForm');
    form.addEventListener('submit', handleSubmit);
});

// Não carregamos lista de professores no cliente: o professor é sempre o usuário autenticado

// Prepara a interface com informações do professor autenticado
async function prepararProfessorAtual(user) {
    const placeholder = document.getElementById('current-professor-placeholder');
    if (placeholder) placeholder.innerHTML = `<p><strong>Professor:</strong> ${user.nome} (${user.matricula || user.id})</p>`;

    const hidden = document.getElementById('hidden-professor-id');
    if (hidden) hidden.value = user.id;

    let professorDados = await api.getProfessor(user.id);

    // atualizar saldo exibido (se o usuário trouxer saldo no objeto)
    const saldoSpan = document.getElementById('professor-saldo');
    if (saldoSpan) saldoSpan.textContent = professorDados.saldo || '0';
    const infoDiv = document.getElementById('professor-info');
    if (infoDiv && saldoSpan) infoDiv.style.display = 'block';
}

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

// Não há seleção de professor: atualização de saldo é feita via prepararProfessorAtual

// Manipular envio do formulário
async function handleSubmit(event) {
    event.preventDefault();
    
    const form = event.target;
    const formData = new FormData(form);
    
    // Validar campos
    let alunoId = formData.get('aluno');
    const currentUser = (typeof auth !== 'undefined' && auth.getCurrentUser) ? await auth.getCurrentUser() : null;
    const professorId = currentUser ? currentUser.id : (formData.get('professor') || document.getElementById('hidden-professor-id')?.value);
    const quantidade = parseFloat(formData.get('quantidade'));
    const motivo = formData.get('motivo').trim();
    
    if (!professorId) {
        showMessage('❌ Usuário não autenticado como professor.', 'error');
        return;
    }

    if (!alunoId) {
        showMessage('❌ Por favor, selecione um aluno.', 'error');
        return;
    }
    
    if (!quantidade || quantidade <= 0) {
        showMessage('❌ Por favor, informe uma quantidade válida (maior que zero).', 'error');
        return;
    }
    
    if (!motivo) {
        showMessage('❌ Por favor, preencha o motivo do reconhecimento.', 'error');
        return;
    }
    
    // Buscar dados completos
    let professor, aluno;
    try {
        [professor, aluno] = await Promise.all([
            api.getProfessor(professorId),
            api.getAluno(alunoId)
        ]);
    } catch (error) {
        const errorMsg = error.message || 'Erro desconhecido ao buscar dados';
        showMessage(`❌ ${errorMsg}`, 'error');
        return;
    }
    
    // Verificar saldo do professor (fazer fetch do professor atual para dados confiáveis)
    if (professor.saldo < quantidade) {
        showMessage(`❌ Saldo insuficiente! Você possui ${professor.saldo} moedas, mas precisa de ${quantidade} moedas.`, 'error');
        return;
    }

    // Preparar transação de envio (enviar IDs, não objetos)
    const transacao = {
        tipo: 'ENVIO',
        quantidade: quantidade,
        motivo: motivo,
        professor: { id: professor.id },
        aluno: { id: aluno.id }
    };

    console.log(transacao);

    // Enviar requisição
    try {
        showLoading(true);
        const resultado = await api.createTransacao(transacao);
        
        showMessage(`✅ Moedas enviadas com sucesso! O aluno ${aluno.nome} recebeu ${quantidade} moedas. Um email de notificação será enviado ao aluno.`, 'success');
        form.reset();
        document.getElementById('professor-info').style.display = 'none';
    } catch (error) {
        console.error('Erro ao enviar moedas:', error);
        const errorMsg = error.message || 'Erro desconhecido ao enviar moedas';
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
    const form = document.getElementById('envioForm');
    const submitBtn = form.querySelector('button[type="submit"]');
    
    if (show) {
        submitBtn.disabled = true;
        submitBtn.textContent = 'Enviando...';
    } else {
        submitBtn.disabled = false;
        submitBtn.textContent = 'Enviar Moedas';
    }
}

