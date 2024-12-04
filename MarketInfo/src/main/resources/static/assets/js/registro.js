document.getElementById('registerForm').addEventListener('submit', async function (event) {
    event.preventDefault();

    const username = document.getElementById('username').value;
    const email = document.getElementById('email').value;
    const senha = document.getElementById('senha').value;

    if (!email.includes('@')) {
        alert('O email deve conter o caractere "@".');
        return;
    }

    if (senha.length < 8) {
        alert('A senha deve ter no mínimo 8 caracteres.');
        return;
    }

try {
    const response = await fetch('/registrar', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, email, senha })
    });

    if (response.ok) {
        const result = await response.json();
    } else {
        const error = await response.json();
        alert(error.message || 'Erro ao registrar usuário!');
    }
} catch (e) {
    alert('Erro de conexão. Tente novamente mais tarde!');
}

});
