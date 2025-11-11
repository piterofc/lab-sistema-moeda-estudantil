// Carregar ao inicializar
document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('empresaForm');
    form.addEventListener('submit', handleSubmit);
    
    // Máscara para CNPJ
    const cnpjInput = document.getElementById('cnpj');
    cnpjInput.addEventListener('input', formatarCNPJ);
});

// Formatar CNPJ
function formatarCNPJ(event) {
    let value = event.target.value.replace(/\D/g, '');
    if (value.length <= 14) {
        value = value.replace(/(\d{2})(\d)/, '$1.$2');
        value = value.replace(/(\d{3})(\d)/, '$1.$2');
        value = value.replace(/(\d{3})(\d)/, '$1/$2');
        value = value.replace(/(\d{4})(\d{1,2})$/, '$1-$2');
        event.target.value = value;
    }
}

// Manipular envio do formulário
async function handleSubmit(event) {
    event.preventDefault();
    
    const form = event.target;
    const formData = new FormData(form);
    
    // Preparar dados da empresa
    const empresa = {
        nome: formData.get('nome').trim(),
        cnpj: formData.get('cnpj').replace(/\D/g, ''),
        email: formData.get('email').trim(),
        senha: formData.get('senha')
    };
    
    // Enviar requisição
    try {
        showLoading(true);
        const resultado = await api.createEmpresa(empresa);
        
        showMessage('✅ Empresa cadastrada com sucesso!', 'success');
        form.reset();
        
        // Redirecionar após 2 segundos
        setTimeout(() => {
            window.location.href = 'cadastro-vantagem.html';
        }, 2000);
    } catch (error) {
        console.error('Erro ao cadastrar empresa:', error);
        const errorMsg = error.message || 'Erro desconhecido ao cadastrar empresa';
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
    const form = document.getElementById('empresaForm');
    const submitBtn = form.querySelector('button[type="submit"]');
    
    if (show) {
        submitBtn.disabled = true;
        submitBtn.textContent = 'Cadastrando...';
    } else {
        submitBtn.disabled = false;
        submitBtn.textContent = 'Cadastrar Empresa';
    }
}

