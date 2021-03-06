$(document).ready(function() {
	$('#tablaLicenciasEmpleados').DataTable({
	    "scrollY": "500px",
	    "scrollCollapse": true
	  });
	
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
		$('#botonCancelar').prop('disabled', true);
		$('#formActualizaArchivo').submit();
		$('#guardarArchivo').prop('disabled', true);
	});
	
	
	$("#detalleLicencia").css("display","none");
	$("#fechaInicio").datepicker({ 
		minDate: 1,
	       beforeShowDay: $.datepicker.noWeekends,
	      
	       onSelect: function() 
	       { 
	    	   DisableSpecificDates();
	    	   calcularDias();
	       },
	   });
	$("#botonCancelar").on('click', function(event){
		event.preventDefault();
		$("#detalleLicencia").css("display","none");
		$(".listaDeLicenciasEmpleados").css("display","inline");
		
	});
	$("#fechaFin").datepicker({ 
		minDate: 1,
	       beforeShowDay: $.datepicker.noWeekends,
	       onSelect: function() 
	       { 
	    	   calcularDias();
	       },
	   });
		$('#tablaLicenciasEmpleados').on('click','.eBtn' , function(event) { 					//botón edita
		 	event.preventDefault();
		 	var href = $(this).attr('href');
			$.get(href, function(hmap, status) {
				$(".actualizaLicencia #nombre").val(hmap.licencia.idUsuario.nombre);
				$(".actualizaLicencia #apellidoPaterno").val(hmap.licencia.idUsuario.apellidoPaterno);
				$(".actualizaLicencia #apellidoMaterno").val(hmap.licencia.idUsuario.apellidoMaterno);
				$(".actualizaLicencia #claveUsuario1").val(hmap.licencia.idUsuario.claveUsuario);
				$(".actualizaLicencia #apellidoMaterno").val(hmap.licencia.idUsuario.apellidoMaterno);
				$(".actualizaLicencia #fechaIngreso").val(hmap.licencia.idUsuario.fechaIngreso)
				$(".actualizaLicencia #idPuesto").val(hmap.licencia.idUsuario.idPuesto);
				$(".actualizaLicencia #rfc").val(hmap.licencia.idUsuario.rfc);
				$(".actualizaLicencia #padecimiento").val(hmap.licencia.padecimiento);
				$(".actualizaLicencia #idSolicitud").val(hmap.licencia.fechaRegistro);
				$(".actualizaLicencia #fechaInicio1").val(hmap.licencia.fechaInicio);
				$(".actualizaLicencia #fechaInicio2").val(hmap.licencia.fechaInicio);
				$(".actualizaLicencia #fechaFin1").val(hmap.licencia.fechaFin);
				$(".actualizaLicencia #fechaFin2").val(hmap.licencia.fechaFin);
				$(".actualizaArchivo #idArchivo").val(hmap.licencia.idArchivo.idArchivo);
				if(hmap.licencia.idArchivo.idArchivo!=null && hmap.licencia.idArchivo.idArchivo!=""){
					$(".aceptaLicencia #idLicencia").val(hmap.licencia.idLicencia);
					$(".aceptaLicencia #idArchivo").val(hmap.licencia.idArchivo.idArchivo);
					$(".aceptaLicencia #claveUsuario").val(hmap.licencia.idUsuario.claveUsuario);
					$(".rechazaLicencia #idLicencia").val(hmap.licencia.idLicencia);
					$(".rechazaLicencia #idArchivo").val(hmap.licencia.idArchivo.idArchivo);
					$(".rechazaLicencia #claveUsuario").val(hmap.licencia.idUsuario.claveUsuario);
					$(".actualizaArchivo1 #idArchivo").val(hmap.licencia.idArchivo.idArchivo);
					$(".actualizaArchivo1 #idLicencia").val(hmap.licencia.idLicencia);
					$(".actualizaArchivo1 #claveUsuario").val(hmap.licencia.idUsuario.claveUsuario);
					$(".actualizaArchivo1").css("display","inline");
					
				}else{
					$(".aceptaLicencia").css("display","none");
					$(".rechazaLicencia").css("display","none");
					$("#descargar").css("display","none");
					$(".actualizaArchivo1").css("display","none");
				}
				if(hmap.licencia.idEstatus.idEstatus=="2" || hmap.licencia.idEstatus.idEstatus=="3"){
					$(".actualizaArchivo").css("display","none");
					
				}else{
					$(".actualizaArchivo").css("display","inline");
				}
				//alert(hmap.licencia.idEstatus.idEstatus);
				if(hmap.licencia.idEstatus.idEstatus!="1"){
					$(".aceptaLicencia").css("display","none");
					$(".rechazaLicencia").css("display","none");
				}
				$(".actualizaArchivo #idLicencia").val(hmap.licencia.idLicencia);
				$(".actualizaArchivo #claveUsuario").val(hmap.licencia.idUsuario.claveUsuario);
				$(".actualizaLicencia #idUnidadAdministrativa").val(hmap.licencia.idUsuario.nombreUnidad);
				$(".actualizaLicencia #dias").val(hmap.licencia.dias);
				$("#detalleLicencia").css("display","inline");
				$(".listaDeLicenciasEmpleados").css("display","none");
				//alert(hmap.licencia.idUsuario.claveUsuario);
				
			});
		});
	
}); 