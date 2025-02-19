document.addEventListener('DOMContentLoaded', function() {
    const form = document.querySelector('#formOpcoesMensageria');
    mostrarOpcoes()

    form.addEventListener('submit', function(event) {
        const alertasEstoque = document.getElementById('alertasEstoque').checked;
        const alertasProdutos = document.getElementById('alertasProdutos').checked;
        const erroEstoque = document.getElementById('erroCheckboxEstoque');
        const erroProdutos = document.getElementById('erroCheckboxProdutos');

        let valid = true;

        if (alertasEstoque) {
            const estoqueChecks = document.querySelectorAll('#opcoesEstoque input[type="checkbox"]:checked');
            if (estoqueChecks.length === 0) {
                erroEstoque.style.display = 'inline';
                erroEstoque.style.opacity = 1;
                valid = false;

                setTimeout(function() {
                    erroEstoque.style.transition = 'opacity 1s';
                    erroEstoque.style.opacity = 0;
                }, 3000);

                setTimeout(function() {
                    erroEstoque.style.display = 'none';
                    erroEstoque.style.transition = '';
                }, 4000);
            }
        }

        if (alertasProdutos) {
            const produtosChecks = document.querySelectorAll('#opcoesProdutos input[type="checkbox"]:checked');
            if (produtosChecks.length === 0) {
                erroProdutos.style.display = 'inline';
                erroProdutos.style.opacity = 1;
                valid = false;

                // Fade out após 3 segundos
                setTimeout(function() {
                    erroProdutos.style.transition = 'opacity 1s';
                    erroProdutos.style.opacity = 0;
                }, 3000);

                setTimeout(function() {
                    erroProdutos.style.display = 'none';
                    erroProdutos.style.transition = '';
                }, 4000);
            }
        }

        if (!valid) {
            event.preventDefault(); // Impede o envio do form
        }
    });
});

document.getElementById('alertasEstoque').addEventListener('change', function() {
    document.getElementById('opcoesEstoque').style.display = this.checked ? 'block' : 'none';
});

document.getElementById('alertasProdutos').addEventListener('change', function() {
    document.getElementById('opcoesProdutos').style.display = this.checked ? 'block' : 'none';
});

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

function mostrarOpcoes() {
    const alertasEstoque = document.getElementById('alertasEstoque').checked;
    const alertasProdutos = document.getElementById('alertasProdutos').checked;

    document.getElementById('opcoesEstoque').style.display = alertasEstoque ? 'block' : 'none';
    document.getElementById('opcoesProdutos').style.display = alertasProdutos ? 'block' : 'none';
}
