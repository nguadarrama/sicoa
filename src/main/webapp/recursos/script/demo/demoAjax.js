var demoAjaxJS  = (function() {
	
	var init = function(){
		initBoton();
	};
	
	
	var initBoton = function(){
		$("#btn-post").click(function(){
			peticionAsincronaPOST();
		});
		$("#btn-get").click(function(){
			peticionAsincronaGET();
		});
	};
	
	var peticionAsincronaGET = function(){
		var opcionesAjax = {
				url : commonFunctionsJS.obtenerUrlServicios("/demo/mensaje"),
				data : {},
				type : "GET",
				beforeSend : function(){},
				success : function(respuesta){
					commonFunctionsJS.alert(JSON.stringify(respuesta));
				},	
		    	error : function(xhq, error){
		    		if(xhq.responseJSON){
		    			commonFunctionsJS.alert(JSON.stringify(xhq.responseJSON));	
		    		} else {
		    			commonFunctionsJS.alert("Error");
		    		}
		    		
		    	}
			};
			commonFunctionsJS.ajaxSubmit(opcionesAjax);	
	};
	
	
	var peticionAsincronaPOST = function(){
		var nombre = $("#formulario-ajax").find("[name='nombre']").val();
		var parametros = {};
		parametros['nombre'] = nombre;
		
		var opcionesAjax = {
				url : commonFunctionsJS.obtenerUrlServicios("/demo/mensaje/POST"),
				data : JSON.stringify(parametros),
				type : "POST",
				contentType : "application/json",
				beforeSend : function(){},
				success : function(respuesta){
					commonFunctionsJS.alert(JSON.stringify(respuesta));
				},
				error : function(xhq, error){
		    		if(xhq.responseJSON){
		    			commonFunctionsJS.alert(JSON.stringify(xhq.responseJSON));	
		    		} else {
		    			commonFunctionsJS.alert("Error");
		    		}
		    		
		    	}
			};
			commonFunctionsJS.ajaxSubmit(opcionesAjax);	
	};

	return {
		init : init		
    };	
})();