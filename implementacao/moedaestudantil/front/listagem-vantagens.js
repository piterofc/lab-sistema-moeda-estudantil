// Carregar vantagens ao carregar a página
document.addEventListener('DOMContentLoaded', () => {
    // Configurar link de voltar baseado no tipo de usuário
    const backLink = document.getElementById('back-link');
    if (Auth.isAluno()) {
        backLink.href = 'dashboard-aluno.html';
    } else if (Auth.isEmpresa()) {
        backLink.href = 'dashboard-empresa.html';
    } else {
        backLink.href = 'index.html';
    }
    
    carregarVantagens();
    
    const refreshBtn = document.getElementById('refreshBtn');
    refreshBtn.addEventListener('click', carregarVantagens);
});

// Carregar lista de vantagens
async function carregarVantagens() {
    const container = document.getElementById('vantagens-container');
    const loading = document.getElementById('loading');
    const messageDiv = document.getElementById('message');
    
    try {
        showLoading(true);
        hideMessage();
        
        const vantagens = await api.getVantagens();
        
        if (vantagens && vantagens.length > 0) {
            exibirVantagens(vantagens);
        } else {
            exibirEmptyState();
        }
    } catch (error) {
        console.error('Erro ao carregar vantagens:', error);
        const errorMsg = error.message || 'Erro desconhecido ao carregar vantagens';
        showMessage(`❌ ${errorMsg}`, 'error');
        container.innerHTML = '';
    } finally {
        showLoading(false);
    }
}

// Exibir vantagens na tela
function exibirVantagens(vantagens) {
    const container = document.getElementById('vantagens-container');
    container.innerHTML = '';
    
    vantagens.forEach(vantagem => {
        const card = criarCardVantagem(vantagem);
        container.appendChild(card);
    });
}

// Criar card de vantagem
function criarCardVantagem(vantagem) {
    const card = document.createElement('div');
    card.className = 'vantagem-card';
    
    // Badge da empresa
    const empresaBadge = document.createElement('div');
    empresaBadge.className = 'empresa-badge';
    empresaBadge.textContent = vantagem.empresa?.nome || 'Empresa';
    card.appendChild(empresaBadge);
    
    // Imagem (se houver)
    if (vantagem.fotoUrl) {
        const img = document.createElement('img');
        img.src = vantagem.fotoUrl;
        img.alt = vantagem.descricao;
        img.className = 'vantagem-imagem';
        img.onerror = function() {
            this.style.display = 'none';
        };
        card.appendChild(img);
    } else {
        const placeholder = document.createElement('div');
        placeholder.className = 'vantagem-imagem';
        placeholder.textContent = '📷 Sem imagem';
        card.appendChild(placeholder);
    }
    
    // Descrição
    const descricao = document.createElement('div');
    descricao.className = 'vantagem-descricao';
    descricao.textContent = vantagem.descricao;
    card.appendChild(descricao);
    
    // Custo
    const custoDiv = document.createElement('div');
    custoDiv.className = 'vantagem-custo';
    
    const custoValor = document.createElement('span');
    custoValor.className = 'custo-valor';
    custoValor.textContent = `${vantagem.custo} moedas`;
    
    const custoLabel = document.createElement('span');
    custoLabel.className = 'custo-label';
    custoLabel.textContent = '💰';
    
    custoDiv.appendChild(custoValor);
    custoDiv.appendChild(custoLabel);
    card.appendChild(custoDiv);
    
    return card;
}

// Exibir estado vazio
function exibirEmptyState() {
    const container = document.getElementById('vantagens-container');
    container.innerHTML = `
        <div class="empty-state" style="grid-column: 1 / -1;">
            <h3>📭 Nenhuma vantagem disponível</h3>
            <p>Não há vantagens cadastradas no momento.</p>
            <p>As empresas parceiras podem cadastrar novas vantagens.</p>
        </div>
    `;
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
    messageDiv.textContent = message;
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
