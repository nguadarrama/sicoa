$(document).ready(function() {
	
	$("#tipo").click(function() {
		if(!$('#tipo').val()) { 
			$("#checkVacacion").attr("disabled", false);
			$("#checkComision").attr("disabled", false);
			$("#checkLicencia").attr("disabled", false);
			$("#checkDescuento").attr("disabled", false);
		} else {
			$("#checkVacacion").attr("disabled", true);
			$("#checkComision").attr("disabled", true);
			$("#checkLicencia").attr("disabled", true);
			$("#checkDescuento").attr("disabled", true);
		}
	});
	
	$("#checkVacacion").click(function() {
		if ($("#checkVacacion").is(':checked') || 
			$("#checkComision").is(':checked') || 
			$("#checkLicencia").is(':checked') || 
			$("#checkDescuento").is(':checked')) {
			
    		$("#tipo").attr("disabled", true);
		} else {
			$("#tipo").attr("disabled", false);
		}
	});
	
	$("#checkComision").click(function() {
		if ($("#checkVacacion").is(':checked') ||
			$("#checkComision").is(':checked') || 
			$("#checkLicencia").is(':checked') || 
			$("#checkDescuento").is(':checked')) {
			
    		$("#tipo").attr("disabled", true);
		} else {
			$("#tipo").attr("disabled", false);
		}
	});
	
	$("#checkLicencia").click(function() {
		if ($("#checkVacacion").is(':checked') ||
			$("#checkComision").is(':checked') || 
			$("#checkLicencia").is(':checked') || 
			$("#checkDescuento").is(':checked')) {
			
    		$("#tipo").attr("disabled", true);
		} else {
			$("#tipo").attr("disabled", false);
		}
	});
	
	$("#checkDescuento").click(function() {
		if ($("#checkVacacion").is(':checked') ||
			$("#checkComision").is(':checked') || 
			$("#checkLicencia").is(':checked') || 
			$("#checkDescuento").is(':checked')) {
			
    		$("#tipo").attr("disabled", true);
		} else {
			$("#tipo").attr("disabled", false);
		}
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