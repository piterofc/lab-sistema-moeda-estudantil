// Carregar instituições ao carregar a página
document.addEventListener('DOMContentLoaded', async () => {
    await carregarInstituicoes();
    
    const form = document.getElementById('alunoForm');
    form.addEventListener('submit', handleSubmit);
    
    // Máscara para CPF
    const cpfInput = document.getElementById('cpf');
    cpfInput.addEventListener('input', formatarCPF);
});

// Carregar lista de instituições
async function carregarInstituicoes() {
    const selectInstituicao = document.getElementById('instituicao');
    
    try {
        const instituicoes = await api.getInstituicoes();
        
        selectInstituicao.innerHTML = '<option value="">Selecione uma instituição...</option>';
        
        if (instituicoes && instituicoes.length > 0) {
            instituicoes.forEach(instituicao => {
                const option = document.createElement('option');
                option.value = instituicao.id;
                option.textContent = instituicao.nome;
                selectInstituicao.appendChild(option);
            });
        } else {
            showMessage('ℹ️ Nenhuma instituição cadastrada. As instituições devem ser pré-cadastradas no sistema.', 'info');
        }
    } catch (error) {
        console.error('Erro ao carregar instituições:', error);
        const errorMsg = error.message || 'Erro desconhecido ao carregar instituições';
        showMessage(`❌ ${errorMsg}`, 'error');
    }
}

// Formatar CPF
function formatarCPF(event) {
    let value = event.target.value.replace(/\D/g, '');
    if (value.length <= 11) {
        value = value.replace(/(\d{3})(\d)/, '$1.$2');
        value = value.replace(/(\d{3})(\d)/, '$1.$2');
        value = value.replace(/(\d{3})(\d{1,2})$/, '$1-$2');
        event.target.value = value;
    }
}

// Manipular envio do formulário
async function handleSubmit(event) {
    event.preventDefault();
    
    const form = event.target;
    const formData = new FormData(form);
    
    // Validar campos
    const instituicaoId = formData.get('instituicao');
    if (!instituicaoId) {
        showMessage('❌ Por favor, selecione uma instituição de ensino.', 'error');
        return;
    }
    
    // Buscar instituição completa
    let instituicao;
    try {
        instituicao = await api.getInstituicao(instituicaoId);
    } catch (error) {
        const errorMsg = error.message || 'Erro desconhecido ao buscar instituição';
        showMessage(`❌ ${errorMsg}`, 'error');
        return;
    }
    
    // Preparar dados do aluno
    const aluno = {
        nome: formData.get('nome').trim(),
        email: formData.get('email').trim(),
        cpf: formData.get('cpf').replace(/\D/g, ''),
        rg: formData.get('rg').trim(),
        endereco: formData.get('endereco').trim(),
        curso: formData.get('curso').trim(),
        senha: formData.get('senha'),
        instituicao: {
            id: instituicao.id
        }
    };
    
    // Enviar requisição
    try {
        showLoading(true);
        const resultado = await api.createAluno(aluno);
        
        showMessage('✅ Aluno cadastrado com sucesso!', 'success');
        form.reset();
        
        // Redirecionar após 2 segundos
        setTimeout(() => {
            window.location.href = 'index.html';
        }, 2000);
    } catch (error) {
        console.error('Erro ao cadastrar aluno:', error);
        const errorMsg = error.message || 'Erro desconhecido ao cadastrar aluno';
        showMessage(`❌ ${errorMsg}`, 'error');
    } finally {
        showLoading(false);
    }
}

// Funções auxiliares
function showMessage(message, type = 'info') {
    const messageDiv = document.getElementById('message');
    messageDiv.textContent = message;
    messageDiv.className = `message ${type}`;
    messageDiv.style.display = 'block';
    
    if (type !== 'error') {
        setTimeout(() => {
            messageDiv.style.display = 'none';
        }, 5000);
    }
}

function showLoading(show) {
    const form = document.getElementById('alunoForm');
    const submitBtn = form.querySelector('button[type="submit"]');
    
    if (show) {
        submitBtn.disabled = true;
        submitBtn.textContent = 'Cadastrando...';
    } else {
        submitBtn.disabled = false;
        submitBtn.textContent = 'Cadastrar Aluno';
    }
}

