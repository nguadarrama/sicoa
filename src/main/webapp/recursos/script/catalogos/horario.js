$(document).ready(function() {
	
	$('#tableHorarios').DataTable({
        "scrollY": "500px",
        "scrollCollapse": true
      });
	
	$('#tableHorarios').on('click','.eBtn' , function(event){					//botón edita
		event.preventDefault();
		var href = $(this).attr('href');
		
		
			$.get(href, function(horario, status) {
				$('#id').val(horario.idHorario);
				$('#nombre').val(horario.nombre);
				$('#timepickerEntradaEditar').val(convierte24horas(horario.horaEntrada));
				$('#timepickerSalidaEditar').val(convierte24horas(horario.horaSalida));
				
				if (horario.activo) {
					$('#activado').prop("checked", true);
				} else {
					$('#desactivado').prop("checked", true);
				}
				
				$('.horarioForm #activo').val(horario.activo);
			});
			
			$('.horarioForm #horarioModal').modal();
	});
	
	$('.nBtn').on('click', function(event) {					//botón nuevo
		$('.nuevoHorarioForm #nuevoHorarioModal').modal(); 
	});
	
	$('.eliminaBtn').on('click', function(event) {			    //botón elimina
		event.preventDefault();
		var href = $(this).attr('href');
		
		$('#eliminarModal #delRef').attr('href', href);
		$('#eliminarModal').modal();
	});
	
	$("#buscarText").on("keyup", function() {
	    var value = $(this).val().toLowerCase();
	    $("#tableHorarios tr").filter(function() {
	      $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
	    });
	  });
	
	$('.filterable .btn-filter').click(function(){
        var $panel = $(this).parents('.filterable'),
        $filters = $panel.find('.filters input'),
        $tbody = $panel.find('.table tbody');
        if ($filters.prop('disabled') == true) {
            $filters.prop('disabled', false);
            $filters.first().focus();
        } else {
            $filters.val('').prop('disabled', true);
            $tbody.find('.no-result').remove();
            $tbody.find('tr').show();
        }
    });

    $('.filterable .filters input').keyup(function(e){
        /* Ignore tab key */
        var code = e.keyCode || e.which;
        if (code == '9') return;
        /* Useful DOM data and selectors */
        var $input = $(this),
        inputContent = $input.val().toLowerCase(),
        $panel = $input.parents('.filterable'),
        column = $panel.find('.filters th').index($input.parents('th')),
        $table = $panel.find('.table'),
        $rows = $table.find('tbody tr');
        /* Dirtiest filter function ever ;) */
        var $filteredRows = $rows.filter(function(){
            var value = $(this).find('td').eq(column).text().toLowerCase();
            return value.indexOf(inputContent) === -1;
        });
        /* Clean previous no-result if exist */
        $table.find('tbody .no-result').remove();
        /* Show all rows, hide filtered ones (never do that outside of a demo ! xD) */
        $rows.show();
        $filteredRows.hide();
        /* Prepend no-result row if all rows are filtered */
        if ($filteredRows.length === $rows.length) {
            $table.find('tbody').prepend($('<tr class="no-result text-center"><td colspan="'+ $table.find('.filters th').length +'">No encontrado</td></tr>'));
        }
    });
    
    $('#timepickerEntradaEditar').timepicker({
    	timeFormat: 'HH:mm:ss',
        interval: 05,
        minTime: '06:00',
        maxTime: '18:00',
        startTime: '00:00',
        dynamic: false,
        dropdown: true,
        scrollbar: true
    });
	
    $('#timepickerSalidaEditar').timepicker({
    	timeFormat: 'HH:mm:ss',
        interval: 05,
        minTime: '12:00',
        maxTime: '22:00',
        startTime: '00:00',
        dynamic: false,
        dropdown: true,
        scrollbar: true
    });
    
    $('#timepickerEntrada').timepicker({
    	timeFormat: 'HH:mm:ss',
        interval: 05,
        minTime: '06:00',
        maxTime: '18:00',
        startTime: '00:00',
        dynamic: false,
        dropdown: true,
        scrollbar: true
    });
	
    $('#timepickerSalida').timepicker({
    	timeFormat: 'HH:mm:ss',
        interval: 05,
        minTime: '12:00',
        maxTime: '22:00',
        startTime: '00:00',
        dynamic: false,
        dropdown: true,
        scrollbar: true
    });
    
    $("#timepickerEntrada, #timepickerSalida, #timepickerEntradaEditar, #timepickerSalidaEditar").keydown(function (e) {
        // no permite la entrada de texto
        e.preventDefault();
    });
    
}); 

function convierte24horas(horaInicial) {
	var time = horaInicial;
	var hours = Number(time.match(/^(\d+)/)[1]);
	var minutes = Number(time.match(/:(\d+)/)[1]);
	var AMPM = time.match(/\s(.*)$/)[1];
	if(AMPM == "PM" && hours<12) hours = hours+12;
	if(AMPM == "AM" && hours==12) hours = hours-12;
	var sHours = hours.toString();
	var sMinutes = minutes.toString();
	if(hours<10) sHours = "0" + sHours;
	if(minutes<10) sMinutes = "0" + sMinutes;
	
	return sHours + ":" + sMinutes + ":00";
}