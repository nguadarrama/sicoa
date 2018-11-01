$(document).ready(function() {
	
	//pobla los campos del modal de justificación
	$('.nBtn').on('click', function(event) { 					//botón justifica
		event.preventDefault();
		var href = $(this).attr('href');
		var text = $(this).text();
		
		$('#fechaInicial').val($('#validBeforeDatepicker').val());
		$('#fechaFinal').val($('#validAfterDatepicker').val());
		
		$.get(href, function(asistenciaJustificacion, status) {
			var nombre = asistenciaJustificacion.asistencia.usuarioDto.nombre + ' ' + 
			asistenciaJustificacion.asistencia.usuarioDto.apellidoPaterno + ' ' + 
			asistenciaJustificacion.asistencia.usuarioDto.apellidoMaterno;
			
			$('.justificaForm #id').val(asistenciaJustificacion.asistencia.idAsistencia);
			$('.justificaForm #puesto').val(asistenciaJustificacion.asistencia.usuarioDto.idPuesto);
			$('.justificaForm #unidad').val(asistenciaJustificacion.asistencia.usuarioDto.nombreUnidad);
			$('.justificaForm #cve_m_usuario').val(asistenciaJustificacion.asistencia.usuarioDto.claveUsuario);
			$('.justificaForm #nombre').val(nombre);
			$('.justificaForm #fechaIngreso').val(asistenciaJustificacion.asistencia.usuarioDto.fechaIngreso);
			$('.justificaForm #RFC').val(asistenciaJustificacion.asistencia.usuarioDto.rfc);
			$('.justificaForm #fecha').val(asistenciaJustificacion.asistencia.entrada);
			$('.justificaForm #tipoDia').val(asistenciaJustificacion.asistencia.idTipoDia.nombre);
			$('.justificaForm #idTipoDia').val(asistenciaJustificacion.asistencia.idTipoDia.idTipoDia);
			$('.justificaForm #estado').val(asistenciaJustificacion.asistencia.idEstatus.estatus);
			$('#justificacion').val(asistenciaJustificacion.asistencia.incidencia.justificacion.justificacion);
		});
		
		$('.justificaForm #justificaModal').modal();
	});
    
    //fechas datepicker
    $('#validAfterDatepicker, #fecha').datepicker({
    	beforeShowDay: $.datepicker.noWeekends, //desactiva sábado y domingo del calendario
    	dateFormat: 'yy-mm-dd',
    	onSelect: function() 
	       { 
    		var maxDate = $('#validAfterDatepicker').datepicker('getDate');
	    		$("#validBeforeDatepicker").datepicker("change", { maxDate: maxDate });
	       },
    	
    });
    
    $('#validBeforeDatepicker, #fecha').datepicker({
    	beforeShowDay: $.datepicker.noWeekends, //desactiva sábado y domingo del calendario
    	dateFormat: 'yy-mm-dd',
    	onSelect: function() 
	       { 
    		var minDate = $('#validBeforeDatepicker').datepicker('getDate');
	    		$("#validAfterDatepicker").datepicker("change", { minDate: minDate });
	       },
    	
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
    
    $("#puesto, #unidad, #cve_m_usuario, #nombre, #fechaIngreso, #RFC, #fecha, #tipoDia, #estado, #justificacion").keydown(function (e) {
        // no permite la entrada de texto
        e.preventDefault();
    });
    
    $('#imprimir').click(function() {
		var contenidoTabla = document.getElementById('tablaImprimir');
		contenidoTabla.border = 2;
		contenidoTabla.hidden = false;
		var wme = window.open("");
		wme.document.write(contenidoTabla.outerHTML);
		contenidoTabla.hidden = true;
		wme.document.close();
		wme.focus();
		wme.print();
		wme.close();
	});
    
    //agrega paginador
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