$(document).ready(function() {
	
	var today = new Date();
	var dd = today.getDate();
	var mm = today.getMonth()+1; //January is 0!

	var yyyy = today.getFullYear();
	if(dd<10){
	    dd='0'+dd;
	} 
	if(mm<10){
	    mm='0'+mm;
	} 
	today = dd+'-'+mm+'-'+yyyy;
	$("#fechaSolicitud").val(today);
	$('.collapse').collapse();
//	$('#collapseOne').collapse({
//		  toggle: false
//		})
	//$("#botonGuardarVacaciones").disabled = true;
	//document.getElementById("botonGuardarVacaciones").disabled=true;
		//document.getElementById("validacionDias").style.display = "none";
	var array =$("#listaDiasFestivos").val();
	//alert("LLegada "+array);
	var disableddates=array.split(",");
	//alert("LLegada "+disableddates);
	
	$("#fechaInicio").datepicker({ 
		beforeShowDay: function(date){ 
			show = true; if(date.getDay() == 0 || date.getDay() == 6){show = false;}
		//No Weekends 
		for (var i = 0; i < disableddates.length; i++) {
			if (new Date(disableddates[i]).toString() == date.toString()) {
				show = false;
				}
		//No Holidays 
		} var  display = [show,'',(show)?'':'Día no disponible por una de las siguientes razones: día festivo, fin de semana o vacaciones'];
		//With Fancy hover tooltip! 
		return display; 
		},
		minDate: 1,
		maxDate:'1y',
		onSelect: function() 
	       { 
	    	   calcularDias();
	    	   var maxDate = $('#fechaInicio').datepicker('getDate');
		        $("#fechaFin").datepicker("change", { minDate: maxDate });
	       },
	})
	$("#fechaFin").datepicker({ 
		beforeShowDay: function(date){ 
			show = true; if(date.getDay() == 0 || date.getDay() == 6){show = false;}
		//No Weekends 
		for (var i = 0; i < disableddates.length; i++) {
			if (new Date(disableddates[i]).toString() == date.toString()) {
				show = false;
				}
		//No Holidays 
		} var display = [show,'',(show)?'':'Día no disponible por una de las siguientes razones: día festivo, fin de semana o vacaciones'];
		//With Fancy hover tooltip! 
		return display; 
		},
		minDate: 1,
		maxDate:'1y',
		onSelect: function() 
	       { 
	    	   calcularDias();
	    	   var maxDate = $('#fechaFin').datepicker('getDate');
		        $("#fechaInicio").datepicker("change", { maxDate: maxDate });
	       },
		   })
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
		
		$("#cerrarMensaje").on('click', function(event){
			event.preventDefault();
			//$("#actualizamosVacacion").css("display","none");
			$("#mensajeError").css("display","none");
			//alert("bien")
		});
		
		$("#fechaInicioBusca1").datepicker({
				dateFormat: 'yy-mm-dd',
		       beforeShowDay: $.datepicker.noWeekends 
		   });
		$("#fechaFinBusca1").datepicker({ 
			dateFormat: 'yy-mm-dd',
		       beforeShowDay: $.datepicker.noWeekends 
		   });
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
					if(hmap.vacacion.idEstatus.idEstatus=='2' || hmap.vacacion.idEstatus.idEstatus=='3'){
						$('.rechazaVacacion #rechazaVacacion').css("display","none");
						$('.aceptaVacacion #aceptaVacacion').css("display","none");
					}
					//alert(hmap.vacacion.idArchivo.idArchivo);
					if(hmap.vacacion.idArchivo.idArchivo!=null && hmap.vacacion.idArchivo.idArchivo!=""){
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
			//alert($("#claveUsuario").val())
			  var from = moment(dateFrom, 'DD/MM/YYY'),
			    to = moment(dateTo, 'DD/MM/YYY'),
			    days = 0;
			    
			  while (!from.isAfter(to)) {
			    // Si no es sabado ni domingo
			    if (from.isoWeekday() !== 6 && from.isoWeekday() !== 7) {
			    	var bandera=false;
			    	for (var i = 0; i < disableddates.length; i++) {
			    		//alert(disableddates[i]+" ");
			    		var nueva = moment(disableddates[i], 'MM-DD-YYY');
			    		var nueva1=nueva.toString();
			    		var from1=from.toString();
						if (nueva1 === from1) {
							//alert("dias que cumplen "+nueva+" "+from);
							bandera=true;
							}else{
								//alert("dias que no cumplen "+nueva+" "+from);
								//days++;
							}
					//No Holidays 
					}
			      if(bandera==true){
			    	  //alert("dia que cumplen "+days);
			      }else{
			    	  days++;
			    	  //alert("dia que no cumplen "+days);
			      }
			    }
			    from.add(1, 'days');
			  }
			  return days;
			}
		function calcularDias(){
			//document.getElementById("botonGuardarVacaciones").disabled = true;
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
							$("#mensajeError").css("display","inline");
							document.getElementById("botonGuardarVacaciones").disabled = true;
						}
					}else{
						//alert("El número de días no es permitido");
						$("#mensajeError").css("display","inline");
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
		
		
		$('.rechazaBtn').on('click', function(event) {			    //botón elimina
			event.preventDefault();
			var href = $(this).attr('href');
			$('#rechazarModal #rechazaModal').attr('href', href);
			$('#rechazarModal').modal();
		});

	
}); 