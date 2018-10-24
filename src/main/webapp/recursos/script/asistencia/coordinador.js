$(document).ready(function() {
	
	var idAsistencia = "";
	
	//pasa datos de campos de búsqueda a campos hidden del modal para llevarlos al controller
	$('#justificacionMultipleListaBtn').on('click', function(event) { 
		$('#numeroEmpleadoHiddenListaMultiple').val($('#numeroEmpleado').val());
		$('#fechaInicialHidddenListaMultiple').val($('#validBeforeDatepicker').val());
		$('#fechaFinalHiddenListaMultiple').val($('#validAfterDatepicker').val());
		
		$('#justificacionMultipleListaBtn').submit();
	});
	
	//pasa datos de campos de búsqueda a campos hidden del modal para llevarlos al controller
	$('#justificacionMultipleGuardaBtn').on('click', function(event) { 
		$('#numeroEmpleadoHiddenGuardaMultiple').val($('#numeroEmpleado').val());
		$('#fechaInicialHidddenGuardaMultiple').val($('#validBeforeDatepicker').val());
		$('#fechaFinalHiddenGuardaMultiple').val($('#validAfterDatepicker').val());
		
		$('#justificacionMultipleGuardaBtn').submit();
	});
	
	//pasa datos de campos de búsqueda a campos hidden del modal para llevarlos al controller
	$('#formatoJustificacionMultipleBtn').on('click', function(event) { 
		$('#numeroEmpleadoHiddenGuardaMultiple').val($('#numeroEmpleado').val());
		$('#fechaInicialHidddenGuardaMultiple').val($('#validBeforeDatepicker').val());
		$('#fechaFinalHiddenGuardaMultiple').val($('#validAfterDatepicker').val());
		
		$('#formatoJustificacionMultipleBtn').submit();
	});
	

	//pobla los campos del modal de justificación
	$('.detalleBtn').on('click', function(event) { 					//botón justifica
		event.preventDefault();
		var href = $(this).attr('href');
		var text = $(this).text();
		$('#selectJustificacion').empty();
		$('#selectAutorizador').empty();
		var optionJustificacion = '<option></option>';
		var optionAutorizador = '<option></option>';
		
		$('#fechaInicial').val($('#validBeforeDatepicker').val());
		$('#fechaFinal').val($('#validAfterDatepicker').val());
		$('#numeroEmpleadoHidden').val($('#numeroEmpleado').val());
		$('#nombreHidden').val($('#nombre').val());
		$('#paternoHidden').val($('#paterno').val());
		$('#maternoHidden').val($('#materno').val());
		$('#nivelHidden').val($('#nivel').val());
		$('#tipoHidden').val($('#tipo').val());
		$('#estadoHidden').val($('#estado').val());
		$('#unidadAdministrativaHidden').val($('#unidadAdministrativa').val());
		
		$.get(href, function(asistenciaJustificacion, status) {
			var nombre = asistenciaJustificacion.asistencia.usuarioDto.nombre + ' ' + 
						asistenciaJustificacion.asistencia.usuarioDto.apellidoPaterno + ' ' + 
						asistenciaJustificacion.asistencia.usuarioDto.apellidoMaterno;
			
			$('.justificaForm #puestoModal').val(asistenciaJustificacion.asistencia.usuarioDto.idPuesto);
			$('.justificaForm #unidadModal').val(asistenciaJustificacion.asistencia.usuarioDto.nombreUnidad);
			$('.justificaForm #cve_m_usuarioModal').val(asistenciaJustificacion.asistencia.usuarioDto.claveUsuario);
			$('.justificaForm #nombreModal').val(nombre);
			$('.justificaForm #fechaIngresoModal').val(asistenciaJustificacion.asistencia.usuarioDto.fechaIngreso);
			$('.justificaForm #RFCModal').val(asistenciaJustificacion.asistencia.usuarioDto.rfc);
			$('.justificaForm #fechaModal').val(asistenciaJustificacion.asistencia.entrada);
			$('.justificaForm #tipoDiaModal').val(asistenciaJustificacion.asistencia.idTipoDia.nombre);
			$('.justificaForm #idTipoDiaModal').val(asistenciaJustificacion.asistencia.idTipoDia.idTipoDia);
			$('.justificaForm #estadoModal').val(asistenciaJustificacion.asistencia.idEstatus.estatus);
			$('.justificaForm #idAsistenciaHidden').val(asistenciaJustificacion.asistencia.idAsistencia);
			idAsistencia = asistenciaJustificacion.asistencia.idAsistencia;
			
			//select justificación
			for (var i=0; i < asistenciaJustificacion.listaJustificacion.length; i++) {
				var estatus = false;
				
				//calculando el estatus para colocarlo seleccionado en el select
				if (asistenciaJustificacion.asistencia.incidencia.justificacion.idJustificacion != null) {
					if (asistenciaJustificacion.asistencia.incidencia.justificacion.idJustificacion == asistenciaJustificacion.listaJustificacion[i].idJustificacion) {
						estatus = true;
					}
				} 
				
				optionJustificacion += '<option ';																			//apertura
				optionJustificacion += 'value="' + asistenciaJustificacion.listaJustificacion[i].idJustificacion + '" ';	//atributo value
				
				if (estatus) {
					optionJustificacion += 'selected="selected" ';
				}
				
				optionJustificacion += '">';																		//cierre apertura
				optionJustificacion += asistenciaJustificacion.listaJustificacion[i].justificacion;					//nombre a mostrar
				optionJustificacion += '</option>';																	//cierre option
			}			
			
			$('#selectJustificacion').append(optionJustificacion);
			//termina select justificación
			
			//para capturar autorizador, si no hay autorizadores coloca un text en lugar de un select
			if (asistenciaJustificacion.listaAutorizador.length > 0) {
				$('#textAutorizador').remove();
			} else if (asistenciaJustificacion.listaAutorizador.length == 0) {
				$('#selectAutorizador').remove();
			}
			
			//select autorizadores
			for (var i=0; i < asistenciaJustificacion.listaAutorizador.length; i++) {
				var jefe = false;
				
				//calculando el jefe para colocarlo seleccionado en el select
				if (asistenciaJustificacion.asistencia.usuarioDto.nombreJefe != null) {
					if (asistenciaJustificacion.asistencia.usuarioDto.nombreJefe == asistenciaJustificacion.listaAutorizador[i].nombre) {
						jefe = true;
					}
				} 
				
				optionAutorizador += '<option ';																		//apertura
				optionAutorizador += 'value="' + asistenciaJustificacion.listaAutorizador[i].nombre + '" ';		//atributo value
				
				if (jefe) {
					optionAutorizador += 'selected="selected" ';
				}
				
				optionAutorizador += '">';																			//cierre apertura
				optionAutorizador += asistenciaJustificacion.listaAutorizador[i].nombre;							//nombre a mostrar
				optionAutorizador += '</option>';																	//cierre option
			}			
			
			$('#selectAutorizador').append(optionAutorizador);
		});
		
		$('.justificaForm #justificaModal').modal();
	});
	
	//pobla los campos del modal de descuento
	$('.descuentoBtn').on('click', function(event) { 					//botón descuento
		event.preventDefault();
		var href = $(this).attr('href');
		var text = $(this).text();
		
		$('.descuentoForm #fechaInicial').val($('#validBeforeDatepicker').val());
		$('.descuentoForm #fechaFinal').val($('#validAfterDatepicker').val());
		$('.descuentoForm #numeroEmpleadoHidden').val($('#numeroEmpleado').val());
		$('.descuentoForm #nombreHidden').val($('#nombre').val());
		$('.descuentoForm #paternoHidden').val($('#paterno').val());
		$('.descuentoForm #maternoHidden').val($('#materno').val());
		$('.descuentoForm #nivelHidden').val($('#nivel').val());
		$('.descuentoForm #tipoHidden').val($('#tipo').val());
		$('.descuentoForm #estadoHidden').val($('#estado').val());
		$('.descuentoForm #unidadAdministrativaHidden').val($('#unidadAdministrativa').val());
		
		$.get(href, function(asistenciaJustificacion, status) {
			var nombre = asistenciaJustificacion.asistencia.usuarioDto.nombre + ' ' + 
						asistenciaJustificacion.asistencia.usuarioDto.apellidoPaterno + ' ' + 
						asistenciaJustificacion.asistencia.usuarioDto.apellidoMaterno;
			
			$('.descuentoForm #puestoModal').val(asistenciaJustificacion.asistencia.usuarioDto.idPuesto);
			$('.descuentoForm #unidadModal').val(asistenciaJustificacion.asistencia.usuarioDto.nombreUnidad);
			$('.descuentoForm #cve_m_usuarioModal').val(asistenciaJustificacion.asistencia.usuarioDto.claveUsuario);
			$('.descuentoForm #nombreModal').val(nombre);
			$('.descuentoForm #fechaIngresoModal').val(asistenciaJustificacion.asistencia.usuarioDto.fechaIngreso);
			$('.descuentoForm #RFCModal').val(asistenciaJustificacion.asistencia.usuarioDto.rfc);
			$('.descuentoForm #fechaModal').val(asistenciaJustificacion.asistencia.entrada);
			$('.descuentoForm #tipoDiaModal').val(asistenciaJustificacion.asistencia.idTipoDia.nombre);
			$('.descuentoForm #idTipoDiaModal').val(asistenciaJustificacion.asistencia.idTipoDia.idTipoDia);
			$('.descuentoForm #estadoModal').val(asistenciaJustificacion.asistencia.idEstatus.estatus);
			$('.descuentoForm #idAsistenciaHidden').val(asistenciaJustificacion.asistencia.idAsistencia);
			$('.descuentoForm #justificacion').val(asistenciaJustificacion.asistencia.incidencia.justificacion.justificacion);
			idAsistencia = asistenciaJustificacion.asistencia.idAsistencia;
			
		});
		
		$('.descuentoForm #justificaModal').modal();
	});
	
	//pobla los campos del modal de justifación múltiple
	$('.justificaMultipleBtn').on('click', function(event) { 					//botón justifica múltiple
		event.preventDefault();
		var href = $(this).attr('href');
		
		var val = [];
        $(':checkbox:checked').each(function(i){
          val[i] = $(this).val();
        });
		
		$('.justificaMultipleForm #fechaInicial').val($('#validBeforeDatepicker').val());
		$('.descuentoForm #fechaFinal').val($('#validAfterDatepicker').val());
		$('.descuentoForm #numeroEmpleadoHidden').val($('#numeroEmpleado').val());
		$('.descuentoForm #nombreHidden').val($('#nombre').val());
		$('.descuentoForm #paternoHidden').val($('#paterno').val());
		$('.descuentoForm #maternoHidden').val($('#materno').val());
		$('.descuentoForm #nivelHidden').val($('#nivel').val());
		$('.descuentoForm #tipoHidden').val($('#tipo').val());
		$('.descuentoForm #estadoHidden').val($('#estado').val());
		$('.descuentoForm #unidadAdministrativaHidden').val($('#unidadAdministrativa').val());
		
		$.get(href, function(asistenciaJustificacion, status) {
			var nombre = asistenciaJustificacion.asistencia.usuarioDto.nombre + ' ' + 
						asistenciaJustificacion.asistencia.usuarioDto.apellidoPaterno + ' ' + 
						asistenciaJustificacion.asistencia.usuarioDto.apellidoMaterno;
			
			$('.descuentoForm #puestoModal').val(asistenciaJustificacion.asistencia.usuarioDto.idPuesto);
			$('.descuentoForm #unidadModal').val(asistenciaJustificacion.asistencia.usuarioDto.nombreUnidad);
			$('.descuentoForm #cve_m_usuarioModal').val(asistenciaJustificacion.asistencia.usuarioDto.claveUsuario);
			$('.descuentoForm #nombreModal').val(nombre);
			$('.descuentoForm #fechaIngresoModal').val(asistenciaJustificacion.asistencia.usuarioDto.fechaIngreso);
			$('.descuentoForm #RFCModal').val(asistenciaJustificacion.asistencia.usuarioDto.rfc);
			$('.descuentoForm #fechaModal').val(asistenciaJustificacion.asistencia.entrada);
			$('.descuentoForm #tipoDiaModal').val(asistenciaJustificacion.asistencia.idTipoDia.nombre);
			$('.descuentoForm #idTipoDiaModal').val(asistenciaJustificacion.asistencia.idTipoDia.idTipoDia);
			$('.descuentoForm #estadoModal').val(asistenciaJustificacion.asistencia.idEstatus.estatus);
			$('.descuentoForm #idAsistenciaHidden').val(asistenciaJustificacion.asistencia.idAsistencia);
			$('.descuentoForm #justificacion').val(asistenciaJustificacion.asistencia.incidencia.justificacion.justificacion);
			idAsistencia = asistenciaJustificacion.asistencia.idAsistencia;
			
		});
		
		$('.descuentoForm #justificaModal').modal();
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
    $('#validBeforeDatepicker,#validAfterDatepicker').datepicker({
    	beforeShowDay: $.datepicker.noWeekends, //desactiva sábado y domingo del calendario
    	dateFormat: 'yy-mm-dd'
    });
    
    //validaciones para datepicker
    $('#buscarRango').validate({ 
        rules: {
            fechaInicial: { 
                dpCompareDate: "notAfter #validAfterDatepicker"
            },
		    fechaFinal: { 
		        dpCompareDate: "notBefore #validBeforeDatepicker"
		    } 
        },
    	messages: {
    		fechaInicial: 'Ingresa fecha menor ó igual a la Fecha Final',
    		fechaFinal: 'Ingresa fecha mayor ó igual a la Fecha Inicial'
    	}
    			
    });
    
    $("#puestoModal, #unidadModal, #cve_m_usuarioModal, #nombreModal, #fechaIngresoModal, #RFCModal, #fechaModal, #tipoDiaModal, #estadoModal, #justificacion").keydown(function (e) {
        // no permite la entrada de texto
        e.preventDefault();
    });
    
	$('#imprimir').click(function() {
		var imprime = document.getElementById('tableAsistencias');
		var wme = window.open("", "", "width=900, height=700");
		wme.document.write(imprime.outerHTML);
		wme.document.close();
		wme.focus();
		wme.print();
		wme.close();
	});
	
	//paginador
	$('#tableAsistencias').DataTable({
		"scrollY": "500px",
        "scrollCollapse": true
     });
	
	//validación para datepicker, si se selecciona uno, entonces ambos deben seleccionarse
    $('#buscaBtn').on('click', function(event) {
    	$("#validAfterDatepicker").prop('required',false);
    	$("#validBeforeDatepicker").prop('required',false);
    	
    	var existefechaInicial = $('#validBeforeDatepicker').datepicker('getDate') != null;
    	var existefechaFinal = $('#validAfterDatepicker').datepicker('getDate') != null;
    	
    	if (existefechaInicial && !existefechaFinal) {
    		$("#validAfterDatepicker").prop('required',true);
    	} else if (existefechaFinal && !existefechaInicial) {
    		$("#validBeforeDatepicker").prop('required',true);
    	} else if (existefechaInicial && existefechaFinal) {
    		$('#buscaBtn').submit();
    	}
    });
	
}); 