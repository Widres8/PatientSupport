$(document).ready(function () {
    $("#countryId").change(function () {
        getZones($(this).val());
    });
    $("#departmentId, #treatmentDepartmentId").change(function () {
        getCities($(this).val());
    });
    $("#statusId").change(function () {
        getStatusReason($(this).val());
    });
});

function getZones(countryId) {
    $('.block').css('display', 'flex');
    $("#zoneId").empty();
    $("#zoneId").append('<option value="0">Seleccione una opción</option>');
    $.ajax({
        type: "POST",
        url: '/generics/getZonesByCountryId',
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
    $("#cityId, #treatmentCityId").empty();
    $("#cityId, #treatmentCityId").append('<option value="0">Seleccione una opción</option>');
    $.ajax({
        type: "POST",
        url: '/generics/getCitiesByDepartmentId',
        dataType: 'json',
        data: {departmentId: departmentId},
        success: function (data) {
            $.each(data, function (i, data) {
                $("#cityId, #treatmentCityId").append('<option value="'
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

function getStatusReason(statusId) {
    $('.block').css('display', 'flex');
    $("#statusReasonId").empty();
    $("#statusReasonId").append('<option value="0">Seleccione una opción</option>');
    $.ajax({
        type: "POST",
        url: '/generics/getStatusReasonByStatusId',
        dataType: 'json',
        data: {statusId: statusId},
        success: function (data) {
            $.each(data, function (i, data) {
                $("#statusReasonId").append('<option value="'
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