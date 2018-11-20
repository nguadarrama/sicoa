$(document).ready(function() {
	
	$("#fecha").datepicker({
		format: 'dd-MM-yyyy',
		beforeShowDay: $.datepicker.noWeekends,
		minDate: '-0D',
		maxDate: '12M',
		zIndexOffset: 100000,
		beforeShow: function () {
                var $datePicker = $("#fecha");
                var zIndexModal = $datePicker.closest(".modal").css("z-index");
                $datePicker.css("z-index", zIndexModal + 1);
            }
        }).attr('required', 'required').
            keypress(function (event) {
                if (event.keyCode == 8) {
                    event.preventDefault();
                }
            });
	
	
	
	$('#tableDiaFestivo').DataTable({
        "scrollY": "500px",
        "scrollCollapse": true
      });
	
	$('#tableDiaFestivo').on('click','.eBtn' , function(event){			//botón edita
		event.preventDefault();
		var href = $(this).attr('href');
		
			$.get(href, function(diaFestivo, status) {
				$('#id').val(diaFestivo.idDiaFestivo);
				$('#nombre').val(diaFestivo.nombre);
				var d = new Date(diaFestivo.fecha);
				$("#fechaM").datepicker({
				format: 'dd-MM-yyyy',
				beforeShowDay: $.datepicker.noWeekends,
				minDate: '-0D',
				maxDate: '12M',
				zIndexOffset: 100000,
				setDate: diaFestivo.fecha,
				beforeShow: function () {
		                var $datePicker = $("#fechaM");
		                var zIndexModal = $datePicker.closest(".modal").css("z-index");
		                $datePicker.css("z-index", zIndexModal + 1);
		            }
		        }).attr('readonly', 'true').
		            keypress(function (event) {
		                if (event.keyCode == 8) {
		                    event.preventDefault();
		                }
		            });
				$('#fechaM').datepicker('setDate', d);
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
