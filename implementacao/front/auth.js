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
        header.style.cssText = `
            padding: 1.5rem 2rem;
            border-bottom: 2px solid #e9ecef;
            display: flex;
            justify-content: center;
            align-items: center;
            background: #ffffff;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
            position: sticky;
            top: 1rem;
            z-index: 1000;
            margin: 0 auto 1.5rem;
            border-radius: 12px;
            max-width: 1200px;
        `;

        const title = document.createElement('div');
        title.innerHTML = `<a href="index.html" style="text-decoration:none;color:#667eea;font-weight:700;font-size:1.75rem;display:flex;align-items:center;justify-content:center;gap:0.75rem;">💰 Sistema de Moeda Estudantil</a>`;

        header.appendChild(title);

        // Insere no início do body
        document.body.insertBefore(header, document.body.firstChild);
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
