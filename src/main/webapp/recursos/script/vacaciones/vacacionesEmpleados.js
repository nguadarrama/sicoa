$(document).ready(function() {
	var elementExists = document.getElementById("archivo");
	if(elementExists!=null && elementExists!=""){
			document.getElementById("guardarArchivo").disabled = true;
			document.getElementById("archivo").required = true;
			if(document.getElementById("archivo").required == true){
				$("#guardarArchivo").attr('disabled', false);
			}else{
				document.getElementById("guardarArchivo").disabled = true;
			}
	}
	
	$('#guardarArchivo').on('click', function(event) { 
		$('#guardarArchivo').val("Guardando...");
		$('#miBotonCancelar').prop('disabled', true);
		$('#formActualizaArchivo').submit();
		$('#guardarArchivo').prop('disabled', true);
	});
	$('#vacacionesPropias').DataTable({
	    "scrollY": "500px",
	    "scrollCollapse": true
	  });
	$('.collapse').collapse();
	
	$("#fechaInicio").datepicker({ 
		beforeShowDay: function(date){ 
			var show = true; if(date.getDay() == 0 || date.getDay() == 6){show = false;}
		//No Weekends 
		for (var i = 0; i < disableddates.length; i++) {
			if (new Date(disableddates[i]).toString() == date.toString()) {
				show = false;
				}
		//No Holidays 
		} var display = [show,'',(show)?'':'No Weekends or Holidays'];
		//With Fancy hover tooltip! 
		return display; 
		},
		minDate: 1,
		onSelect: function() 
	       { 
	    	   calcularDias();
	       },
	})
	$("#fechaFin").datepicker({ 
		beforeShowDay: function(date){ 
			var show = true; if(date.getDay() == 0 || date.getDay() == 6){show = false;}
		//No Weekends 
		for (var i = 0; i < disableddates.length; i++) {
			if (new Date(disableddates[i]).toString() == date.toString()) {
				show = false;
				}
		//No Holidays 
		} var display = [show,'',(show)?'':'No Weekends or Holidays'];
		//With Fancy hover tooltip! 
		return display; 
		},
		minDate: 1,
		onSelect: function() 
	       { 
	    	   calcularDias();
	       },
		   })
//	  function DisableSpecificDates(date) {
//
//	   var m = date.getMonth();
//	   var d = date.getDate();
//	   var y = date.getFullYear();
//	   var currentdate = (m + 1) + '-' + d + '-' + y ;
//
//	   for (var i = 0; i < disableddates.length; i++) {
//
//
//	   if ($.inArray(currentdate, disableddates) != -1 ) {
//	   return [false];
//	   } 
//	   }
//	   var highlight = eventDates[date];
//       if( highlight ) {
//            return [true, "event", 'Tooltip text'];
//       } else {
//            return [true, '', ''];
//       }
//	  
//	   return disableddates;
//	  }
	$('.descargaArchivo').css("display","none");
	$("#actualizamosVacacion").css("display","none");
	$('.actualizaVacacion #responsableAux').css("display","none");
		$("#btnVacacionesPropias").on('click', function(event){
		
			
		});
		$("#miBotonCancelar").on('click', function(event){
			event.preventDefault();
			$("#actualizamosVacacion").css("display","none");
			$(".misPropiasVacaciones").css("display","inline");
		});
		
		$("#fechaInicioBusca1").datepicker({
				dateFormat: 'yy-mm-dd',
		       beforeShowDay: $.datepicker.noWeekends 
		   });
		$("#fechaFinBusca1").datepicker({ 
			dateFormat: 'yy-mm-dd',
		       beforeShowDay: $.datepicker.noWeekends 
		   });
//		$("#fechaInicio").datepicker({ 
//			minDate: 1,
//		       beforeShowDay: $.datepicker.noWeekends,
//		       //beforeShowDay: DisableSpecificDates,
//		       onSelect: function() 
//		       { 
//		    	   DisableSpecificDates();
//		    	   calcularDias();
//		       },
//		   });
//		$("#fechaInicio").on('click', function(event){
//			alert();
//			DisableSpecificDates();
//		});
		$('#vacacionesPropias').on('click','.eBtn' , function(event) { 					//botón edita
			//alert(""); 
			event.preventDefault();
			var href = $(this).attr('href');
				$.get(href, function(hmap, status) {
					//alert(hmap.vacacion.idVacacion.idVacacion),
					$('.actualizaVacacion #numeroEmpleado').val(hmap.vacacion.idUsuario.claveUsuario);
					
					$('.actualizaVacacion #nombre').val(hmap.vacacion.idUsuario.nombre);
					$('.actualizaVacacion #apellidoPaterno').val(hmap.vacacion.idUsuario.apellidoPaterno);
					$('.actualizaVacacion #apellidoMaterno').val(hmap.vacacion.idUsuario.apellidoMaterno);
					$('.actualizaVacacion #idSolicitud').val(hmap.vacacion.idDetalle);
					$('.actualizaVacacion #idVacacion').val(hmap.vacacion.idVacacion.idVacacion);
					$('.actualizaVacacion #idEstatus').val(hmap.vacacion.idEstatus.estatus);
					$('.actualizaVacacion #idPuesto').val(hmap.vacacion.idUsuario.idPuesto);
					$('.actualizaVacacion #idUnidadAdministrativa').val(hmap.vacacion.idUsuario.nombreUnidad);
					$('.actualizaVacacion #fechaIngreso').val(hmap.vacacion.idUsuario.fechaIngreso);
					$('.actualizaVacacion #rfc').val(hmap.vacacion.idUsuario.rfc);
					$('.actualizaVacacion #fechaInicio1').val(hmap.vacacion.fechaInicio);
					$('.actualizaVacacion #fechaSolicitud').val(hmap.vacacion.fechaRegistro);
					$('.actualizaVacacion #fechaFin1').val(hmap.vacacion.fechaFin);
					$('.actualizaArchivo #idArchivo').val(hmap.vacacion.idArchivo.idArchivo);
					$('.actualizaArchivo #idDetalle').val(hmap.vacacion.idDetalle);
					$('.actualizaArchivo #claveUsuario').val(hmap.vacacion.idUsuario.claveUsuario);
					//alert(hmap.vacacion.idEstatus.idEstatus+" "+hmap.vacacion.idArchivo.idArchivo)
//					if(hmap.vacacion.idEstatus.idEstatus!='3'){
//						$('.cancelaVacacion #idVacacionCancelar').val(hmap.vacacion.idDetalle);
//						$('.cancelaVacacion').css("display","inline");
//					}else{
//						$('.cancelaVacacion').css("display","none");
//					}
//					if(hmap.vacacion.idEstatus.idEstatus=='2' || hmap.vacacion.idEstatus.idEstatus=='3'){
//						$('.rechazaVacacion #rechazaVacacion').css("display","none");
//						$('.aceptaVacacion #aceptaVacacion').css("display","none");
//						$('.actualizaArchivo').css("display","none");
//					}
					//<alert(hmap.vacacion.idEstatus.idEstatus)
					if(hmap.vacacion.idEstatus.idEstatus=='1'){
						$('#descarga').css("display","inline");
						$('.actualizaArchivo1 #idDetalle').val(hmap.vacacion.idDetalle);
						$('.actualizaArchivo1 #claveUsuario').val(hmap.vacacion.idUsuario.claveUsuario);
						$('.cancelaVacacion').css("display","none");
						$('.actualizaArchivo1').css("display","inline");
						if(hmap.vacacion.idArchivo.idArchivo!=null && hmap.vacacion.idArchivo.idArchivo!=""){
							$('.actualizaArchivo1 #idArchivo').val(hmap.vacacion.idArchivo.idArchivo);
							
							$('.descargaArchivo #idArchivo').val(hmap.vacacion.idArchivo.idArchivo);
							$('.descargaArchivo').css("display","inline");
							$('.rechazaVacacion').css("display","inline");
							$('.aceptaVacacion').css("display","inline");
							//alert(hmap.vacacion.idArchivo.idArchivo);
						}else{
							$('.descargaArchivo').css("display","none");
							$('.rechazaVacacion').css("display","none");
							$('.aceptaVacacion').css("display","none");
						}	
					}
					if(hmap.vacacion.idEstatus.idEstatus=='2'){
						$('.actualizaArchivo1').css("display","none");
						if(hmap.vacacion.idArchivo.idArchivo!=null && hmap.vacacion.idArchivo.idArchivo!=""){
							$('.descargaArchivo #idArchivo').val(hmap.vacacion.idArchivo.idArchivo);
							$('.descargaArchivo').css("display","inline");
							$('.aceptaVacacion').css("display","none");
							$('.rechazaVacacion').css("display","none");
						}
						$('.cancelaVacacion #idVacacionCancelar').val(hmap.vacacion.idDetalle);
						$('.cancelaVacacion').css("display","inline");
						$('#descarga').css("display","none");
					}
					if(hmap.vacacion.idEstatus.idEstatus=='3'){
						$('.actualizaArchivo1').css("display","inline");
						$('.cancelaVacacion').css("display","none");
						$('#descarga').css("display","none");
						$('.aceptaVacacion').css("display","none");
						$('.rechazaVacacion').css("display","none");
						if(hmap.vacacion.idArchivo.idArchivo!=null && hmap.vacacion.idArchivo.idArchivo!=""){
							$('.descargaArchivo #idArchivo').val(hmap.vacacion.idArchivo.idArchivo);
							$('.descargaArchivo').css("display","inline");
						}
					}
					if(hmap.vacacion.idEstatus.idEstatus=='6'){
						$('.actualizaArchivo1').css("display","none");
						if(hmap.vacacion.idArchivo.idArchivo!=null && hmap.vacacion.idArchivo.idArchivo!=""){
							$('.descargaArchivo #idArchivo').val(hmap.vacacion.idArchivo.idArchivo);
							$('.descargaArchivo').css("display","inline");
						}
						$('.actualizaArchivo').css("display","none");
						$('#descarga').css("display","none");
						$('.cancelaVacacion').css("display","none");
						$('.rechazaVacacion').css("display","none");
						$('.aceptaVacacion').css("display","none");
					}
					//alert(hmap.vacacion.idArchivo.idArchivo);
//					if(hmap.vacacion.idArchivo.idArchivo!=null && hmap.vacacion.idArchivo.idArchivo!=""){
//						$('.descargaArchivo #idArchivo').val(hmap.vacacion.idArchivo.idArchivo);
//						$('.descargaArchivo').css("display","inline");
//						$('.rechazaVacacion').css("display","inline");
//						$('.aceptaVacacion').css("display","inline");
//						//alert(hmap.vacacion.idArchivo.idArchivo);
//					}else{
//						$('.descargaArchivo').css("display","none");
//						$('.rechazaVacacion').css("display","none");
//						$('.aceptaVacacion').css("display","none");
//					}
					$('.actualizaVacacion #dias').val(hmap.vacacion.dias);
					$('.aceptaVacacion #idSolicitud').val(hmap.vacacion.idDetalle);
					$('.aceptaVacacion #fechaInicio').val(hmap.vacacion.fechaInicio);
					$('.aceptaVacacion #fechaFin').val(hmap.vacacion.fechaFin);
					$('#id').val(hmap.vacacion.idUsuario.idUsuario);
					$('#idUsuario').val(hmap.vacacion.idUsuario.idUsuario);
					$('.rechazaVacacion #idSolicitud').val(hmap.vacacion.idDetalle);
					$('.rechazaVacacion #idVacacion').val(hmap.vacacion.idVacacion.idVacacion);
					$('.rechazaVacacion #dias').val(hmap.vacacion.dias);
					//alert(hmap.responsable);
					if(hmap.responsable!=null && hmap.responsable!=""){
					$('.actualizaVacacion #responsable').val(hmap.responsable.nombre+" "+hmap.responsable.apellidoPaterno+" "+hmap.responsable.apellidoMaterno);
					$('.actualizaVacacion #responsableAux').css("display","none");
					}else{
						$('.actualizaVacacion #responsable').css("display","none");
						$('.actualizaVacacion #responsableAux').css("display","inline");
					}
					$("#actualizamosVacacion").css("display","inline");
					$(".misPropiasVacaciones").css("display","none");
					
				});
				
				//$('.usuarioForm #usuarioModal').modal().slideUp(300).delay(400).fadeIn(400);
		});
		
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
			document.getElementById("botonGuardarVacaciones").disabled = true;
			var fechaInicio= $("#fechaInicio").val();
			var fechaFin=$("#fechaFin").val();
			//alert("Valor1 "+fechaInicio+" valor2 "+fechaFin);
			//alert("variable "+fechaInicio);
			//alert("variable2 "+fechaFin);
			if(fechaInicio!=null && fechaInicio!=""){
				if(fechaFin!=null && fechaFin!=""){	
					var x = fechaInicio.split("/");
					fechaInicio = x[0] + "-" + x[1] + "-" + x[2];
					
					var y = fechaFin.split("/");
					fechaFin = y[0] + "-" + y[1] + "-" + y[2];
					var resultado = diasLibres(fechaInicio,fechaFin);
					//var dias=diasEntreFechas();
					//resultado+=1;
					//alert ("fechaInicio "+fechaInicio+" fechaFin "+fechaFin+" resultado "+resultado);
					if(resultado>0 && resultado<=10){
						//alert("bien ");
						var diasAutorizados=$("#diasDispobibles").val();
						//alert("Dias disponibles "+diasAutorizados);
						if(diasAutorizados>=resultado){
							$("#botonGuardarVacaciones").attr('disabled', false);
						$("#diasPorPedir").val(resultado);
						//alert("bien");
						
						}else{
							//$("#validacionDias").css("display","inline");
							//alert("Error, los días no deben pasar del tope ");
							document.getElementById("botonGuardarVacaciones").disabled = true;
						}
					}else{
						//alert("El número de días no es permitido");
						document.getElementById("botonGuardarVacaciones").disabled = true;
						//$("#validacionDias").css("display","inline");
					}
				}
				//document.getElementById("botonGuardarVacaciones").disabled = true;
			}
			//document.getElementById("botonGuardarVacaciones").disabled = true;
		}
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