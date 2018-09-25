var demoJqueryValidatorJS  = (function() {
	
	var init = function(){
		inicializaValidator();
		inicializaBotonValidar();
	};
	
	
	var inicializaValidator= function(){
		$("#formulario-validado").validate({
            rules: {
                "email": {
                    "email": true
                },"select":{
                	"required":true
                },"contrasenia":{
                	"required":true,
                	"minlength" : 3                	
                },"nombre":{
                	"soloCaracteres":true          	
                },"numeros":{
                    "soloNumeros": true
                },"archivo":{
                    "required": true
                },"texto" : {
                	"maxlength" : 20 
                }
            },
            messages: {
            	"archivo" : {
            		"required" : "Debe adjuntar un archivo..."
				}
			}
        });
	};
	
	var inicializaBotonValidar = function(){
		$("#btn-validar").click(function(){
			var capturaValida = $("#formulario-validado").valid();
			if(capturaValida){
				commonFunctionsJS.alert("La captura es valida");
			}
			
		});
		
	};
	

	return {
		init : init		
    };	
})();