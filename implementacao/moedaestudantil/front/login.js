// Verificar se já está logado
document.addEventListener('DOMContentLoaded', () => {
    if (Auth.isAuthenticated()) {
        Auth.redirectByType();
        return;
    }

    const form = document.getElementById('loginForm');
    form.addEventListener('submit', handleLogin);
});

async function handleLogin(event) {
    event.preventDefault();
    
    const form = event.target;
    const formData = new FormData(form);
    
    const login = formData.get('login').trim();
    const senha = formData.get('senha');
    
    if (!login || !senha) {
        showMessage('❌ Por favor, preencha todos os campos.', 'error');
        return;
    }
    
    try {
        showLoading(true);
        const response = await api.login(login, senha);
        
        if (response.success) {
            // Salvar dados do usuário
            Auth.setUser({
                tipo: response.tipo,
                id: response.id,
                nome: response.nome,
                login: response.login
            });
            
            showMessage('✅ Login realizado com sucesso! Redirecionando...', 'success');
            
            // Redirecionar após 1 segundo
            setTimeout(() => {
                Auth.redirectByType();
            }, 1000);
        } else {
            showMessage(`❌ ${response.message || 'Erro ao fazer login'}`, 'error');
        }
    } catch (error) {
        console.error('Erro ao fazer login:', error);
        const errorMsg = error.message || 'Erro desconhecido ao fazer login';
        showMessage(`❌ ${errorMsg}`, 'error');
    } finally {
        showLoading(false);
    }
}

function showMessage(message, type = 'info') {
    const messageDiv = document.getElementById('message');
    messageDiv.textContent = message;
    messageDiv.className = `message ${type}`;
    messageDiv.style.display = 'block';
    
    if (type !== 'error') {
        setTimeout(() => {
            messageDiv.style.display = 'none';
        }, 3000);
    }
}

function showLoading(show) {
    const form = document.getElementById('loginForm');
    const submitBtn = form.querySelector('button[type="submit"]');
    
    if (show) {
        submitBtn.disabled = true;
        submitBtn.textContent = 'Entrando...';
    } else {
        submitBtn.disabled = false;
        submitBtn.textContent = 'Entrar';
    }
}

