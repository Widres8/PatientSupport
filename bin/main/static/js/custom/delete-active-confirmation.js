$(document).ready(function() {
    //Events with danger --Delete --
    //Parameters Swal Alert button delete/active
    var titleSwal = "¿Desea Eliminar el registro?";
    var textSwal = "Este paso eliminara el registro del sistema";
    var typeSwal = "warning";

    $('.tablecontainer').on('click', 'button.swalDelete', function(e) {
        var parameterModel = $(this).data('id');
        var urlAjaxSwal = $(this).data('url');

        swal({
            title: titleSwal,
            text: textSwal,
            icon: typeSwal,
            buttons: {
                close: {
                    text: "Cerrar",
                    value: "Cancelar",
                    className: "btn-info"
                },
                delete: {
                    text: "Eliminar",
                    value: "Delete",
                    className: "btn-danger"
                }
            }
        }).then((value) => {
            if (value === "Delete") {
                $.ajax({
                    type: "DELETE",
                    url: urlAjaxSwal + parameterModel,
                    success: function(data) {
                        if (data.Status == 200) {
                            swal("Ok", data.Message, "success");
                            setInterval('location.reload()', 1000);
                        } else {
                            swal("¡Error!", data.Error, "error");
                        }
                    }
                }); //Ajax
            }
        });
    });

    //Active control
    var titleSwalActive = "¿Desea Activar el registro?";
    var textSwalActive = "Este paso habilitara nuevamente el registro en el sistema";
    var typeSwalActive = "info";
    var textButton = "Activar";
    var textclassName = "btn-success";

    $('.tablecontainer').on('click', 'button.swalActive', function(e) {
        var parameterModel = $(this).data('id');
        var urlAjaxSwalActive = $(this).data('url');
        var active = $(this).data('active');

        if (active == 1) {
            titleSwalActive = "¿Desea Desactivar el registro?";
            textSwalActive = "Este paso deshabilitara nuevamente el registro en el sistema";
            typeSwalActive = "warning";
            textButton = "Desactivar";
            textclassName = "btn-warning";
        }

        swal({
            title: titleSwalActive,
            text: textSwalActive,
            icon: typeSwalActive,
            buttons: {
                close: {
                    text: "Cerrar",
                    value: "Cancelar",
                    className: "btn-info"
                },
                active: {
                    text: textButton,
                    value: "Active",
                    className: textclassName
                },
            }
        }).then((value) => {
            if (value === "Active") {
                $.ajax({
                    type: "POST",
                    url: urlAjaxSwalActive + parameterModel,
                    data: { '_token': $('input[name=_token]').val(), },
                    success: function(data) {
                        if (data.Status == 200) {
                            swal("Ok", data.Message, "success");
                            setInterval('location.reload()', 1000);
                        } else {
                            swal("¡Error!", data.Message, "error");
                        }
                    }
                }); //Ajax
            }
        });
    });
});