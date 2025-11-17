// Gerenciamento simples de autenticação no front
const auth = (function () {
    function setAuth(authResp) {
        console.log('Setting auth with response:', authResp);
        // authResp esperado: { token, tipo, nome, email }
        if (!authResp || !authResp.token) return;
        localStorage.setItem('auth_token', authResp.token);
    }

    function clearAuth() {
        localStorage.removeItem('auth_token');
    }

    async function getCurrentUser() {
        const token = localStorage.getItem('auth_token');
        if (!token) return null;

        let currentUser = await api.me().catch(err => {
            console.error('Erro ao obter dados do usuário atual:', err);
            return null;
        });

        console.log('Current User:', currentUser);

        return currentUser;
    }

    function logout(redirect = 'login.html') {
        clearAuth();
        window.location.href = redirect;
    }

    // Renderiza menu topo conforme tipo de usuário
    async function renderNav() {
        const user = await getCurrentUser();
        const header = document.createElement('header');
        header.style.padding = '12px 20px';
        header.style.borderBottom = '1px solid #eee';
        header.style.display = 'flex';
        header.style.justifyContent = 'space-between';
        header.style.alignItems = 'center';
        header.style.background = '#fff';

        const left = document.createElement('div');
        left.innerHTML = `<a href="index.html" style="text-decoration:none;color:#667eea;font-weight:700">💰 Sistema de Moeda Estudantil</a>`;

        const right = document.createElement('div');

        if (user) {
            const nomeSpan = document.createElement('span');
            nomeSpan.textContent = `${user.nome} (${user.tipo})`;
            nomeSpan.style.marginRight = '12px';
            const logoutBtn = document.createElement('button');
            logoutBtn.textContent = 'Sair';
            logoutBtn.className = 'btn btn-secondary';
            logoutBtn.addEventListener('click', () => logout());

            // Links por tipo
            const links = document.createElement('span');
            links.style.marginRight = '12px';

            if (user.tipo === 'ALUNO') {
                links.innerHTML = `
                    <a href="listagem-vantagens.html" class="btn btn-link">Vantagens</a>
                    <a href="resgate-vantagem.html" class="btn btn-link">Resgatar</a>
                    <a href="extrato-aluno.html" class="btn btn-link">Meu Extrato</a>
                `;
            } else if (user.tipo === 'PROFESSOR') {
                links.innerHTML = `
                    <a href="envio-moedas.html" class="btn btn-link">Enviar Moedas</a>
                    <a href="extrato-professor.html" class="btn btn-link">Meu Extrato</a>
                `;
            } else if (user.tipo === 'EMPRESA') {
                links.innerHTML = `
                    <a href="cadastro-vantagem.html" class="btn btn-link">Cadastrar Vantagem</a>
                    <a href="listagem-vantagens.html" class="btn btn-link">Minhas Vantagens</a>
                `;
            } else {
                links.innerHTML = `<a href="index.html" class="btn btn-link">Início</a>`;
            }

            right.appendChild(links);
            right.appendChild(nomeSpan);
            right.appendChild(logoutBtn);
        } else {
            right.innerHTML = `
                <a href="login.html" class="btn btn-primary" style="margin-right:8px">Entrar</a>
                <a href="register.html" class="btn btn-secondary">Cadastrar</a>
            `;
        }

        header.appendChild(left);
        header.appendChild(right);

        // Insere antes do primeiro elemento .container ou no body
        const container = document.querySelector('.container');
        if (container) {
            container.parentNode.insertBefore(header, container);
        } else {
            document.body.insertBefore(header, document.body.firstChild);
        }
    }

    // Ao carregar cada página, renderiza nav automaticamente
    document.addEventListener('DOMContentLoaded', async () => {
        try { await renderNav(); } catch (e) { /* silencioso */ }
    });

    return {
        setAuth,
        getCurrentUser,
        logout,
        renderNav
    };
})();

// Export no caso de módulos (compatibilidade básica)
if (typeof window !== 'undefined') window.auth = auth;
