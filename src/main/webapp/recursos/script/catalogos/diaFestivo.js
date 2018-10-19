$(document).ready(function() {
	$("#fecha").datepicker({
		format: 'dd-MM-yyyy',
		minDate: 1,
		beforeShowDay: $.datepicker.noWeekends
   });
	
	$('#tableDiaFestivo').DataTable({
        "scrollY": "500px",
        "scrollCollapse": true
      });
	
	$('.eBtn').on('click', function(event) { 					//botón edita
		event.preventDefault();
		var href = $(this).attr('href');
		var text = $(this).text();
		
			$.get(href, function(diaFestivo, status) {
				$('#id').val(diaFestivo.idDiaFestivo);
				$('#nombre').val(diaFestivo.nombre);
//				$("#fecha").datepicker({
//					format: 'dd-MM-yyyy',
//					minDate: 1,
//					beforeShowDay: $.datepicker.noWeekends 
//			   });
				$('#fecha').val(diaFestivo.fecha);
				if (diaFestivo.activo) {
					$('#activado').prop("checked", true);
				} else {
					$('#desactivado').prop("checked", true);
				}
				
				$('.diaFestivoForm #activo').val(diaFestivo.activo);
			});
			
			$('.diaFestivoForm #diaFestivoModal').modal();
	});
	
	
	
	$('.nBtn').on('click', function(event) {					//botón nuevo
		$('.nuevodiaFestivoForm #nuevodiaFestivoModal').modal(); 
	});
	
    
}); 
