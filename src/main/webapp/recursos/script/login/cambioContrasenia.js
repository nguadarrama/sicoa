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
var CambioContraseniaJS =  (function() {

		
		var formCambioContrasenia = "#formCambioContrasenia";		
		var btnCambioContrasenia="#btnCambioContrasenia";
		var accesoClave= "#accesoClave";
		var accesoClave1= "#accesoClave1";
		var accesoClave2= "#accesoClave2";
		$("#formLogin").css("display","none");
		$("#regla1").css("display","none");
		$("#regla2").css("display","none");
		$("#regla3").css("display","none");
		$("#btnMensajeInicial").click(function(){
			$("#mensajeInicial").css("display","none");
			$("#formLogin").css("display","inline");
			
		});
		$("#btnCambioContrasenia").click(function(){
			$("#formLogin").css("display","none");
			$("#contraseniaCambiada").css("display","inline");
		});
		
		$("#accesoClave1").keyup(function(){
			var valor=$('#accesoClave1').val();
			var regla2=/^(?=\w*\d)(?=\w*[A-Z])(?=\w*[a-z])/;
			var regla3=/\s+/;
			
			
			 var resRegla2=regla2.test(valor);
			 var resRegla3=regla3.test(valor);
			 
			 if(valor.length==8){
				 $("#regla1").css("display","inline"); 
				 
				 if(resRegla3==false){
					 $("#regla3").css("display","inline"); 
				 }else{
					 $("#regla3").css("display","none");
				 }
			 }else{
				 $("#regla1").css("display","none");
			 }
			if(resRegla2==true){
		    	$("#regla2").css("display","inline");
		    }else{
		    	$("#regla2").css("display","none");
		    }
		});
		
		$("#accesoClave1").blur(function(){
			 	var valor=$('#accesoClave1').val();
			    var re = /^(?=\w*\d)(?=\w*[A-Z])(?=\w*[a-z])\S{8,8}$/;
			    var res= re.test(valor);
			  if(res==false){
				  
				  document.getElementById("btnCambioContrasenia").disabled = true;
			  }else{
				  document.getElementById("btnCambioContrasenia").disabled = false; 
			  }
		});
	
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

