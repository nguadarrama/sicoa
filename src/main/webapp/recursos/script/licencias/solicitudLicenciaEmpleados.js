$(document).ready(function() {
	document.getElementById("botonGuardarLicencia").disabled=true;
	$("#fechaInicio").datepicker({ 
		//minDate: 1,
	       beforeShowDay: $.datepicker.noWeekends,
	       onSelect: function() 
	       { 
	    	   calcularDias();
	       },
	   });
	$("#fechaFin").datepicker({ 
		//minDate: 1,
	       beforeShowDay: $.datepicker.noWeekends,
	       onSelect: function() 
	       { 
	    	   calcularDias();
	       },
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
				if(resultado>0){
					//alert("bien ");
					//alert(resultado);
					$("#botonGuardarLicencia").attr('disabled', false);
					$("#dias").val(resultado);
//					var diasAutorizados=$("#diasDispobibles").val();
//					//alert("Dias disponibles "+diasAutorizados);
//					if(diasAutorizados>=resultado){
//						$("#botonGuardarVacaciones").attr('disabled', false);
//					$("#diasPorPedir").val(resultado);
//					//alert("bien");
//					
//					}else{
//						//$("#validacionDias").css("display","inline");
//						//alert("Error, los días no deben pasar del tope ");
//						document.getElementById("botonGuardarVacaciones").disabled = true;
//					}
				}else{
					document.getElementById("botonGuardarLicencia").disabled = true;
				}
			}
			//document.getElementById("botonGuardarVacaciones").disabled = true;
		}
		//document.getElementById("botonGuardarVacaciones").disabled = true;
	};
	
	$("#registrarMiLicencia").on('click', function(event){
		//alert("");
		event.preventDefault();
		var href = $(this).attr('href');
	 	var text = $(this).text();
		$.get(href, function(hmap, status) {
			//alert(hmap.usuario.fechaIngreso)
			$(".actualizaLicencia #nombre").val(hmap.usuario.nombre);
			$(".actualizaLicencia #apellidoPaterno").val(hmap.usuario.apellidoPaterno);
			$(".actualizaLicencia #apellidoMaterno").val(hmap.usuario.apellidoMaterno);
			$(".actualizaLicencia #claveUsuario1").val(hmap.usuario.claveUsuario);
			$(".actualizaLicencia #apellidoMaterno").val(hmap.usuario.apellidoMaterno);
			$(".actualizaLicencia #idPuesto").val(hmap.usuario.idPuesto);
			$(".actualizaLicencia #rfc").val(hmap.usuario.rfc);
			//$(".actualizaLicencia #fechaInicio1").val(hmap.licencia.fechaInicio);
			//$(".actualizaLicencia #fechaInicio2").val(hmap.licencia.fechaInicio);
			//$(".actualizaLicencia #fechaFin1").val(hmap.licencia.fechaFin);
			//$(".actualizaLicencia #fechaFin2").val(hmap.licencia.fechaFin);
			$(".actualizaLicencia #idUnidadAdministrativa").val(hmap.usuario.nombreUnidad);
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
			var today = dd+'-'+mm+'-'+yyyy;
			$(".actualizaLicencia #idSolicitud").val(today);
//			var str=;
//			str.replace(",","");
//			var today = new Date(str);
//			var dd = today.getDate();
//			var mm = today.getMonth()+1; //January is 0!
//
//			var yyyy = today.getFullYear();
//			if(dd<10){
//			    dd='0'+dd;
//			} 
//			if(mm<10){
//			    mm='0'+mm;
//			} 
//			var today = dd+'-'+mm+'-'+yyyy;
			$(".actualizaLicencia #fechaIngreso").val(hmap.usuario.fechaIngreso);
			
			
			//document.getElementById("idSolicitud").value = today;
			//$(".actualizaLicencia #dias").val(hmap.licencia.dias);
			$("#detalleLicencia").css("display","inline");
			$(".tablaLicencias").css("display","none");
		});
		
	});
	
	$('#tablaLicenciasEmpleados').on('click','.eBtn' , function(event) { 					//botón edita
	 	event.preventDefault();
	 	var href = $(this).attr('href');
	 	var text = $(this).text();
		$.get(href, function(hmap, status) {
			$(".actualizaLicencia #nombre").val(hmap.usuario.nombre);
			$(".actualizaLicencia #apellidoPaterno").val(hmap.usuario.apellidoPaterno);
			$(".actualizaLicencia #apellidoMaterno").val(hmap.usuario.apellidoMaterno);
			$(".actualizaLicencia #claveUsuario1").val(hmap.usuario.claveUsuario);
			$(".actualizaLicencia #apellidoMaterno").val(hmap.usuario.apellidoMaterno);
			$(".actualizaLicencia #idPuesto").val(hmap.usuario.idPuesto);
			$(".actualizaLicencia #rfc").val(hmap.usuario.rfc);
			$(".actualizaLicencia #idUnidadAdministrativa").val(hmap.usuario.nombreUnidad);
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
			var today = dd+'-'+mm+'-'+yyyy;
			$(".actualizaLicencia #idSolicitud").val(today);
			$(".actualizaLicencia #fechaIngreso").val(hmap.usuario.fechaIngreso);
			$("#detalleLicencia").css("display","inline");
			$(".tablaLicencias").css("display","none");
			
		});
	});
});