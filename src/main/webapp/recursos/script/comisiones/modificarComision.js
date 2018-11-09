$(document)
		.ready(
				function() {

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
						minDate: '0d',
						onSelect: function() 
					       { 
							validarFechas();
							var minDate = $('#fechaInicio').datepicker('getDate');
					        $("#fechaFin").datepicker("change", { minDate: minDate });
					       }
					});
					
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
					maxDate:'1y',
					onSelect: function() 
				       { 
						validarFechas();
						var maxDate = $('#fechaFin').datepicker('getDate');
				        $("#fechaInicio").datepicker("change", { maxDate: maxDate });
				       }
					   });


					$('.aceptaBtn').on('click', function(event) { // botón
																	// nuevo
						event.preventDefault();
						var href = $(this).attr('href');

						$('#aceptarModal #aceptaModal').attr('href', href);
						$('#aceptarModal').modal();
					})

					function validarFechas() {
						document.getElementById("botonGuardarComisiones").disabled = true;
						var fechaInicio = $("#fechaInicio").val();
						var fechaFin = $("#fechaFin").val();
						// alert("variable "+fechaInicio);
						// alert("variable2 "+fechaFin);
						if (fechaInicio != null && fechaInicio != "") {
							if (fechaFin != null && fechaFin != "") {
								var x = fechaInicio.split("/");
								fechaInicio = x[0] + "-" + x[1] + "-" + x[2];

								var y = fechaFin.split("/");
								fechaFin = y[0] + "-" + y[1] + "-" + y[2];
								var resultado = diasLibres(fechaInicio,
										fechaFin);
								if (resultado > 0) {
									$("#botonGuardarComisiones").attr(
											'disabled', false);
									$("#dias").val(resultado);
								} else {
									document
											.getElementById("botonGuardarComisiones").disabled = true;
									// $("#validacionDias").css("display","inline");
								}
							}
							// document.getElementById("botonGuardarVacaciones").disabled
							// = true;
						}
						// document.getElementById("botonGuardarVacaciones").disabled
						// = true;
					}

					function diasLibres(dateFrom, dateTo) {
						var from = moment(dateFrom, 'DD/MM/YYY'), to = moment(
								dateTo, 'DD/MM/YYY'), days = 0;

						while (!from.isAfter(to)) {
							// Si no es sabado ni domingo
							if (from.isoWeekday() !== 6
									&& from.isoWeekday() !== 7) {
								days++;
							}
							from.add(1, 'days');
						}
						return days;
					}
				});