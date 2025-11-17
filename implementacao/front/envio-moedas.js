// Carregar dados ao inicializar
document.addEventListener('DOMContentLoaded', async () => {
    await Promise.all([
        carregarProfessores(),
        carregarAlunos()
    ]);
    
    const form = document.getElementById('envioForm');
    form.addEventListener('submit', handleSubmit);
    
    // Atualizar saldo do professor ao selecionar
    const professorSelect = document.getElementById('professor');
    professorSelect.addEventListener('change', atualizarSaldoProfessor);
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
                option.textContent = `${professor.nome} (Saldo: ${professor.saldo || 0} moedas)`;
                option.dataset.saldo = professor.saldo || 0;
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

// Atualizar saldo do professor selecionado
function atualizarSaldoProfessor() {
    const selectProfessor = document.getElementById('professor');
    const selectedOption = selectProfessor.options[selectProfessor.selectedIndex];
    const infoDiv = document.getElementById('professor-info');
    
    if (selectedOption.value) {
        document.getElementById('professor-saldo').textContent = selectedOption.dataset.saldo || '0';
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
    const professorId = formData.get('professor');
    const alunoId = formData.get('aluno');
    const quantidade = parseFloat(formData.get('quantidade'));
    const motivo = formData.get('motivo').trim();
    
    if (!professorId) {
        showMessage('❌ Por favor, selecione um professor.', 'error');
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
    
    // Verificar saldo do professor
    if (professor.saldo < quantidade) {
        showMessage(`❌ Saldo insuficiente! Você possui ${professor.saldo} moedas, mas precisa de ${quantidade} moedas.`, 'error');
        return;
    }
    
    // Preparar transação de envio
    const transacao = {
        tipo: 'ENVIO',
        quantidade: quantidade,
        motivo: motivo,
        professor: {
            id: professor.id
        },
        aluno: {
            id: aluno.id
        }
    };
    
    // Enviar requisição
    try {
        showLoading(true);
        const resultado = await api.createTransacao(transacao);
        
        showMessage(`✅ Moedas enviadas com sucesso! O aluno ${aluno.nome} recebeu ${quantidade} moedas. Um email de notificação será enviado ao aluno.`, 'success');
        form.reset();
        document.getElementById('professor-info').style.display = 'none';
        
        // Recarregar professores para atualizar saldo
        await carregarProfessores();
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

