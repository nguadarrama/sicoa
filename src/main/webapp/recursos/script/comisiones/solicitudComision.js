$(document)
		.ready(
				function() {

					$("#fechaInicio").datepicker({
						beforeShowDay : $.datepicker.noWeekends,
						onSelect : function() {
							validarFechas();
						}
					})
					$("#fechaFin").datepicker({
						beforeShowDay : $.datepicker.noWeekends,
						onSelect : function() {
							validarFechas();

						}
					})

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