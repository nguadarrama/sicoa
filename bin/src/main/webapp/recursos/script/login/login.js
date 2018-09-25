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
var loginJS =  (function() {

		
		var formlogin = "#formLogin";		
		var btnLogin="#btnLogin";
		var accesoClave= "#accesoClave";
	
		/**
		 * Funcion que inicializa componentes y configuraciones genericas
		 */
		var init = function(){		
			moduloValidadorJS.inicializaValidator();
			iniciaValidacionesFormulario();
			iniciaEventoBotonLogin();
		};
	
		/**
		 * Funcion que inicializa los eventos del boton
		 */
		var iniciaEventoBotonLogin = function(){
			$(btnLogin).click(function(){
				if($(formlogin).valid()){
					$(formlogin).submit();
				}		
			});
			
			$(accesoClave).keyup(function(event){
			    if(event.keyCode == 13){
			    	if($(formlogin).valid()){
						$(formlogin).submit();
					}
			    }
			});
		};
		
		/**
		 * Funcion que inicializa las validaciones a aplicar el formulario que contiene las credenciales
		 */
		var iniciaValidacionesFormulario = function(){
			var $form = $(formlogin);
			$form.validate({
				rules: {
					"accesoUsuario": {
						"soloCaracteres": true,
						"required":true,
						"maxlength":30
					},
					"accesoClave":{
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

