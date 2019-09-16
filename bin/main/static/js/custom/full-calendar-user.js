var events = [];
var idEvent;
var selectedEvent = null;

$(document).ready(function() {

    getEvents();

    $('#createForm').submit(function(event) {
        event.preventDefault();
        createEventPost();
        
    });

    $('#createModal').on('hidden.bs.modal', function (e) {
        $('#createForm').trigger("reset");
    });
});


function createEventPost() {
    var itemToCreate = {
        subject: $('#subject').val(),
        startDate: new Date($('#startDate').val()),
        endDate: new Date($('#endDate').val()),
        isFullDay: $('#isFullDay').is(":checked") ? true : false,
        color: $('#color').val(),
        observations: $('#observations').val(),
        eventTypeId: $('#eventtypeid').val(),
    };

    $.ajax({
        type: 'POST',
        url: '/events/create',
        contentType:"application/json",
        datatype: 'json',
        data: JSON.stringify(itemToCreate),
        success: function (data) {
            if (data.Status == 200) {
                swal("Ok", data.Message, "success");
                setInterval('location.reload()', 1000);
            } else {
                swal("¡Error!", data.Error, "error");
            }   
        }
    });
    
}

function createEvent(startDate) {
    $('#isCreate').show();
    $('#isEdit').hide();
    $('#editEvenClick').hide();
    $('#createEvenClick').show();
    $('#startDate').val(startDate);
    $('#createModal ').modal();
}

function editEvent(event) {
    $('#isCreate').hide();
    $('#isEdit').show();
    $('#editEvenClick').show();
    $('#createEvenClick').hide();

    event.allDay ?  $('#isFullDay').attr('checked','checked') : $('#isFullDay').attr('checked','');
    $('#subject').val(event.title);
    $('#startDate').val(event.start);
    $('#endDate').val(event.end);
    $('#color').val(event.color);
    $('#observations').val(event.description);
    $('#eventtypeid').val(event.eventTypeId);
    $('#createModal').modal();
}
function getEvents() {
    $.ajax({
        type: 'GET',
        url: '/events/getEvents',
        contentType:"application/json",
        datatype: 'json',
        data: null,
        success: function (data) {
            $.each(data, function (i, v) {
                events.push({
                    id: v.id,
                    eventTypeId: v.eventTypeId,
                    title: v.subject,
                    description: v.observations,
                    start: moment(v.startDate),
                    end: v.endDate != null ? moment(v.endDate) : null,
                    color: v.color,
                    allDay: v.isFullDay
                });
            });

            generateCalender(events);
        },
        error: function (error) {
            alert('failed');
        }
    });
}

function generateCalender(events) {

    $('#calender').fullCalendar({
        contentHeight: 500,
        defaultDate: new Date(),
        timeFormat: 'h(:mm)a',
        header: {
            left: 'title',
            center: 'prev,next today',
            right: 'month,basicWeek,basicDay,agenda'
        },
        eventLimit: true,
        eventColor: '#378006',
        locale: 'es',
        events: events,
        eventRender: function(eventObj, $el) {
            $el.popover({
              title: eventObj.title,
              content: eventObj.description,
              trigger: 'hover',
              placement: 'top',
              container: 'body'
            });
        },
        dayClick: function(date, jsEvent, view) {

            // alert('Clicked on: ' + date.format());
            // alert('Coordinates: ' + jsEvent.pageX + ',' + jsEvent.pageY);
            // alert('Current view: ' + view.name);
            // // change the day's background color just for fun
            // $(this).css('background-color', 'red');
            createEvent(date.format());
        },
        eventClick: function (calEvent, jsEvent, view) {
            editEvent(calEvent);
            // $('#myModal #eventTitle').text(calEvent.title);
            // var $description = $('<div/>');
            // $description.append($('<p/>').html('<b>Sede: </b>' + calEvent.sede));
            // $description.append($('<p/>').html('<b>Dirección Evento: </b>' + calEvent.address));
            // $description.append($('<p/>').html('<b>Descripción: </b>' + calEvent.description));
            // $description.append($('<p/>').html('<b>Inicio: </b>' + calEvent.start.format("DD-MMM-YYYY HH:mm a")));
            // if (calEvent.end != null) {
            //     $description.append($('<p/>').html('<b>Fin: </b>' + calEvent.end.format("DD-MMM-YYYY HH:mm a")));
            // }
            // idEvent = calEvent.eventID;

            // $('#myModal #pDetails').empty().html($description);

            // $('#myModal').modal();
        },
        selectable: false,
        editable: false,
        buttonText: {
            prev: "Ant",
            next: "Sig",
            today: "Hoy",
            month: "Mes",
            week: "Semana",
            day: "Día",
            list: "Agenda"
        },
        weekLabel: "Sm",
        allDayHtml: "Todo<br/>el día",
        eventLimitText: "más",
        noEventsMessage: "No hay eventos para mostrar",
    });    
}
