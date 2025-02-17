function validarEmails() {
    const input = document.getElementById('emailsNotificacao');
    const erro = document.getElementById('emailErro');
    const emails = input.value.split(',').map(email => email.trim());

    const eValido = emails.every(email => email === '' || /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(email));

    if (!eValido) {
        erro.style.display = 'inline';
    } else {
        erro.style.display = 'none';
    }
}

document.getElementById('alertasEstoque').addEventListener('change', function() {
    document.getElementById('opcoesEstoque').style.display = this.checked ? 'block' : 'none';
});

document.getElementById('alertasProdutos').addEventListener('change', function() {
    document.getElementById('opcoesProdutos').style.display = this.checked ? 'block' : 'none';
});