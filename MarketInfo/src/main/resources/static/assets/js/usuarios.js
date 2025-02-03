document.getElementById("pesquisaEmailUsuario").addEventListener("keyup", filtrarTabela);
document.getElementById("pesquisaNomeUsuario").addEventListener("keyup", filtrarTabela);
document.getElementById("selectPesquisaPerfilUsuario").addEventListener("change", filtrarTabela);

function modalEditarUsuario(button) {
    var idUsuarioEdicao = button.getAttribute('data-id');
    var emailUsuarioEdicao = button.getAttribute("data-email");
    var nomeEdicaoUsuario = button.getAttribute("data-nome");
    const roleNames = button.dataset.role.split(',');
    const roleMap = {
        'ROLE_USER': 'Usuário',
        'ROLE_ADMIN': 'Administrador'
    };

    $('#modalEditarUsuario').modal('show');
    document.getElementById('idUsuarioEdicao').value = idUsuarioEdicao;
    document.getElementById('emailEdicaoUsuario').value = emailUsuarioEdicao;
    document.getElementById('nomeEdicaoUsuario').value = nomeEdicaoUsuario;
    const roleSelect = $('#selectRoleEdicaoUsuario');
    roleSelect.val([]).trigger('change');
    const selectedValues = [];

    $('#selectRoleEdicaoUsuario option').each(function () {
        const optionText = $(this).text().trim();
        const roleName = roleNames.find(role => roleMap[role] === optionText);
        if (roleName) {
            selectedValues.push($(this).val());
        }
    });
    roleSelect.val(selectedValues).trigger('change');

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

function confirmacaoExcluirUsuario(button) {
    var idUsuarioExclusao = button.getAttribute('data-id')
    Swal.fire({
        title: 'Tem certeza que deseja excluir este usuário?',
        text: "Essa ação não pode ser desfeita!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Sim, excluir este usuário.',
        cancelButtonText: 'Não, voltar.',
        reverseButtons: true
    }).then((result) => {
        if (result.isConfirmed) {
            excluirUsuario(idUsuarioExclusao);
        } else {
            Swal.fire('Cancelado', 'O produto não foi excluído', 'info');
        }
    });
}

function excluirUsuario(idButton) {
    $.ajax({
        url: '/usuario/excluirusuario',
        type: 'POST',
        data: {
            idUsuarioExclusao: idButton
        },
        complete: function(xhr, status) {
            switch (xhr.status) {
                case 200:
                    Swal.fire({
                        title: "Pronto!",
                        text: "O usuário foi excluído com sucesso!",
                        icon: "success",
                        confirmButtonText: 'OK'
                    }).then((result) => {
                        if (result.isConfirmed) {
                            window.location.href = "/usuario/listausuario";
                        }
                    });
                    break;
                case 304:
                    Swal.fire({
                        title: "Ops!",
                        text: "Não foi possível excluir este usuário pois ele possui vínculo à algum produto. Deseja inativá-lo?",
                        icon: "warning",
                        showDenyButton: true,
                        confirmButtonText: 'Sim, inativar',
                        cancelButtonText: 'Não, voltar',
                        reverseButtons: true
                    }).then((result) => {
                        if (result.isConfirmed) {
                            inativarUsuario(idButton)
                        } else {
                            Swal.fire({
                                title: "Cancelado",
                                text: "O usuário não foi inativado ou excluido.",
                                icon: "info",
                                confirmButtonText: 'OK'
                            }).then((result) => {
                                if (result.isConfirmed) {
                                    window.location.href = "/usuario/listausuario";
                                }
                            })
                        }
                    })
                    break;
                case 500:
                    Swal.fire({
                        title: "Erro!",
                        text: "Ocorreu um erro ao excluir este usuário.",
                        icon: "error"
                    });
                    break;
                default:
                    alert("Erro desconhecido: " + status);
            }
        }
    });
}

function inativarUsuario(idButton) {
    $.ajax({
        url: '/usuario/inativarusuario',
        type: 'POST',
        data: {
            idUsuarioInativacao: idButton
        },
        complete: function(xhr, status) {
            switch (xhr.status) {
                case 200:
                    Swal.fire({
                        title: "Pronto!",
                        text: "O usuário foi inativado com sucesso!",
                        icon: "success",
                        confirmButtonText: 'OK'
                    }).then((result) => {
                        if (result.isConfirmed) {
                            window.location.href = "/usuario/listausuario";
                        }
                    });
                    break;
                case 500:
                    Swal.fire({
                        title: "Erro!",
                        text: "Ocorreu um erro ao inativar este usuário.",
                        icon: "error"
                    });
                    break;

                default:
                    alert("Erro desconhecido: " + status);
            }
        }
    });
}