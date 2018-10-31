$(document).ready(function() {
	
	//pobla los campos del modal de justificación
	$('.nBtn').on('click', function(event) { 					//botón justifica
		event.preventDefault();
		var href = $(this).attr('href');
		var text = $(this).text();
		
		$('#fechaInicial').val($('#validBeforeDatepicker').val());
		$('#fechaFinal').val($('#validAfterDatepicker').val());
		$('#numeroEmpleadoHidden').val($('#numeroEmpleado').val());
		$('#nombreHidden').val($('#nombre').val());
		$('#paternoHidden').val($('#paterno').val());
		$('#maternoHidden').val($('#materno').val());
		$('#nivelHidden').val($('#nivel option:selected').val());
		$('#tipoHidden').val($('#tipo option:selected').val());
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
			$('#justificacion').val(asistenciaJustificacion.asistencia.incidencia.justificacion.justificacion);
			$('#idJustificacionHidden').val(asistenciaJustificacion.asistencia.incidencia.justificacion.idJustificacion);
		});
		
		$('.justificaForm #justificaModal').modal();
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
    
    $("#puestoModal, #unidadModal, #cve_m_usuarioModal, #nombreModal, #fechaIngresoModal, #RFCModal, #fechaModal, #tipoDiaModal, #estadoModal").keydown(function (e) {
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