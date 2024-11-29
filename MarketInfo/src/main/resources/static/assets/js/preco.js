document.getElementById('formPreco').addEventListener('submit', function(event) {
    var precoInput = document.getElementById('preco');
    var precoValor = precoInput.value;

    precoValor = precoValor.replace('R$', '').replace(/\D/g, '');

    precoInput.value = precoValor;
});

var cleave = new Cleave('#preco', {
    numeral: true,
    numeralThousandsGroupStyle: 'thousand',
    prefix: 'R$ ',
    noImmediatePrefix: true,
    delimiters: [',', '.'],
    decimalMark: ','
});