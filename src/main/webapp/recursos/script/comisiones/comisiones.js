$(document).ready(function() {
	$('#vacacionesPropias').DataTable({
	    "scrollY": "500px",
	    "scrollCollapse": true
	  });
	$('#registraVacacionesEmpleados').DataTable({
	    "scrollY": "500px",
	    "scrollCollapse": true
	  });
	
	$('.collapse').collapse();
//	$('#collapseOne').collapse({
//		  toggle: false
//		})
	//$("#botonGuardarVacaciones").disabled = true;
	//document.getElementById("botonGuardarVacaciones").disabled=true;
		//document.getElementById("validacionDias").style.display = "none";
	var disableddates = ["10-26-2018", "10-30-2018", "10-31-2018"];

	  function DisableSpecificDates(date) {

	   var m = date.getMonth();
	   var d = date.getDate();
	   var y = date.getFullYear();
	   var currentdate = (m + 1) + '-' + d + '-' + y ;

	   for (var i = 0; i < disableddates.length; i++) {


	   if ($.inArray(currentdate, disableddates) != -1 ) {
	   return [false];
	   } 
	   }

	   return disableddates;
	  }
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
		$("#fechaInicio").datepicker({ 
			minDate: 1,
		       beforeShowDay: $.datepicker.noWeekends,
		       beforeShowDay: DisableSpecificDates,
		       onSelect: function() 
		       { 
		    	   calcularDias();
		       },
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
		    if(input.files && input.files[0]);
		        //alert("File Seleccionado : ", input.files[0]);
		    
		}
		$('#vacacionesPropias').on('click','.eBtn' , function(event) { 					//botón edita
			 event.preventDefault();
			var href = $(this).attr('href');
			var text = $(this).text();
				$.get(href, function(hmap, status) {
					//alert(hmap.vacacion.idVacacion.idVacacion),
					$('.actualizaVacacion #numeroEmpleado').val(hmap.comision.idUsuario.claveUsuario);
					$('.actualizaVacacion #nombre').val(hmap.comision.idUsuario.nombre);
					$('.actualizaVacacion #apellidoPaterno').val(hmap.comision.idUsuario.apellidoPaterno);
					$('.actualizaVacacion #apellidoMaterno').val(hmap.comision.idUsuario.apellidoMaterno);
					$('.actualizaVacacion #idSolicitud').val(hmap.comision.idComision);
					$('.actualizaVacacion #idEstatus').val(hmap.comision.idEstatus.estatus);
					$('.actualizaVacacion #idPuesto').val(hmap.comision.idUsuario.idPuesto);
					$('.actualizaVacacion #idUnidadAdministrativa').val(hmap.comision.idUsuario.nombreUnidad);
					$('.actualizaVacacion #fechaIngreso').val(hmap.comision.idUsuario.fechaIngreso);
					$('.actualizaVacacion #rfc').val(hmap.comision.idUsuario.rfc);
					$('.actualizaVacacion #fechaInicio1').val(hmap.comision.fechaInicio);
					$('.actualizaVacacion #fechaSolicitud').val(hmap.comision.fechaRegistro);
					$('.actualizaVacacion #fechaFin1').val(hmap.comision.fechaFin);
					$('.actualizaVacacion #comision').val(hmap.comision.comision);
					$('.actualizaVacacion #idHorario').val(hmap.horario);
//					$('.actualizaArchivo #idArchivo').val(hmap.comision.idArchivo.idArchivo);
					$('.actualizaArchivo #idArchivo').val(hmap.comision.idArchivo);
					$('.actualizaArchivo #idComision').val(hmap.comision.idComision);
					$('.actualizaArchivo #claveUsuario').val(hmap.comision.idUsuario.claveUsuario);
					$('.formModificar #claveUsuarioEditar').val(hmap.comision.idUsuario.claveUsuario);
					$('.formModificar #idComisionEditar').val(hmap.comision.idComision);
					if(hmap.comision.idEstatus.idEstatus=='2' || hmap.comision.idEstatus.idEstatus=='3'){
						$('.rechazaVacacion #rechazaVacacion').css("display","none");
						$('.aceptaVacacion #aceptaVacacion').css("display","none");
					}else{
						
					}
					//alert(hmap.vacacion.idArchivo.idArchivo);
//					if(hmap.comision.idArchivo.idArchivo!=null && hmap.comision.idArchivo.idArchivo!=""){
					if(hmap.comision.idArchivo!=null && hmap.comision.idArchivo!=""){
//						$('.descargaArchivo #idArchivo').val(hmap.comision.idArchivo.idArchivo);
						$('.descargaArchivo #idArchivo').val(hmap.comision.idArchivo);
						$('.descargaArchivo').css("display","inline");
						$('.rechazaVacacion').css("display","inline");
						$('.aceptaVacacion').css("display","inline");
						//alert(hmap.vacacion.idArchivo.idArchivo);
					}else{
						$('.descargaArchivo').css("display","none");
						$('.rechazaVacacion').css("display","none");
						$('.aceptaVacacion').css("display","none");
					}
					$('.actualizaVacacion #dias').val(hmap.comision.dias);
					$('.aceptaVacacion #idSolicitud').val(hmap.comision.idComision);
					$('.aceptaVacacion #fechaInicio').val(hmap.comision.fechaInicio);
					$('.aceptaVacacion #fechaFin').val(hmap.comision.fechaFin);
					$('#id').val(hmap.comision.idUsuario.idUsuario);
					$('#idUsuario').val(hmap.comision.idUsuario.idUsuario);
					$('.rechazaVacacion #idSolicitud').val(hmap.comision.idComision);
//					$('.rechazaVacacion #idVacacion').val(hmap.comision.idVacacion.idVacacion);
					$('.rechazaVacacion #dias').val(hmap.comision.dias);
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