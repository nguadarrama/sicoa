$(document).ready(function() {
	
	$("#fechaInicio").datepicker({ 
		minDate: 1,
	       beforeShowDay: $.datepicker.noWeekends,
	      
	   });
	$("#fechaFin").datepicker({ 
		minDate: 1,
	       beforeShowDay: $.datepicker.noWeekends,
	   });
	
}); 