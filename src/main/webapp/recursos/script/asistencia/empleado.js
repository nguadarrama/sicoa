$(document).ready(function() {
	
	//pobla los campos del modal de justificación
	$('.nBtn').on('click', function(event) { 					//botón justifica
		event.preventDefault();
		var href = $(this).attr('href');
		
		$('#fechaInicial').val($('#validBeforeDatepicker').val());
		$('#fechaFinal').val($('#validAfterDatepicker').val());
		
		$.get(href, function(asistenciaJustificacion, status) {
			var nombre = asistenciaJustificacion.asistencia.usuarioDto.nombre + ' ' + 
			asistenciaJustificacion.asistencia.usuarioDto.apellidoPaterno + ' ' + 
			asistenciaJustificacion.asistencia.usuarioDto.apellidoMaterno;
			
			var fecha = new Date(asistenciaJustificacion.asistencia.entrada);
			var dia = ("0" + fecha.getDate()).slice(-2);
			var mes = ("0" + (fecha.getMonth() + 1)).slice(-2);
			var anio = fecha.getFullYear();
			
			$('.justificaForm #id').val(asistenciaJustificacion.asistencia.idAsistencia);
			$('.justificaForm #puesto').val(asistenciaJustificacion.asistencia.usuarioDto.idPuesto);
			var aux1 = asistenciaJustificacion.asistencia.usuarioDto.nombreUnidad;
			aux1 = aux1.replace("Ã“", "Ó");
			aux1 = aux1.replace("Ã‰", "É");
			aux1 = aux1.replace("Ãš", "Ú");
			aux1 = aux1.replace("Ã", "Í");
			$('.justificaForm #unidad').val(aux1);
			$('.justificaForm #cve_m_usuario').val(asistenciaJustificacion.asistencia.usuarioDto.claveUsuario);
			$('.justificaForm #nombre').val(nombre);
			$('.justificaForm #fechaIngreso').val(asistenciaJustificacion.asistencia.usuarioDto.fechaIngreso);
			$('.justificaForm #RFC').val(asistenciaJustificacion.asistencia.usuarioDto.rfc);
			$('.justificaForm #fecha').val(dia + '-' + mes + '-' + anio);
			var aux = asistenciaJustificacion.asistencia.idTipoDia.nombre;
			aux = aux.replace("Ã³", "ó");
			aux = aux.replace("Ã©", "é");
			aux = aux.replace("Ã¡", "á");
			aux = aux.replace("Ã­", "í");
			$('.justificaForm #tipoDia').val(aux);
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
    	$("#validAfterDatepicker").prop('required',true);
    	$("#validBeforeDatepicker").prop('required',true);
    	
    	var existefechaInicial = $('#validBeforeDatepicker').datepicker('getDate') != null;
    	var existefechaFinal = $('#validAfterDatepicker').datepicker('getDate') != null;
    	
    	if (existefechaInicial && existefechaFinal) {
    		$('#buscaBtn').submit();
    	}
    });
	
}); 