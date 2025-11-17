// Verificar autenticação
document.addEventListener('DOMContentLoaded', () => {
    if (!Auth.protectRoute(['PROFESSOR'])) {
        return;
    }

    const user = Auth.getUser();
    if (user) {
        document.getElementById('welcome-message').textContent = `Bem-vindo, ${user.nome}!`;
        carregarSaldo();
    }
});

async function carregarSaldo() {
    const user = Auth.getUser();
    if (!user || !user.id) return;

    try {
        const extrato = await api.getExtratoProfessor(user.id);
        document.getElementById('saldo-valor').textContent = `${extrato.saldo || 0} moedas`;
        document.getElementById('saldo-box').classList.remove('hidden');
    } catch (error) {
        console.error('Erro ao carregar saldo:', error);
    }
}

