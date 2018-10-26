$(document).ready(function() {
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
				//alert(""),
				
				$(".actualizaLicencia #nombre").val(hmap.licencia.idUsuario.nombre);
				$(".actualizaLicencia #apellidoPaterno").val(hmap.licencia.idUsuario.apellidoPaterno);
				$(".actualizaLicencia #apellidoMaterno").val(hmap.licencia.idUsuario.apellidoMaterno);
				$(".actualizaLicencia #claveUsuario1").val(hmap.licencia.idUsuario.claveUsuario);
				$(".actualizaLicencia #apellidoMaterno").val(hmap.licencia.idUsuario.apellidoMaterno);
				$(".actualizaLicencia #idPuesto").val(hmap.licencia.idUsuario.idPuesto);
				$(".actualizaLicencia #rfc").val(hmap.licencia.idUsuario.rfc);
				$(".actualizaLicencia #fechaSolicitud").val(hmap.licencia.fechaRegistro);
				$(".actualizaLicencia #fechaInicio1").val(hmap.licencia.fechaInicio);
				$(".actualizaLicencia #fechaInicio2").val(hmap.licencia.fechaInicio);
				$(".actualizaLicencia #fechaFin1").val(hmap.licencia.fechaFin);
				$(".actualizaLicencia #fechaFin2").val(hmap.licencia.fechaFin);
				$(".actualizaLicencia #idUnidadArministrativa").val(hmap.licencia.idUsuario.nombreUnidad);
				$(".actualizaLicencia #dias").val(hmap.licencia.dias);
				$("#detalleLicencia").css("display","inline");
				$(".listaDeLicenciasEmpleados").css("display","none");
				alert(hmap.licencia.idUsuario.claveUsuario);
				
			});
		});
	
}); 