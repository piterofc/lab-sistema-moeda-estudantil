// Carregar empresas ao carregar a página
document.addEventListener('DOMContentLoaded', async () => {
    await carregarEmpresas();
    
    const form = document.getElementById('vantagemForm');
    form.addEventListener('submit', handleSubmit);
});

// Carregar lista de empresas
async function carregarEmpresas() {
    const selectEmpresa = document.getElementById('empresa');
    
    try {
        const empresas = await api.getEmpresas();
        
        selectEmpresa.innerHTML = '<option value="">Selecione uma empresa...</option>';
        
        if (empresas && empresas.length > 0) {
            empresas.forEach(empresa => {
                const option = document.createElement('option');
                option.value = empresa.id;
                option.textContent = `${empresa.nome} (${empresa.cnpj})`;
                selectEmpresa.appendChild(option);
            });
        } else {
            showMessage('ℹ️ Nenhuma empresa cadastrada. <a href="cadastro-empresa.html">Cadastre uma empresa primeiro</a>.', 'info');
        }
    } catch (error) {
        console.error('Erro ao carregar empresas:', error);
        const errorMsg = error.message || 'Erro desconhecido ao carregar empresas';
        showMessage(`❌ ${errorMsg}`, 'error');
    }
}

// Manipular envio do formulário
async function handleSubmit(event) {
    event.preventDefault();
    
    const form = event.target;
    const formData = new FormData(form);
    
    // Validar campos
    const empresaId = formData.get('empresa');
    const descricao = formData.get('descricao').trim();
    const custo = parseFloat(formData.get('custo'));
    const fotoUrl = formData.get('fotoUrl').trim();
    
    if (!empresaId) {
        showMessage('❌ Por favor, selecione uma empresa.', 'error');
        return;
    }
    
    if (!descricao) {
        showMessage('❌ Por favor, preencha a descrição da vantagem.', 'error');
        return;
    }
    
    if (!custo || custo <= 0) {
        showMessage('❌ Por favor, informe um custo válido (maior que zero).', 'error');
        return;
    }
    
    // Buscar empresa completa
    let empresa;
    try {
        empresa = await api.getEmpresa(empresaId);
    } catch (error) {
        const errorMsg = error.message || 'Erro desconhecido ao buscar empresa';
        showMessage(`❌ ${errorMsg}`, 'error');
        return;
    }
    
    // Preparar dados da vantagem
    const vantagem = {
        descricao: descricao,
        custo: custo,
        fotoUrl: fotoUrl || null,
        empresa: {
            id: empresa.id
        }
    };
    
    // Enviar requisição
    try {
        showLoading(true);
        const resultado = await api.createVantagem(vantagem);
        
        showMessage('✅ Vantagem cadastrada com sucesso!', 'success');
        form.reset();
        
        // Redirecionar após 2 segundos
        setTimeout(() => {
            window.location.href = 'listagem-vantagens.html';
        }, 2000);
    } catch (error) {
        console.error('Erro ao cadastrar vantagem:', error);
        const errorMsg = error.message || 'Erro desconhecido ao cadastrar vantagem';
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
            messageDiv.style.display = 'none';
        }, 5000);
    }
}

function showLoading(show) {
    const form = document.getElementById('vantagemForm');
    const submitBtn = form.querySelector('button[type="submit"]');
    
    if (show) {
        submitBtn.disabled = true;
        submitBtn.textContent = 'Cadastrando...';
    } else {
        submitBtn.disabled = false;
        submitBtn.textContent = 'Cadastrar Vantagem';
    }
}
