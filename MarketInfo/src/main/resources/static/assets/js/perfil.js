// FORMATAÇÃO E ADIÇÃO DO PREFIXO "ROLE_" NA TELA DE CADASTRO DE PERFIL
document.getElementById('nomePerfil').addEventListener('input', function () {
    let value = this.value.replace(/^ROLE_/, '');
    this.value = `ROLE_${value.toUpperCase()}`;
});
