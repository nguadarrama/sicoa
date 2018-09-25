/*
* ****************************************************
* * Aplicaci&oacute;n Base
* * Versi&oacute;n 1.0.0
* * Secretar&iacute;a de Gobernaci&oacute;n - DGTIC
* ****************************************************
* 
*/


//Se configura para que las busquedas ajax no se manejen por medio de cache (Bug IExplorer)
$.ajaxSetup({ cache:false });


/**
 * Modulo que contiene funciones varias para uso del proyecto
 * 
 */
var commonFunctionsJS  = (function() {
	
	/**Variables que definen el contexto de los aplicativos*/
    var PATH_RAIZ_SERVICIOS = "/codigo-base-webservice";
	var PATH_WEB			= "/codigo-base-front";
	
	/**
	 * Funcion que inicializa o configura los aspectos generales de la aplicacion
	 */
	var initFuncionesGenerales = function() {
				//	inicializa el modulo validador
				moduloValidadorJS.inicializaValidator();
			};
						

	/**
	 * Funcion que integra el path de los recursos rest a los endpoint relativos
	 * Ejemplo
	 * var path = "/recurso";
	 * commonFunctionsJS.obtenerUrlServicios(path); // "{contextoAplicativoRecursosRest}/recurso"
	 * 
	 * 
	 * @param pathRelativoServicio path relativo al servicio
	 * @return el path completon incluyendo el contexto del proyecto REST
	 */
	var obtenerUrlServicios = function(pathRelativoServicio){
						    return PATH_RAIZ_SERVICIOS+pathRelativoServicio;
						};
						
	/**
	 * Funcion que integra el path del aplicativo web al path relativo solicitado
	 * 
	 * Ejemplo
	 * var path = "/recursoWeb";
	 * obtenerUrlWeb(path); // "{contextoAplicativoWeb}/recursoWeb"
	 * 
	 * @param pathRelativoWeb path relativo al servicio
	 * @return el path completon incluyendo el contexto del proyecto  WEB
	 */
	var obtenerUrlWeb = function(pathRelativoWeb){
						    return PATH_WEB+pathRelativoWeb;
						};



    /**
     * Funcion que ejecuta una funcion ajax con una configuracion predeterminada
     * Nota : Las peticiones que realiza ya integran el token de seguridad para consumo de recursos REST.
     * 
     * Ejemplo:
     * 		var opcionesAjax = {
	 *			url : commonFunctionsJS.obtenerUrlServicios("/demo/mensaje/POST"),
	 *			data : JSON.stringify(parametros),
	 *			type : "POST",
	 *			contentType : "application/json",
	 *			beforeSend : function(){},
	 *			success : function(respuesta){
	 *				commonFunctionsJS.alert(JSON.stringify(respuesta));
	 *			},
	 *			error : function(xhq, error){
	 *	    		if(xhq.responseJSON){
	 *	    			commonFunctionsJS.alert(JSON.stringify(xhq.responseJSON));	
	 *	    		} else {
	 *	    			commonFunctionsJS.alert("Error");
	 *	    		}		    		
	 *	    	}
	 *		};
	 *		commonFunctionsJS.ajaxSubmit(opcionesAjax);	
     * 
     * 
     * Las opciones disponibles a poder modificar son
     * var opcionesDefault = {
	 *							    url  : null,		//Url del servicio que atendera la peticion
	 *								type : "POST",		//Tipo de peticion a enviar : default - POST
	 *								data : {},			//Datos a enviar
	 *								async : true, 		//Asincrono activado por default
	 *				                cache: false,
	 *								beforeSend 	: null,	//Funcion a ejecutar previo a enviar la peticion
	 *								complete 	: null, //Funcion a ejecutar una vez que se concluya la peticion, sea error o exito
	 *								error 		: null,	//Funcion a ejecutar cuando es un error
	 *								success		: null	//funcion a ejecutar cuando es una respuesta de exito
	 *							};
     *
     *	@param opciones Las opciones posibles a cambiar
     *	
     *  @return la promesa de la peticion asincrona
     */
	var ajaxSubmit = function(opciones){
						        var opcionesDefault = {
					                                    contentType : 'application/x-www-form-urlencoded', //Tipo de dato que genera
					                                    dataType : "json",
					                                    url  : null,		//Url del servicio que atendera la peticion
					                                    type : "POST",		//Tipo de peticion a enviar : default - POST
					                                    data : {},			//Datos a enviar
					                                    async : true, 		//Asincrono activado por default
					                                    cache: false,
					                                    beforeSend 	: null,	//Funcion a ejecutar previo a enviar la peticion
					                                    complete 	: null, //Funcion a ejecutar una vez que se concluya la peticion, sea error o exito
					                                    error 		: null,	//Funcion a ejecutar cuando es un error
					                                    success		: null	//funcion a ejecutar cuando es una respuesta de exito
					                                };
					
					            var opcionesAjax = $.extend({}, opcionesDefault, opciones);
					            var opcionesRequestAjax = {
					                                            contentType : opcionesAjax.contentType,
					                                            dataType : opcionesAjax.dataType,
					                                            url: opcionesAjax.url,
					                                            type : opcionesAjax.type,
					                                            data : opcionesAjax.data,
					                                            headers : securityJS.getTokenService(),
					                                            beforeSend :  function(){
					
					                                                            if(opcionesAjax.beforeSend !=  null){
					                                                                opcionesAjax.beforeSend();
					                                                            }
					                                                        },
					                                            complete : function() {
					                                                            if(opcionesAjax.complete !=  null){
					                                                                opcionesAjax.complete();
					                                                            }
					                                                        },
					                                            async : opcionesAjax.async,
					                                            success : opcionesAjax.success,
					                                            error : function(jqXHR, textStatus, errorThrown){
					                                                    if(opcionesAjax.error != null){
					                                                        opcionesAjax.error(jqXHR, textStatus, errorThrown);
					                                                    } else {
					                                                        commonFunctionsJS.alert("Ocurrio un error en la petición ", BootstrapDialog.TYPE_DANGER);
					                                                    }
					                                                }
					
					
					
					                                        };
					            var promisePing = securityJS.validateSesion();
							    var promiseRequest = promisePing.then(function(){
							        return $.ajax(opcionesRequestAjax);
							    });
								return promiseRequest;
						};
		
		
		
	/**
	 * Funcion que inicializa/limpia un formulario.
	 * 
	 * @param selectorForm El selector con que se identifica el formulario a resetear
	 */		    
	var limpiarFormulario = function (selectorForm) {
			    	var formToClean = $(selectorForm);
			    	formToClean.find("INPUT[type='radio']").removeAttr("checked");
			    	formToClean.find("INPUT[type='checkbox']").removeAttr("checked");
			    	formToClean[0].reset();
			    	
			    	if(formToClean.validate){
			    		formToClean.validate().resetForm();	
			    		formToClean.validate().reset();	
			    		formToClean.find(".error").removeClass("error");
			    	}
			    	
	};
	
	
	/**
	 * Funcion que genera un modal para un alert personalizado.
	 *  
	 * Requiere que se importe la libreria "assets/bootstrap_dialog/*"
	 * 
	 * Ejemplo
	 * commonFunctionsJS.alert("Esta es una alerta");
	 * commonFunctionsJS.alert("Esta es una alerta", BootstrapDialog.TYPE_INFO);
	 * 
	 * 
	 * @param mensajes Mensaje a mostrar
	 * @param tipomensaje Clasificador para el tipo de alerta a mostrar
	 * 		   Los tipos disponibles son :
	 * 			- BootstrapDialog.TYPE_WARNING
	 * 			- BootstrapDialog.TYPE_INFO
	 * 			- BootstrapDialog.TYPE_PRIMARY
	 * 			- BootstrapDialog.TYPE_SUCCESS
	 * 			- BootstrapDialog.TYPE_DANGER
	 * 
	 */
	var alert = function(mensajes, tipomensaje) {
				
				var tipoMensaje = (tipomensaje == null)? BootstrapDialog.TYPE_PRIMARY : tipomensaje;
    	 		var mensajesCadena =""    	 		 
    	 		if(mensajes instanceof Array) {
    	 			$.each(mensajes, function(){
    	 				mensajesCadena += "<li>"+this+"</li>"
    	 			});
    	 		} else {
    	 			mensajesCadena += "<li>"+mensajes+"</li>"
    	 		}    	 		
    	 
		    	BootstrapDialog.show({
	                 type : tipoMensaje, 
	                 title : 'Atenci&oacute;n',
	                 message : "<ul>"+mensajesCadena+"</ul>",
	                 closable : false,
	                 buttons : [{
	                             label : "Aceptar",
	                             cssClass : 'btn-danger',
	                             autospin : true,
	                             action : function(dialog) {
	                            	  dialog.close();
	                             }                             
	                    	}]
		    	});
	};
	
	/**
	 * Funcion que genera un modal para realizar una confirmacion personalizada.
	 * 
	 * Requiere que se importe la libreria "assets/bootstrap_dialog/*"
	 * 
	 * Ejemplo
	 * commonFunctionsJS.confirmacion("Seguro", BootstrapDialog.TYPE_WARNING, function(){console.log('hola')});
	 * 
	 * @param mensajes Mensaje a mostrar
	 * @param tipomensaje Clasificador para el tipo de alerta a mostrar
	 * 		   Los tipos disponibles son :
	 * 			- BootstrapDialog.TYPE_WARNING
	 * 			- BootstrapDialog.TYPE_INFO
	 * 			- BootstrapDialog.TYPE_PRIMARY
	 * 			- BootstrapDialog.TYPE_SUCCESS
	 * 			- BootstrapDialog.TYPE_DANGER
	 * @param funcion Funcion a ejecutar en la opcion Aceptar
	 * 
	 */
     var confirmacion = function(mensaje, tipomensaje, funcion) {			    	 					    	 
    	 				var tipoMensaje = (tipomensaje == null)? BootstrapDialog.TYPE_PRIMARY : tipomensaje;
    	 				funcion = (funcion != null)? funcion : function(){commonFunctionsJS.alert()("No definio funcion a ejecutar al aceptar (3er parametro)");};
    	 				
    	 				BootstrapDialog.show({
				                 type : tipoMensaje, 
				                 title : 'Atenci&oacute;n',
				                 message : mensaje,
				                 closable : false,
				                 buttons : [{
				                             	label : "Cancelar",
				                             	cssClass : 'btn-danger',
				                             	autospin : true,
				                             	action : function(dialog) { dialog.close(); }
				                 			 }, { 
				                            	 label : "Aceptar",
				                            	 cssClass : 'btn-danger', 
				                            	 action : function(dialogItself) {
					                            	  dialogItself.close();
					                            	  funcion();					                            	  
					                             }       	
				                    		}]
					    	 });
				     };
					     
     /**
     * Funcion que genera un div de alerta de bootstrap que se cierra por tiempo una fraccion de tiempo.
     * 
     * @param mensaje  		Mensaje a presentar 
     * @param tipoAlerta 	Clasifica el tipo de alerta que se generara
     * 						Las opciones disponibles son :
     * 							- alert-success
	 * 							- alert-info
     * 							- alert-warning
     * 							- alert-danger
     * @param selector 		Identificador del tag html donde se integrara la alerta 
     * @param tiempo		Numero de segundos que se mostrara el mensaje antes de cerrar		
     * 
     */
	var  alertDismiss = function(mensaje, tipoAlerta, selector, tiempo) {
			       		var time=5000;
			       		var select='#mensajeConfirmacion';
			       		var alert="<div id=\"alertDismiss\"class=\"alert "+tipoAlerta+" alert-dismissible\" role=\"alert\"><button type=\"button\" class=\"close\" data-dismiss=\"alert\" " +
			       					"aria-label=\"Close\"><span aria-hidden=\"true\">&times;</span></button>"+mensaje+"</div>";

			       		$(select).find("#alertDismiss").remove();
			       		if(selector!=null)
		                   	select = selector;
		                
		                if(tiempo!=null) 
		                    time=1000*tiempo;
		                

		                $(select).find("#alertDismiss").remove();
		                $(select).append(alert);

		                $("#alertDismiss").fadeTo(time, 500).slideUp(500, function(){
		                	$("#alertDismiss").slideUp(500);
		                });

             	};
     
 
	
					     

       /**
        * Funcion que inicializa un componente como Datepicker.
        * 
        *  Requiere que se importe la libreria "assets/date-picker/*"
        *
        * Ejemplo : 
        *   	var inputFechaHasta = $(FORM_BUSQUEDA).find("INPUT[name='fechaHasta']");
        * 		commonFunctionsJS.inicializarCalendario(inputFechaDesde);
        * 		commonFunctionsJS.inicializarCalendario(inputFechaDesde, { changeYear: false });
        * 
        * @param componenteSelector componente del selector que se inicializara como un datepicker
        * @param options opciones para personalizar el componente
        * 
        */
		var inicializarCalendario = function(componenteSelector, options)  {
						               if( typeof $.datepicker === 'undefined' && !jQuery.isFunction( $.datepicker ) ) {
						            	   commonFunctionsJS.alert("<i>Function <b>inicializarCalendario()</b></i> <br> Requiere la libreria <b>jquery-ui-datepicker (js, css)</b>, favor de importarla", BootstrapDialog.TYPE_WARNING);
						               }
						
						                var optionsDefaultCalendar = {   changeMonth: true,
						                                          changeYear: true,
						                                          showButtonPanel : false};
						                
						                
						               var optionsCalendar = $.extend({}, optionsDefaultCalendar, options);
						               $(componenteSelector).datepicker(optionsCalendar)
						                           .on("change",
						                               function () {
						                                   var id = $(this).attr("id");
						                                   var val = $("label[for='" + id + "']").text();
						                                   $("#msg").text(val + " changed");
						                               });
						           };
          	
           
       	/**
       	 * Funcion que habilita los tooltip de bootstrap 
       	 */
		var inicializatooltip = function(selector){
       						    $(selector).tooltip();
       						};
       						
       						
       						
        /**
        * Funcion que genera un bloqueo de pantalla, mediante un overlay. 
        * 
        */
        var bloqueaPantalla = function(){
	            var divOverlay = $("<div></div>", { "id" : "overlayId",
	                                                "class" : "overlay"
	                                                });
	            var divWaitOverlay = $("<div></div>", {"id" : "overlayModalId",
	                                                    "class" : "overlayModal col-md-offset-4 col-md-4"});
	            divWaitOverlay.html("<i class='fa fa-spinner' aria-hidden='true'></i><span>Cargando...</span>");
	
	            var documentElement = $(document.body);
	            documentElement.append(divOverlay);
	            documentElement.append(divWaitOverlay);
        };
    
        
        /**
         * Funcion que desbloquea la pantalla, mediante eliminando el overlay 
         * 
         */
        var desbloqueaPantalla = function(){
            $("#overlayModalId").remove();
            $("#overlayId").remove();
        };
       		
        
	/**
	 * Funciones publicas.
	 */
	return {
		initFuncionesGenerales 		: initFuncionesGenerales,
		
		obtenerUrlServicios 		: obtenerUrlServicios,
		obtenerUrlWeb 				: obtenerUrlWeb,
		
		ajaxSubmit 					: ajaxSubmit,
		
		limpiarFormulario			: limpiarFormulario,
		
		alert 						: alert,
		alertDismiss				: alertDismiss,
		confirmacion				: confirmacion,
		
	    inicializarCalendario 		: inicializarCalendario,
	    inicializatooltip 			: inicializatooltip,
	    
	    bloqueaPantalla: bloqueaPantalla,
	    desbloqueaPantalla : desbloqueaPantalla 
    };	
})();