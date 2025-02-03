document.getElementById('nomePerfil').addEventListener('input', function () {
    let value = this.value.replace(/^ROLE_/, '');
    this.value = `ROLE_${value.toUpperCase()}`;
});
