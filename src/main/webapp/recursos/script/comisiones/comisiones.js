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
					$('.actualizaArchivo #idArchivo').val(hmap.comision.idArchivo.idArchivo);
					$('.actualizaArchivo #idComisionArchivo').val(hmap.comision.idComision);
					$('.actualizaArchivo #claveUsuario').val(hmap.comision.idUsuario.claveUsuario);
					$('.actualizaArchivo #idEstatusArchivo').val(hmap.comision.idEstatus.idEstatus);
					$('.actualizaArchivoPropio #idArchivoPropio').val(hmap.comision.idArchivo.idArchivo);
					$('.actualizaArchivoPropio #idComisionArchivoPropio').val(hmap.comision.idComision);
					$('.actualizaArchivoPropio #claveUsuarioPropio').val(hmap.comision.idUsuario.claveUsuario);
					$('.actualizaArchivoPropio #idEstatusArchivoPropio').val(hmap.comision.idEstatus.idEstatus);
					$('.formModificar #claveUsuarioEditar').val(hmap.comision.idUsuario.claveUsuario);
					$('.formModificar #idComisionEditar').val(hmap.comision.idComision);
					if(hmap.comision.idEstatus.idEstatus=='2' || hmap.comision.idEstatus.idEstatus=='3'){
						$('.rechazaComision').css("display","none");
						$('.aceptaComision').css("display","none");
						$('.formModificar #botonEditar').css("display","none");
					}else{
						
					}
					//alert(hmap.vacacion.idArchivo.idArchivo);
					if(hmap.comision.idArchivo.idArchivo!=null && hmap.comision.idArchivo.idArchivo!=""){
						$('.descargaArchivo #idArchivo').val(hmap.comision.idArchivo.idArchivo);
						$('.actualizaArchivo').css("display","inline");
						$('.rechazaComision').css("display","inline");
						$('.aceptaComision').css("display","inline");
						$('.actualizaArchivoPropio').css("display","none");
					}else{
						$('.actualizaArchivo').css("display","none");
						$('.rechazaComision').css("display","none");
						$('.aceptaComision').css("display","none");
						$('.actualizaArchivoPropio').css("display","inline");
						$("#descargar").css("display","none");
						
					}
					$(".aceptaComision #idComision").val(hmap.comision.idComision);
					$(".aceptaComision #idArchivo").val(hmap.comision.idArchivo.idArchivo);
					$(".aceptaComision #claveUsuario").val(hmap.comision.idUsuario.claveUsuario);
					$(".rechazaComision #idComision").val(hmap.comision.idComision);
					$(".rechazaComision #idArchivo").val(hmap.comision.idArchivo.idArchivo);
					$(".rechazaComision #claveUsuario").val(hmap.comision.idUsuario.claveUsuario);
					$('.actualizaVacacion #dias').val(hmap.comision.dias);
					$('#id').val(hmap.comision.idUsuario.idUsuario);
					$('#idUsuario').val(hmap.comision.idUsuario.idUsuario);
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
		
		$('#fechaInicio').val($('#validBeforeDatepicker').val());
		$('#fechaFin').val($('#validAfterDatepicker').val());
		
	    //validaciones para datepicker
	    $('#validarFechas').validate({ 
	        rules: { 
	            fechaInicio1: { 
	                dpCompareDate: "notAfter #fechaFin1"
	            },
			    fechaFin1: { 
			        dpCompareDate: "notBefore #fechaInicio1"
			    } 
	        },
	    	messages: {
	    		fechaInicial: 'Ingresa fecha menor ó igual a la Fecha Final',
	    		fechaFinal: 'Ingresa fecha mayor ó igual a la Fecha Inicial'
	    	}
	    			
	    });
}); 