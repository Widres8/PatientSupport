var Table;

$(document).ready(function () {

    Table = $('#myDatatable').DataTable({
        responsive: true,
        "language": {
            "url": "/js/custom/datatables/Spanish.json",
            buttons: {
                pageLength: {
                    _: "Ver %d Elementos",
                    '-1': "Ver Todos"
                }
            },
            select: {
                //rows: {
                //    _: "Ha seleccionado %d fila",
                //    0: "Haz click en una fila para seleccionarla"
                //}
            },
            search: ' ',
            searchPlaceholder: "Buscar..."
        },
        lengthMenu: [
            [5, 10, 25, 50, -1],
            ['5', '10', '25', '50', 'Ver Todos']
        ],
        dom: 'Bfrtip',
        stateSave: false,
        buttons: [
            {
                extend: 'pageLength',
                text: 'Ver Registros',
                titleAttr: 'Filas'
            },
            {
                extend: 'colvis',
                text: 'Columnas',
                postfixButtons: ['colvisRestore'],
                columns: ':not(.noVis)'
            },
            {
                extend: 'copyHtml5',
                text: '<i class="fa fa-copy"></i>',
                titleAttr: 'Copiar'
            },
            {
                extend: 'excelHtml5',
                text: '<i class="fa fa-file-excel"></i>',
                titleAttr: 'Excel'
            },
            // {
            //     extend: 'csvHtml5',
            //     text: '<i class="fa fa-file-excel"></i>',
            //     titleAttr: 'CSV'
            // },
            {
                extend: 'pdfHtml5',
                text: '<i class="fa fa-file-pdf"></i>',
                titleAttr: 'PDF',
                orientation: orientationPdf,
                pageSize: pageSizePdf,
                exportOptions: {
                    columns: columnsPdf
                }
            },
            {
                extend: 'print',
                text: '<i class="fa fa-print"></i>',
                titleAttr: 'Imprimir'
            },
        ],
    });//Fin table

    //hide columns in datatable
    Table.columns(columnsHide).visible(false, false);
});