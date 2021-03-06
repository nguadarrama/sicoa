$(document).ready(function() {
	
	//pobla los campos del modal de justificación
	$('.nBtn').on('click', function(event) { 					//botón justifica
		event.preventDefault();
		var href = $(this).attr('href');
		
		$('#fechaInicial').val($('#validBeforeDatepicker').val());
		$('#fechaFinal').val($('#validAfterDatepicker').val());
		$('#numeroEmpleadoHidden').val($('#numeroEmpleado').val());
		$('#nombreHidden').val($('#nombre').val());
		$('#paternoHidden').val($('#paterno').val());
		$('#maternoHidden').val($('#materno').val());
		$('#nivelHidden').val($('#nivel option:selected').val());
		$('#tipoHidden').val($('#tipo option:selected').val());
		$('#estadoHidden').val($('#estado option:selected').val());
		$('#unidadAdministrativaHidden').val($('#unidadAdministrativa').val());
		
		$.get(href, function(asistenciaJustificacion, status) {
			var nombre = asistenciaJustificacion.asistencia.usuarioDto.nombre + ' ' + 
			asistenciaJustificacion.asistencia.usuarioDto.apellidoPaterno + ' ' + 
			asistenciaJustificacion.asistencia.usuarioDto.apellidoMaterno;
			
			var fecha = new Date(asistenciaJustificacion.asistencia.entrada);
			var dia = ("0" + fecha.getDate()).slice(-2);
			var mes = ("0" + (fecha.getMonth() + 1)).slice(-2);
			var anio = fecha.getFullYear();

			$('.justificaForm #puestoModal').val(asistenciaJustificacion.asistencia.usuarioDto.idPuesto);
			var aux1 = asistenciaJustificacion.asistencia.usuarioDto.nombreUnidad;
			aux1 = aux1.replace("Ã“", "Ó");
			aux1 = aux1.replace("Ã‰", "É");
			aux1 = aux1.replace("Ãš", "Ú");
			aux1 = aux1.replace("Ã", "Í");
			$('.justificaForm #unidadModal').val(aux1);
			$('.justificaForm #cve_m_usuarioModal').val(asistenciaJustificacion.asistencia.usuarioDto.claveUsuario);
			$('.justificaForm #nombreModal').val(nombre);
			$('.justificaForm #fechaIngresoModal').val(asistenciaJustificacion.asistencia.usuarioDto.fechaIngreso);
			$('.justificaForm #RFCModal').val(asistenciaJustificacion.asistencia.usuarioDto.rfc);
			$('.justificaForm #fechaModal').val(dia + '-' + mes + '-' + anio);
			var aux = asistenciaJustificacion.asistencia.idTipoDia.nombre;
			aux = aux.replace("Ã³", "ó");
			aux = aux.replace("Ã©", "é");
			aux = aux.replace("Ã¡", "á");
			aux = aux.replace("Ã­", "í");
			$('.justificaForm #tipoDiaModal').val(aux);
			$('.justificaForm #idTipoDiaModal').val(asistenciaJustificacion.asistencia.idTipoDia.idTipoDia);
			$('.justificaForm #estadoModal').val(asistenciaJustificacion.asistencia.idEstatus.estatus);
			$('.justificaForm #idAsistenciaHidden').val(asistenciaJustificacion.asistencia.idAsistencia);
			$('#justificacion').val(asistenciaJustificacion.asistencia.incidencia.justificacion.justificacion);
			$('#idJustificacionHidden').val(asistenciaJustificacion.asistencia.incidencia.justificacion.idJustificacion);
		});
		
		$('.justificaForm #justificaModal').modal();
	});
	
	//fecha final
    $('#validAfterDatepicker, #fecha').datepicker({
    	beforeShowDay: $.datepicker.noWeekends, //desactiva sábado y domingo del calendario
    	dateFormat: 'yy-mm-dd',
    	onSelect: function() 
	       { 
    		var maxDate = $('#validAfterDatepicker').datepicker('getDate');
    		var minDate = $('#validAfterDatepicker').datepicker('getDate');
    		minDate.setMonth(maxDate.getMonth() - 3);
    		$("#validBeforeDatepicker").datepicker("change", { minDate: minDate });
    		$("#validBeforeDatepicker").datepicker("change", { maxDate: maxDate });
	       },
    });
    
    //fecha inicial
    $('#validBeforeDatepicker, #fecha').datepicker({
    	beforeShowDay: $.datepicker.noWeekends, //desactiva sábado y domingo del calendario
    	dateFormat: 'yy-mm-dd',
    	onSelect: function() 
	       { 
    		var minDate = $('#validBeforeDatepicker').datepicker('getDate');
    		var maxDate = $('#validBeforeDatepicker').datepicker('getDate');
    		maxDate.setMonth(maxDate.getMonth() + 3);
    		
    		$("#validAfterDatepicker").datepicker("change", { minDate: minDate });
    		$("#validAfterDatepicker").datepicker("change", { maxDate: maxDate });
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
    	$("#validAfterDatepicker").prop('required',true);
		$("#validBeforeDatepicker").prop('required',true);
    	
    	var existefechaInicial = $('#validBeforeDatepicker').datepicker('getDate') != null;
    	var existefechaFinal = $('#validAfterDatepicker').datepicker('getDate') != null;
    	
    	 if (existefechaInicial && existefechaFinal) {
    		$('#buscaBtn').submit();
    	}
    });
	
}); 