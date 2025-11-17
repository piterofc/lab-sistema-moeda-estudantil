// Configuração da API
const API_BASE_URL = 'http://localhost:8080/api';

// Função auxiliar para fazer requisições HTTP
async function apiRequest(endpoint, method = 'GET', data = null) {
    const url = `${API_BASE_URL}${endpoint}`;
    const options = {
        method: method,
        headers: {
            'Content-Type': 'application/json',
        },
    };

    // Adicionar token de autenticação se existir
    const user = Auth.getUser();
    if (user && user.id) {
        // Por enquanto, não usamos token JWT, mas podemos adicionar no futuro
        // options.headers['Authorization'] = `Bearer ${user.token}`;
    }

    if (data && (method === 'POST' || method === 'PUT')) {
        options.body = JSON.stringify(data);
    }

    try {
        const response = await fetch(url, options);
        
        // Se a resposta estiver vazia (204 No Content), retorna null
        if (response.status === 204) {
            return null;
        }
        
        const responseData = await response.json().catch(() => null);
        
        if (!response.ok) {
            // Tratar erros de validação do back-end
            let errorMessage = 'Erro desconhecido';
            
            if (responseData) {
                if (responseData.message) {
                    errorMessage = responseData.message;
                } else if (responseData.errors) {
                    // Erros de validação (Bean Validation)
                    const errors = Object.values(responseData.errors).join(', ');
                    errorMessage = `Erro de validação: ${errors}`;
                }
            } else {
                errorMessage = `Erro ${response.status}: ${response.statusText}`;
            }
            
            throw new Error(errorMessage);
        }

        return responseData;
    } catch (error) {
        // Se já é um Error com mensagem, apenas propaga
        if (error instanceof Error) {
            throw error;
        }
        
        // Erro de rede ou outro tipo
        console.error('Erro na requisição:', error);
        throw new Error('Erro de conexão com o servidor. Verifique se o back-end está rodando.');
    }
}

// Funções específicas da API
const api = {
    // Instituições
    getInstituicoes: () => apiRequest('/instituicoes'),
    getInstituicao: (id) => apiRequest(`/instituicoes/${id}`),
    
    // Alunos
    getAlunos: () => apiRequest('/alunos'),
    getAluno: (id) => apiRequest(`/alunos/${id}`),
    createAluno: (aluno) => apiRequest('/alunos', 'POST', aluno),
    updateAluno: (id, aluno) => apiRequest(`/alunos/${id}`, 'PUT', aluno),
    getExtratoAluno: (id) => apiRequest(`/alunos/${id}/extrato`),
    
    // Professores
    getProfessores: () => apiRequest('/professores'),
    getProfessor: (id) => apiRequest(`/professores/${id}`),
    createProfessor: (professor) => apiRequest('/professores', 'POST', professor),
    updateProfessor: (id, professor) => apiRequest(`/professores/${id}`, 'PUT', professor),
    getExtratoProfessor: (id) => apiRequest(`/professores/${id}/extrato`),
    
    // Empresas
    getEmpresas: () => apiRequest('/empresas'),
    getEmpresa: (id) => apiRequest(`/empresas/${id}`),
    createEmpresa: (empresa) => apiRequest('/empresas', 'POST', empresa),
    updateEmpresa: (id, empresa) => apiRequest(`/empresas/${id}`, 'PUT', empresa),
    
    // Vantagens
    getVantagens: () => apiRequest('/vantagens'),
    getVantagem: (id) => apiRequest(`/vantagens/${id}`),
    createVantagem: (vantagem) => apiRequest('/vantagens', 'POST', vantagem),
    updateVantagem: (id, vantagem) => apiRequest(`/vantagens/${id}`, 'PUT', vantagem),
    deleteVantagem: (id) => apiRequest(`/vantagens/${id}`, 'DELETE'),
    
    // Transações
    getTransacoes: () => apiRequest('/transacoes'),
    getTransacao: (id) => apiRequest(`/transacoes/${id}`),
    createTransacao: (transacao) => apiRequest('/transacoes', 'POST', transacao),
    
    // Autenticação
    login: (login, senha) => apiRequest('/auth/login', 'POST', { login, senha }),
};
