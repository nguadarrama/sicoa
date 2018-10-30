$(document).ready(function() {
	$('#tablaLicenciasEmpleados').DataTable({
	    "scrollY": "500px",
	    "scrollCollapse": true
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
		 	var text = $(this).text();
			$.get(href, function(hmap, status) {
				//alert(hmap.licencia.idArchivo.idArchivo),
				
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
				}else{
					$(".aceptaLicencia").css("display","none");
					$(".rechazaLicencia").css("display","none");
					$("#descargar").css("display","none");
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