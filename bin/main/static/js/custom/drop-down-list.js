$(document).ready(function () {
    $("#countryId").change(function () {
        getZones($(this).val());
    });
    $("#departmentId").change(function () {
        getCities($(this).val());
    });
});

function getZones(countryId) {
    $('.block').css('display', 'flex');
    $("#zoneId").empty();
    $("#zoneId").append('<option value="0">Seleccione una opción</option>');
    $.ajax({
        type: "POST",
        url: '/departments/getZonesByCountryId',
        dataType: 'json',
        data: {countryId: countryId},
        success: function (data) {
            $.each(data, function (i, data) {
                $("#zoneId").append('<option value="'
                    + data.id + '">'
                    + data.description + '</option>');
            });
            $('.block').css('display', 'none');
        },
        error: function (ex) {
            swal("Error", 'Fallo al cargar las opciones.' + ex.statusText, "error");
        }
    });
}

function getCities(departmentId) {
    $('.block').css('display', 'flex');
    $("#cityId").empty();
    $("#cityId").append('<option value="0">Seleccione una opción</option>');
    $.ajax({
        type: "POST",
        url: '/patients/getCitiesByDepartmentId',
        dataType: 'json',
        data: {departmentId: departmentId},
        success: function (data) {
            $.each(data, function (i, data) {
                $("#cityId").append('<option value="'
                    + data.id + '">'
                    + data.description + '</option>');
            });
            $('.block').css('display', 'none');
        },
        error: function (ex) {
            swal("Error", 'Fallo al cargar las opciones.' + ex.statusText, "error");
        }
    });
}