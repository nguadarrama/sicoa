$(document).ready(function() {
	
	$("#fechaInicio").datepicker({ 
	       beforeShowDay: $.datepicker.noWeekends,
	      
	   });
	$("#fechaFin").datepicker({ 
	       beforeShowDay: $.datepicker.noWeekends,
	   });
	
}); 