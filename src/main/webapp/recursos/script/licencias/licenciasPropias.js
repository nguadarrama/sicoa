$(document).ready(function() {
	
	$("#fechaInicio").datepicker({ 
	       beforeShowDay: $.datepicker.noWeekends,
	      
	   });
	$("#fechaFin").datepicker({ 
	       beforeShowDay: $.datepicker.noWeekends,
	   });
	$('#tablaLicenciasPropias').on('click','.eBtn' , function(event) { 					//botón edita
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
			if(hmap.licencia.idArchivo.idArchivo!=null && hmap.licencia.idArchivo.idArchivo!=""){
			$(".actualizaArchivo #idArchivo").val(hmap.licencia.idArchivo.idArchivo);
			}else{
				$(".actualizaArchivo").css("display","none");
			}
			$(".actualizaArchivo #idLicencia").val(hmap.licencia.idLicencia);
			$(".actualizaArchivo #claveUsuario").val(hmap.licencia.idUsuario.claveUsuario);
			$(".actualizaLicencia #idUnidadAdministrativa").val(hmap.licencia.idUsuario.nombreUnidad);
			$(".actualizaLicencia #dias").val(hmap.licencia.dias);
			$("#detalleLicencia").css("display","inline");
			$(".listaDeLicenciasPropias").css("display","none");
			//alert(hmap.licencia.idUsuario.claveUsuario);
			
		});
	});
	
}); 