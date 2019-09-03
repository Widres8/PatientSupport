$(document).ready(function() {

    $('#locales').on('click', 'a.i18n', function(e) {
        var parameterModel = $(this).data('lang');

        // http://localhost:8080/countries?lang=en?lang=es

        var url = window.location.href;

        if(url.indexOf('?lang=')>0)
        {
            url = url.split('?')[0];
        }

        window.location.replace(url + '?lang=' + parameterModel);        
    });
});