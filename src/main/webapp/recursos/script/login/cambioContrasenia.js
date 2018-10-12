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
	
	/**
	 * Funcion que inicializa componentes y configuraciones genericas
	 */
	var init = function(){		
		moduloValidadorJS.inicializaValidator();
		iniciaValidacionesFormulario();
		iniciaEventoBotoncontrasenia();
	};
		
		var formCambioContrasenia = "#formCambioContrasenia";		
		var btnCambioContrasenia="#btnCambioContrasenia";
		var accesoClave= "#accesoClave";
		var accesoClave1= "#accesoClave1";
		var accesoClave2= "#accesoClave2";
		$("#formLogin").css("display","none");
		$("#regla1").css("display","none");
		$("#regla2").css("display","none");
		$("#regla3").css("display","none");
		$("#contraseniaCambiada").css("display","none");
		$("#btnMensajeInicial").click(function(){
			$("#mensajeInicial").css("display","none");
			$("#formLogin").css("display","inline");
			$("#contraseniaCambiada").css("display","none");
			
		});
		
//		$("#btnCambioContrasenia").click(function(){
//			//event.preventDefault();
//			
//			$("#formLogin").css("display","none","disabled","true");
//			$("#contraseniaCambiada").css("display","inline");
//			var valor=$('#accesoClave1').val();
//			$("#aC1").val(valor);
//		});
//		
//		$("#accesoClave1").keyup(function(){
//			var valor=$('#accesoClave1').val();
//			var regla2=/^(?=\w*\d)(?=\w*[A-Z])(?=\w*[a-z])/;
//			var regla3=/\s+/;
//			
//			
//			 var resRegla2=regla2.test(valor);
//			 var resRegla3=regla3.test(valor);
//			 
//			 if(valor.length==8){
//				 $("#regla1").css("display","inline"); 
//				 
//				 if(resRegla3==false){
//					 $("#regla3").css("display","inline"); 
//				 }else{
//					 $("#regla3").css("display","none");
//				 }
//			 }else{
//				 $("#regla1").css("display","none");
//			 }
//			if(resRegla2==true){
//		    	$("#regla2").css("display","inline");
//		    }else{
//		    	$("#regla2").css("display","none");
//		    }
//		});
//		
//		$("#accesoClave1").blur(function(){
//			 	var valor=$('#accesoClave1').val();
//			    var re = /^(?=\w*\d)(?=\w*[A-Z])(?=\w*[a-z])\S{8,8}$/;
//			    var res= re.test(valor);
//			  if(res==false){
//				  
//				  document.getElementById("btnCambioContrasenia").disabled = true;
//			  }else{
//				  document.getElementById("btnCambioContrasenia").disabled = false; 
//			  }
//		});
		var dato=function(){
			var va=$('#accesoClave1').val();
			
			$('#aC1').val(va);
			//$('.prueba #aC1').val(va);
			
		}
		
	
		/**
		 * Funcion que inicializa los eventos del boton
		 */
		var iniciaEventoBotoncontrasenia = function(){
			
			$(btnCambioContrasenia).click(function(){
				var valor=$('#accesoClave1').val();
				event.preventDefault();
			    var re = /^(?=\w*\d)(?=\w*[A-Z])(?=\w*[a-z])\S{8,8}$/;
			    var res= re.test(valor);
			    
				if(($(formCambioContrasenia).valid()==true) && (res==true)){
					dato();
					if(accesoClave1===accesoClave2){
						
					$(formCambioContrasenia).submit();
					
					}
					$("#formLogin").css("display","none","disabled","true");
					$("#contraseniaCambiada").css("display","inline");
					
				}else{
					$("#mensajeError").css("display","inline");
					
				}		
			});
			
			$(accesoClave1).keyup(function(event){
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
						"maxlength":8,
						"minlength":8
					},
					"accesoClave2":{
						"soloCaracteres": true,
						"required":true,
						"maxlength":8,
						"minlength":8
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

