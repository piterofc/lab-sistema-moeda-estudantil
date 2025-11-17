// Verificar autenticação
document.addEventListener('DOMContentLoaded', () => {
    if (!Auth.protectRoute(['EMPRESA'])) {
        return;
    }

    const user = Auth.getUser();
    if (user) {
        document.getElementById('welcome-message').textContent = `Bem-vindo, ${user.nome}!`;
    }
});

