$(document).ready(function() {

	//pobla los campos del modal de justificación
	$('.nBtn').on('click', function(event) { 					//botón justifica
		event.preventDefault();
		var href = $(this).attr('href');
		
		$('#selectJustificacion').empty();
		var option = '<option>-- Selecciona --</option>';
		
		$('#fechaInicial').val($('#validBeforeDatepicker').val());
		$('#fechaFinal').val($('#validAfterDatepicker').val());
		
		$.get(href, function(asistenciaJustificacion, status) {
			$('.justificaForm #id').val(asistenciaJustificacion.asistencia.idAsistencia);
			$('.justificaForm #fecha').val(asistenciaJustificacion.asistencia.entrada);
			$('.justificaForm #tipoDia').val(asistenciaJustificacion.asistencia.idTipoDia.nombre);
			$('.justificaForm #idTipoDia').val(asistenciaJustificacion.asistencia.idTipoDia.idTipoDia);
			
			//select justificación
			for (var i=0; i < asistenciaJustificacion.listaJustificacion.length; i++) {
				var estatus = false;
				
				//calculando el estatus para colocarlo seleccionado en el select
				if (asistenciaJustificacion.asistencia.incidencia.justificacion.idJustificacion != null) {
					if (asistenciaJustificacion.asistencia.incidencia.justificacion.idJustificacion == asistenciaJustificacion.listaJustificacion[i].idJustificacion) {
						estatus = true;
					}
				} 
				
				option += '<option ';																	//apertura
				option += 'value="' + asistenciaJustificacion.listaJustificacion[i].idJustificacion + '" ';	//atributo value
				
				if (estatus) {
					option += 'selected="selected" ';
				}
				
				option += '">';																			//cierre apertura
				option += asistenciaJustificacion.listaJustificacion[i].justificacion;					//nombre a mostrar
				option += '</option>';																	//cierre option
			}			
			
			$('#selectJustificacion').append(option);
		});
		
		$('.justificaForm #justificaModal').modal();
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
      
    //filtro
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
    
    //fechas datepicker
    $('#validBeforeDatepicker,#validAfterDatepicker, #fecha').datepicker({
    	beforeShowDay: $.datepicker.noWeekends //desactiva sábado y domingo del calendario
    });
    
    //validaciones para datepicker
    $('#buscarRango').validate({ 
        rules: { 
            fechaInicial: { 
                required: true, 
                dpCompareDate: "notAfter #validAfterDatepicker"
            },
		    fechaFinal: { 
		        required: true, 
		        dpCompareDate: "notBefore #validBeforeDatepicker"
		    } 
        },
    	messages: {
    		fechaInicial: 'Debe ser menor que la "Fecha Final"',
    		fechaFinal: 'Debe ser mayor que la "Fecha Inicial"'
    	}
    			
    });
	
}); 