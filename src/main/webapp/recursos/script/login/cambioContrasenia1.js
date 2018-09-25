/*
* ****************************************************
* * Aplicaci&oacute;n Base
* * Versi&oacute;n 1.0.0
* * Secretar&iacute;a de Gobernaci&oacute;n - DGTIC
* ****************************************************
* 
* 
* Script utilizado por el modulo de login
*/
var CambioContrasenia1JS =  (function() {

		
		var formCambioContrasenia = "#formCambioContrasenia";		
		var btnCambioContrasenia="#btnCambioContrasenia";
		var accesoClave1= "#accesoClave1";
		var accesoClave2= "#accesoClave2";
	
		/**
		 * Funcion que inicializa componentes y configuraciones genericas
		 */
		var init = function(){		
			moduloValidadorJS.inicializaValidator();
			iniciaValidacionesFormulario();
			iniciaEventoBotoncontrasenia();
		};
	
		/**
		 * Funcion que inicializa los eventos del boton
		 */
		var iniciaEventoBotoncontrasenia = function(){
			$(btnCambioContrasenia).click(function(){
				if($(formCambioContrasenia).valid()){
					if(accesoClave1===accesoClave2){
					$(formCambioContrasenia).submit();
					}
				}		
			});
			
			$(accesoClave1).keyup(function(event){
			    if(event.keyCode == 13){
			    	if($(formCambioContrasenia).valid()){
						$(formCambioContrasenia).submit();
					}
			    }
			});
			$(accesoClave2).keyup(function(event){
			    if(event.keyCode == 13){
			    	if($(formCambioContrasenia).valid()){
						$(formCambioContrasenia).submit();
					}
			    }
			});
		};
		
		/**
		 * Funcion que inicializa las validaciones a aplicar el formulario que contiene las credenciales
		 */
		var iniciaValidacionesFormulario = function(){
			var $form = $(formCambioContrasenia);
			$form.validate({
				rules: {
					"accesoClave1": {
						"soloCaracteres": true,
						"required":true,
						"maxlength":30
					},
					"accesoClave2":{
						"soloCaracteres": true,
						"required":true,
						"maxlength":30
					}
			    }
			});		
		};


		/**
		 * Metodos publicos.
		 */
		return {
					init : init					
				};	
})();

