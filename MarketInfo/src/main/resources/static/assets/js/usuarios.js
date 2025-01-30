function modalEditarUsuario(button) {
    var idUsuarioEdicao = button.getAttribute('data-id');
    var emailUsuarioEdicao = button.getAttribute("data-email");
    var nomeEdicaoUsuario = button.getAttribute("data-nome");

    $('#modalEditarUsuario').modal('show');
    document.getElementById('idUsuarioEdicao').value = idUsuarioEdicao;
    document.getElementById('emailEdicaoUsuario').value = emailUsuarioEdicao;
    document.getElementById('nomeEdicaoUsuario').value = nomeEdicaoUsuario;
}

function salvarEdicaoUsuario() {
    var idUsuarioEdicao = document.getElementById('idUsuarioEdicao').value;
    var emailEdicaoUsuario = document.getElementById('emailEdicaoUsuario').value;
    var nomeEdicaoUsuario = document.getElementById('nomeEdicaoUsuario').value;
    var senhaEdicaoUsuario = document.getElementById('senhaEdicaoUsuario').value;
    var selectRoleEdicaoUsuario = document.getElementById('selectRoleEdicaoUsuario').value;
    var checkboxUsuarioAtivo = document.getElementById('checkboxUsuarioAtivo').checked;

    $.ajax({
        url: '/usuario/editarusuario',
        type: 'POST',
        data: {
            idUsuarioEdicao: idUsuarioEdicao,
            emailEdicaoUsuario: emailEdicaoUsuario,
            nomeEdicaoUsuario: nomeEdicaoUsuario,
            senhaEdicaoUsuario: senhaEdicaoUsuario,
            selectRoleEdicaoUsuario: selectRoleEdicaoUsuario,
            checkboxUsuarioAtivo: checkboxUsuarioAtivo
        },
        complete: function(xhr, status) {
            switch (xhr.status) {
                case 200:
                    $('#modalEditarUsuario').modal('hide')
                    Swal.fire({
                        title: "Pronto!",
                        text: "O usuário foi editado com sucesso!",
                        icon: "success",
                        confirmButtonText: 'OK'
                    }).then((result) => {
                        if (result.isConfirmed) {
                            window.location.href = "/usuario/listausuario";
                        }
                    });
                    break;
                case 500:
                    $('#modalEditarUsuario').modal('hide')
                    Swal.fire({
                        title: "Erro!",
                        text: "Ocorreu um erro ao editar este usuário.",
                        icon: "error"
                    });
                    break;
                default:
                    alert("Erro desconhecido: " + status);
            }
        }
    });
}