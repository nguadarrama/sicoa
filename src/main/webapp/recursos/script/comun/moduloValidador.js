/*
* ****************************************************
* * Aplicaci&oacute;n Base
* * Versi&oacute;n 1.0.0
* * Secretar&iacute;a de Gobernaci&oacute;n - DGTIC
* ****************************************************
* 
* 
* Script que complementa las funcionalidades de validacion a nivel cliente.
* 
* Para hacer uso de este modulo se requiere se haya importado la libreria assets/jquery-validator/* 
*/
var moduloValidadorJS = (function() {

	/**Variable que contiene los mensajes de error en español*/
	var VALIDADOR_MENSAJES = {
		required: "Este campo es obligatorio.",
  	  	remote: "Por favor, rellena este campo.",
  	  	email: "Por favor, escribe una direcci\u00f3n de correo v\u00e1lida",
  	  	url: "Por favor, escribe una URL v\u00e1lida.",
  	  	date: "Por favor, escribe una fecha v\u00e1lida.",
  	  	dateISO: "Por favor, escribe una fecha (ISO) v\u00e1lida.",
  	  	number: "Por favor, escribe un n\u00famero entero v\u00e1lido.",
  	  	digits: "Por favor, escribe s\u00f3lo d\u00edgitos.",
  	  	creditcard: "Por favor, escribe un n\u00famero de tarjeta v\u00e1lido.",
  	  	equalTo: "Por favor, escribe el mismo valor de nuevo.",
  	  	accept: "Por favor, escribe un valor con una extensi\u00f3n aceptada.",
  	  	maxlength: jQuery.validator.format("Por favor, no escribas m\u00e1s de {0} caracteres."),
  	  	minlength: jQuery.validator.format("Por favor, no escribas menos de {0} caracteres."),
  	  	rangelength: jQuery.validator.format("Por favor, escribe un valor entre {0} y {1} caracteres."),
  	  	range: jQuery.validator.format("Por favor, escribe un valor entre {0} y {1}."),
  	  	max: jQuery.validator.format("Por favor, escribe un valor menor o igual a {0}."),
  	  	min: jQuery.validator.format("Por favor, escribe un valor mayor o igual a {0}."),
  	  	extension: "Tipo de archivo no valido",
  	  	/*Etiquetas metodos personalizados*/
        requerido: "Este campo es obligatorio.",
        fecha: "La fecha no cumple con el formato (dd/mm/yyyy).",
        numero: "Solo se permiten n\u00fameros.",
        decimales : "Solo se permiten n\u00fameros decimales.",
        texto: "El texto contiene caracteres no validos",
        datoVacio: "Dato requerido",
        jpg: "Solo se admiten Imagenes de formato .jpg",
        maximo_fecha_actual : "No debe ser mayor a la fecha actual",
        pattern : jQuery.validator.format("Por favor, cumple con el patron especificado")
        	  
    };
	
	/**
	 * Inicializa las etiquetas generales del plugin jquery.validator
	 */
	var __inicializaEtiquetasGenerales = function(){
					//Inicializa los mensajes de jquery validator a español
					jQuery.extend(jQuery.validator.messages,VALIDADOR_MENSAJES);
		};
   
	/**
	 * Agrega los metodos personalizados para uso del validator
	 * Metodos integrados 
	 *  - soloNumeros
	 *  - soloDecimales
	 *  - soloCaracteres
	 *  - formatoFechas
	 *  - noVacio
	 *  - validaExtensionJpg
	 *  - fechaActualMaximo
	 */
	var __agregaMetodosPersonalizados = function(){
								        jQuery.validator.addMethod("soloNumeros", _evaluaSoloNumeros, VALIDADOR_MENSAJES.numero);
								        jQuery.validator.addMethod("soloDecimales", _evaluaDecimales, VALIDADOR_MENSAJES.decimales);
								        jQuery.validator.addMethod("soloCaracteres", _evaluaCaracteres, VALIDADOR_MENSAJES.texto);
								        jQuery.validator.addMethod("formatoFechas", _evaluaFechas,VALIDADOR_MENSAJES.fecha);
								        jQuery.validator.addMethod("noVacio", _evaluaVacio,VALIDADOR_MENSAJES.datoVacio);
								        jQuery.validator.addMethod("validaExtensionJpg", _evaluaJpg,VALIDADOR_MENSAJES.jpg);
								        jQuery.validator.addMethod("fechaActualMaximo", __fechaActualMaximo, VALIDADOR_MENSAJES.maximo_fecha_actual);
									};
									
									
	/**
	 * Evalua que el valor del elemento es un numero entero bajo una expresion regular
	 * 
	 * @param value valor a evaluar
	 * @param element componente que se esta evaluando
	 * 
	 * @return true, cuando el valor sea correcto
	 */
	var _evaluaSoloNumeros = function(value, element) {
						        return this.optional(element) || /^\d*$/i.test(value);
						    };

    /**
	 * Evalua que el valor del elemento es un numero decimal bajo una expresion regular
	 * 
	 * @param value valor a evaluar
	 * @param element componente que se esta evaluando
	 * 
	 * @return true, cuando el valor sea correcto 
	 */
    var _evaluaDecimales = function(value, element) {
					        return this.optional(element) || /^\d*[\.]*\d*$/i.test(value);
					    };

    /**
	 * Evalua que el valor del elemento es una cadenas con valores alfanumericos y .\-_()\/:
	 * 
	 * @param value valor a evaluar
	 * @param element componente que se esta evaluando
	 * 
	 * @return true, cuando el valor sea correcto  
	 */
    var _evaluaCaracteres = function(value, element) {
					        return this.optional(element) || /^[a-zA-Z0-9\s\u00f1\u00d1\u00e1\u00e9\u00ed\u00f3\u00fa\u00c1\u00c9\u00cd\u00d3\u00da.,\-_()\\/:]*$/i.test(value);
					    };
					    
    /**
	 * Evalua que el valor del elemento es una fecha
	 *  
	 * @param value valor a evaluar
	 * @param element componente que se esta evaluando
	 * 
	 * @return true, cuando el valor sea correcto 
	 */
    var _evaluaFechas = function(value, element) {
				        return this.optional(element) || /^(0[1-9]|[12][0-9]|3[01])[/](0[1-9]|1[012])[/]\d\d\d\d$/i.test(value);
				    };
				    
    var _evaluaJpg = function(value, element) {
				        if (!value.match(/(?:jpg)$/)) {
				            return false;
				        } else {
				            return true;
				        }
				    };
				    
    /**
	 * Evalua que el valor del elemento no es vacio
	 *  
	 * @param value valor a evaluar
	 * @param element componente que se esta evaluando
	 * 
	 * @return true, cuando el valor sea correcto 
	 */
    var _evaluaVacio = function(value, element) {
				        if (value === null || value === "") {
				            return false;
				        } else {
				            return true;
				        }
				    };
    
				    
	/**
	 * Evalua que el valor capturado es menor o igual al dia en curso
	 *  
	 * @param value valor a evaluar
	 * @param element componente que se esta evaluando
	 * 
	 * @return true, cuando el valor sea correcto 
	 */			    
	var __fechaActualMaximo = function(value, element) {
		var fecha = null;
		if(value != null && value != ""){
			if($(element).datepicker){
				fecha = $(element).datepicker('getDate');
			} else {
				fecha = new Date(value);
			}	
			var current = new Date();
			if(fecha <= current){
				return true;
			} else 
				return false;
			
		} else return false;
		
	}; 
			
    /**
	 * Inicializa las configuraciones personalizadas del validador
	 * 
	 * 
	 */    
    var inicializaValidator = function() {
					    	__inicializaEtiquetasGenerales();
					    	__agregaMetodosPersonalizados();
					
					    	//Configuraciones generales
					        jQuery.validator.setDefaults({
						            errorPlacement: function(error, element) {
						                var $elementParent = $(element).parent();
                                        var elementError = element;
                                        if($elementParent.hasClass("input-group")) {
                                           elementError = $elementParent;
                                        }
						                generaErrorDiv(elementError, error);
						            },
						            errorElement: "div",						            
				                    ignore : ":hidden :not(input[type='file'])"
					        });
					    };
						 
		/**
		 * Funcion que genera la etiqueta de error a presentar
		 * 
		 * 
		 * @param inputSelector
		 * @param errores
		 */   
	var generaErrorDiv = function(inputSelector, errores){
						var inputReferencia = $(inputSelector)[0];
				    	
						
				        if(errores != null && errores.length != 0){ 
					        	
						        var divWarning = $("<div></div>", {"class":"text"} );										    	
				                $.each(errores, function(){
				                        var error = this;
				                        var label = $("<label></label>", {"class":"error"});
				                        $(label).html(error);
				                                        
				                        $(divWarning).append(label);
				                });
				                $(inputReferencia).addClass("hasError");
				                $(inputReferencia).parent().append(divWarning);
				        }
				    };
								    

	/**
	 * Funciones publicas.
	 */			   
    return {
        inicializaValidator: inicializaValidator
    };
})();


