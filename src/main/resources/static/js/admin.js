const API_COMENTARIOS = "http://localhost:8080/admin/comentarios";

// ================== MOSTRAR MODAL DE ELIMINACIÓN ==================
function mostrarModalEliminar(tipo, elemento) {
    let id;
    let mensaje = '';
    let targetElement = $(elemento).closest('.col, tr'); // Busca el ancestro más cercano

    if (!targetElement.length) return;

    id = targetElement.data('id');

    if (tipo === 'alumno') {
        mensaje = '¿Estás seguro que deseas eliminar este alumno?';
    } else if (tipo === 'docente') {
        mensaje = '¿Estás seguro que deseas eliminar este docente?';
    } else if (tipo === 'comentario') { 
        mensaje = '¿Estás seguro que deseas eliminar este comentario?';
    } else {
        return;
    }

    // 1. Guarda la data para usarla en la confirmación
    $("#entidadIdEliminar").val(id);
    $("#entidadTipoEliminar").val(tipo);
    $("#modalEliminarMensaje").html(mensaje);

    // 2. Muestra el modal (se requiere la librería de Bootstrap JS)
    const modalEliminar = new bootstrap.Modal(document.getElementById('modalConfirmacionEliminar'));
    modalEliminar.show();
}

// Evento para Alumno: Muestra el modal
$(document).on('click', '.btn-eliminar-alumno', function() {
    mostrarModalEliminar('alumno', this);
});

// Evento para Docente: Muestra el modal
$(document).on('click', '.btn-eliminar-docente', function() {
    mostrarModalEliminar('docente', this);
});

// Evento para Comentario: Muestra el modal
$(document).on("click", ".btn-eliminar-comentario", function() {
    mostrarModalEliminar('comentario', this);
});

// ================== LÓGICA DE CONFIRMACIÓN AJAX ==================

// Maneja la acción de eliminar cuando se hace clic en el botón "Eliminar" del modal
$("#btnConfirmarEliminar").on("click", function() {
    let id = $("#entidadIdEliminar").val();
    let tipo = $("#entidadTipoEliminar").val();
    let url = '';
    let methodType = 'POST'; // Predeterminado para alumno y docente

    // 1. Cierra el modal inmediatamente
    const modalEliminarEl = document.getElementById('modalConfirmacionEliminar');
    const modalEliminarInstance = bootstrap.Modal.getInstance(modalEliminarEl);
    modalEliminarInstance.hide();
    
    if (!id || !tipo) return;

    // 2. Determina la URL y el método HTTP
    if (tipo === 'alumno') {
        url = '/admin/eliminar-alumno/' + id;
    } else if (tipo === 'docente') {
        url = '/admin/eliminar-docente/' + id;
    } else if (tipo === 'comentario') {
        url = API_COMENTARIOS + '/' + id;
        methodType = 'DELETE'; // Usar DELETE para comentarios
    } else {
        return;
    }

    // 3. Ejecuta la petición AJAX
    $.ajax({
        url: url,
        type: methodType,
        success: function() {
            // Elimina el elemento de la vista (card para alumno/docente, row para comentario)
            if (tipo === 'alumno' || tipo === 'docente') {
                $(`.col[data-id="${id}"]`).remove();
            } else if (tipo === 'comentario') {
                $(`tr[data-id="${id}"]`).remove();
            }

            // Si se eliminó un comentario, verifica si la tabla está vacía para mostrar el mensaje
            if (tipo === 'comentario' && $("#tabla-comentarios tbody tr").length === 0) {
                 $("#tabla-comentarios tbody").html(`
                    <tr><td colspan="4" class="text-center text-muted">No hay comentarios.</td></tr>
                `);
            }
        },
        error: function() {
            alert(`Error al eliminar el ${tipo}`);
        }
    });
});

// ================== CARGA DE COMENTARIOS ==================
function cargarComentarios() {
    $.get(API_COMENTARIOS, function(data) {
        let tbody = $("#tabla-comentarios tbody");
        tbody.empty();

        if (data.length === 0) {
            tbody.append(`
                <tr><td colspan="4" class="text-center text-muted">No hay comentarios.</td></tr>
            `);
            return;
        }

        data.reverse().forEach(c => {
            let row = `
                <tr data-id="${c.id}">
                    <td>${c.nombre}</td>
                    <td>${c.texto}</td>
                    <td>${new Date(c.fecha).toLocaleString("es-PE")}</td>
                    <td>
                        <button class="btn btn-danger btn-sm btn-eliminar-comentario">Eliminar</button>
                    </td>
                </tr>
            `;
            tbody.append(row);
        });
    });
}

// Ejecutar al cargar la página
cargarComentarios();