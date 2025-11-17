// Sistema de Autenticação
const Auth = {
    // Salvar dados do usuário logado
    setUser(userData) {
        localStorage.setItem('user', JSON.stringify(userData));
    },

    // Obter dados do usuário logado
    getUser() {
        const userStr = localStorage.getItem('user');
        return userStr ? JSON.parse(userStr) : null;
    },

    // Verificar se está logado
    isAuthenticated() {
        return this.getUser() !== null;
    },

    // Obter tipo de usuário
    getUserType() {
        const user = this.getUser();
        return user ? user.tipo : null;
    },

    // Verificar se é aluno
    isAluno() {
        return this.getUserType() === 'ALUNO';
    },

    // Verificar se é professor
    isProfessor() {
        return this.getUserType() === 'PROFESSOR';
    },

    // Verificar se é empresa
    isEmpresa() {
        return this.getUserType() === 'EMPRESA';
    },

    // Fazer logout
    logout() {
        localStorage.removeItem('user');
        window.location.href = 'login.html';
    },

    // Redirecionar baseado no tipo de usuário
    redirectByType() {
        const tipo = this.getUserType();
        if (tipo === 'ALUNO') {
            window.location.href = 'dashboard-aluno.html';
        } else if (tipo === 'PROFESSOR') {
            window.location.href = 'dashboard-professor.html';
        } else if (tipo === 'EMPRESA') {
            window.location.href = 'dashboard-empresa.html';
        } else {
            window.location.href = 'login.html';
        }
    },

    // Proteger rota - verificar se pode acessar
    protectRoute(allowedTypes) {
        // Modo desenvolvedor - permite acesso a todas as páginas
        if (this.isDevMode()) {
            return true;
        }

        if (!this.isAuthenticated()) {
            window.location.href = 'login.html';
            return false;
        }

        const userType = this.getUserType();
        if (!allowedTypes.includes(userType)) {
            // Redirecionar para dashboard correto
            this.redirectByType();
            return false;
        }

        return true;
    },

    // ============================================
    // MODO DESENVOLVEDOR - Para testes locais
    // ============================================
    
    // Ativar modo desenvolvedor (permite acesso a todas as páginas)
    enableDevMode(userType = 'ALUNO') {
        const devUsers = {
            'ALUNO': {
                tipo: 'ALUNO',
                id: 999,
                nome: 'Aluno Teste (Dev Mode)',
                login: 'dev@teste.com'
            },
            'PROFESSOR': {
                tipo: 'PROFESSOR',
                id: 999,
                nome: 'Professor Teste (Dev Mode)',
                login: '11122233344'
            },
            'EMPRESA': {
                tipo: 'EMPRESA',
                id: 999,
                nome: 'Empresa Teste (Dev Mode)',
                login: 'dev@empresa.com'
            }
        };
        
        this.setUser(devUsers[userType] || devUsers['ALUNO']);
        console.log(`🔧 Modo Desenvolvedor ATIVADO como: ${userType}`);
    },

    // Verificar se está em modo desenvolvedor
    isDevMode() {
        const user = this.getUser();
        return user && user.id === 999 && user.nome && user.nome.includes('(Dev Mode)');
    },

    // Desativar modo desenvolvedor
    disableDevMode() {
        localStorage.removeItem('user');
        console.log('🔧 Modo Desenvolvedor DESATIVADO');
    },

    // Alternar entre tipos de usuário no modo dev
    switchDevUserType(userType) {
        if (this.isDevMode()) {
            this.enableDevMode(userType);
        }
    }
};

