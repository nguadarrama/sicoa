$(document).ready(function() {
	
	$('.collapse').collapse();
//	$('#collapseOne').collapse({
//		  toggle: false
//		})
		
		
		$("#btnVacacionesPropias").on('click', function(event){
			
		
			
		});
		$("#btnVacacionesEmpleados").on('click', function(event){
			
			
		});
		
		$("#fechaInicioBusca1").datepicker({
				minDate: 1,
				dateFormat: 'yy-mm-dd',
		       beforeShowDay: $.datepicker.noWeekends 
		   });
		$("#fechaFinBusca1").datepicker({ 
			minDate: 1,
			dateFormat: 'yy-mm-dd',
		       beforeShowDay: $.datepicker.noWeekends 
		   });
		$("#fechaInicio").datepicker({ 
			minDate: 1,
		       beforeShowDay: $.datepicker.noWeekends,
		   });
		$("#fechaFin").datepicker({ 
			minDate: 1,
		       beforeShowDay: $.datepicker.noWeekends ,
		       onSelect: function() 
		       { 
		    	   calcularDias();
		       },
		   });
		
		function cambiarFile(){
		    const input = document.getElementById('inputFileServer');
		    if(input.files && input.files[0])
		        alert("File Seleccionado : ", input.files[0]);
		    
		}
		
		function diasLibres(dateFrom, dateTo) {
			  var from = moment(dateFrom, 'DD/MM/YYY'),
			    to = moment(dateTo, 'DD/MM/YYY'),
			    days = 0;
			    
			  while (!from.isAfter(to)) {
			    // Si no es sabado ni domingo
			    if (from.isoWeekday() !== 6 && from.isoWeekday() !== 7) {
			      days++;
			    }
			    from.add(1, 'days');
			  }
			  return days;
			}
		function calcularDias(){
			var fechaInicio= $("#fechaInicio").val();
			var x = fechaInicio.split("/");
			fechaInicio = x[0] + "-" + x[1] + "-" + x[2];
			var fechaFin=$("#fechaFin").val();
			var y = fechaFin.split("/");
			fechaFin = y[0] + "-" + y[1] + "-" + y[2];
			var resultado = diasLibres(fechaInicio,fechaFin);
			//var dias=diasEntreFechas();
			//resultado+=1;
			//alert ("fechaInicio "+fechaInicio+" fechaFin "+fechaFin+" resultado "+resultado);
			if(resultado>=0 && resultado<=10){
				//alert("bien ");
				var diasAutorizados=$("#diasDispobibles").val();
				//alert("Dias disponibles "+diasAutorizados);
				if(diasAutorizados>=resultado){
				$("#diasPorPedir").val(resultado);
				}else{
					alert("Error, los días no deben pasar del tope "+diasAutorizados);
				}
			}else{
				alert("El número de días no es permitido");
			}
			
		};
		$('#guardarVacaciones').on('click', function(event){
			
		});
		$('.aceptaBtn').on('click', function(event) {					//botón nuevo
			event.preventDefault();
			var href = $(this).attr('href');
			
			$('#aceptarModal #aceptaModal').attr('href', href);
			$('#aceptarModal').modal(); 
		});
		
//		$('.rechazaBtn').on('click', function(event) {					//botón nuevo
//			$('#rechazarModal').modal();
//			
//		});
		
		$('.rechazaBtn').on('click', function(event) {			    //botón elimina
			event.preventDefault();
			var href = $(this).attr('href');
			$('#rechazarModal #rechazaModal').attr('href', href);
			$('#rechazarModal').modal();
		});
		
//		$('.aceptaModal').on('click', function(event) {			    //botón elimina
//			event.preventDefault();
//			var href = $(this).attr('href');
//			
//			$('#aceptarModal #aceptaModal').attr('href', href);
//			$('#aceptarModal').modal();
//		});
		
	/*$('.eBtn').on('click', function(event) { 					//botón edita
		event.preventDefault();
		var href = $(this).attr('href');
		var text = $(this).text();
		
			$.get(href, function(horario, status) {
				$('.horarioForm #id').val(horario.idHorario);
				$('.horarioForm #horaEntrada').val(horario.horaEntrada);
				$('.horarioForm #horaSalida').val(horario.horaSalida);
				$('.horarioForm #activo').val(horario.activo);
			});
			
			$('.horarioForm #horarioModal').modal();
	});*/
	
	
	
//	$('.eliminaBtn').on('click', function(event) {			    //botón elimina
//		event.preventDefault();
//		var href = $(this).attr('href');
//		
//		$('#eliminarModal #delRef').attr('href', href);
//		$('#eliminarModal').modal();
//	});
//	
//	$("#buscarText").on("keyup", function() {
//	    var value = $(this).val().toLowerCase();
//	    $("#tableHorarios tr").filter(function() {
//	      $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
//	    });
//	  });
//	
//	$('.filterable .btn-filter').click(function(){
//        var $panel = $(this).parents('.filterable'),
//        $filters = $panel.find('.filters input'),
//        $tbody = $panel.find('.table tbody');
//        if ($filters.prop('disabled') == true) {
//            $filters.prop('disabled', false);
//            $filters.first().focus();
//        } else {
//            $filters.val('').prop('disabled', true);
//            $tbody.find('.no-result').remove();
//            $tbody.find('tr').show();
//        }
//    });
//
//    $('.filterable .filters input').keyup(function(e){
//        /* Ignore tab key */
//        var code = e.keyCode || e.which;
//        if (code == '9') return;
//        /* Useful DOM data and selectors */
//        var $input = $(this),
//        inputContent = $input.val().toLowerCase(),
//        $panel = $input.parents('.filterable'),
//        column = $panel.find('.filters th').index($input.parents('th')),
//        $table = $panel.find('.table'),
//        $rows = $table.find('tbody tr');
//        /* Dirtiest filter function ever ;) */
//        var $filteredRows = $rows.filter(function(){
//            var value = $(this).find('td').eq(column).text().toLowerCase();
//            return value.indexOf(inputContent) === -1;
//        });
//        /* Clean previous no-result if exist */
//        $table.find('tbody .no-result').remove();
//        /* Show all rows, hide filtered ones (never do that outside of a demo ! xD) */
//        $rows.show();
//        $filteredRows.hide();
//        /* Prepend no-result row if all rows are filtered */
//        if ($filteredRows.length === $rows.length) {
//            $table.find('tbody').prepend($('<tr class="no-result text-center"><td colspan="'+ $table.find('.filters th').length +'">No encontrado</td></tr>'));
//        }
//    });
	
}); 