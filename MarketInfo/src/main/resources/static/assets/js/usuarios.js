document.getElementById("pesquisaEmailUsuario").addEventListener("keyup", filtrarTabela);
document.getElementById("pesquisaNomeUsuario").addEventListener("keyup", filtrarTabela);
document.getElementById("selectPesquisaPerfilUsuario").addEventListener("change", filtrarTabela);

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
    var selectRoleEdicaoUsuario = $('#selectRoleEdicaoUsuario').val();
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

function filtrarTabela() {
    var nomeFilter = document.getElementById("pesquisaNomeUsuario").value.toLowerCase();
    var emailFilter = document.getElementById("pesquisaEmailUsuario").value.toLowerCase();
    var perfilFilter = document.getElementById("selectPesquisaPerfilUsuario").value.toLowerCase();
    if (perfilFilter == "role_user") {
        perfilFilter = "usuário"
    }
    if (perfilFilter == "role_admin") {
        perfilFilter = "administrador"
    }

    var table = document.querySelector(".table");
    var rows = table.querySelectorAll("tbody tr");

    rows.forEach(function(row) {
        var nome = row.querySelector("td:nth-child(1)").textContent.toLowerCase();
        var email = row.querySelector("td:nth-child(2)").textContent.toLowerCase();
        var perfil= row.querySelector("td:nth-child(3)").textContent.toLowerCase();

        var nomeMatch = nome.indexOf(nomeFilter) > -1 || nomeFilter === "";
        var emailMatch = email.indexOf(emailFilter) > -1 || emailFilter === "";
        var perfilMatch = perfil.indexOf(perfilFilter) > -1 || perfilFilter === "";


        if (nomeMatch && emailMatch && perfilMatch) {
            row.style.display = "";
        } else {
            row.style.display = "none";
        }
    });
}