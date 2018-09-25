/*
* ****************************************************
* * Aplicaci&oacute;n Base
* * Versi&oacute;n 1.0.0
* * Secretar&iacute;a de Gobernaci&oacute;n - DGTIC
* ****************************************************
* 
* 
* Script utilizado para el manejo de ciertas caracteristicas de seguridad del lado cliente
*/
var securityJS = (function(){
	
	/**Arreglo que se carga con los permisos asignados al usuario en sesion*/
	var __features = [];
	
	/**
	 * Funcion que realiza la verificacion de vigencia en la sesion del aplicativo WEB, 
	 * mediante una peticion asincrona
	 * 
	 * @returns La promesa de la peticion asincrona
	 */
	var validateSesion = function(){
		var promiseRequest=$.ajax({
			  	url : commonFunctionsJS.obtenerUrlWeb("/sesion/ping"),
		        type: "HEAD",
		        error:function(jqXHR, textStatus, errorThrown){
					if( jqXHR != null &&
							jqXHR.status != null && 
							jqXHR.status == 401 ){
						  	window.location.href =commonFunctionsJS.obtenerUrlWeb("/login?timeout=true");
					} else{
						if(opcionesAjax.error != null){
							opcionesAjax.error(jqXHR, textStatus, errorThrown);
						} else {
							commonFunctionsJS.alert("Ocurrio un error en la petición ", BootstrapDialog.TYPE_DANGER);
						}
					}		
					
				}
		    
		    });
		 return promiseRequest;	
	};
	
	
	/**
	 * Funcion que verifica si el usuario tiene asignado alguno de los permiso especificados, requiere que previamente se invoque el metodo 
	 * securityJS.loadFeatures
	 * 
	 * Ejemplo.
	 * var tienePermiso = securityJS.hasAnyAuthority('permiso');
	 * ...
	 * var tienePermiso = securityJS.hasAnyAuthority(['permiso1','permiso2']);
	 * 
	 * @param features Cadena o arreglo de cadena a verificar
	 * @returns true, si tiene alguno asignado
	 */
	var hasAnyAuthority = function(features){ 
								var hasAuthority = false;
								
								if(features instanceof Array){
									$.each(features, function(){
										var feature = this;				
										if( __containFeature(feature) ) {
											hasAuthority = true;		
										}					
									});
								} else if(typeof features == "string") {
									 hasAuthority = __containFeature(features)
								} else {
									hasAuthority = false;
								}
								
								return hasAuthority;
							};
	
	/**
	 * Funcion encargada de obtener los permisos asignados al usuario en sesion de manera asincrona.
	 * Se recomienda hacerlo una sola vez al cargar la vista
	 * 
	 */
	var loadFeatures = function(){
								 var opcionesAjax = {
						                 url : commonFunctionsJS.obtenerUrlWeb("/sesion/features"),
						                 data : {},
						                 type : "GET",
						                 beforeSend : function(){
						                     commonFunctionsJS.bloqueaPantalla();
						                 },
						                 success : function(respuesta){
						                     __features = respuesta;
						                 },
						                 complete : function(){
						                	 commonFunctionsJS.desbloqueaPantalla();
						                 }
						             };
						             commonFunctionsJS.ajaxSubmit(opcionesAjax);
							};
							

	/**
	 * Funcion que verifica si un arreglo de permisos tiene un permiso especifico.
	 * 
	 * @param feature Cadena que identifica el permiso
	 * @returns true, si tiene tiene el permiso asignado
	 */
	var __containFeature = function(feature){
								var hasFeature = false;
								hasFeature = (__features.indexOf(feature) != -1)? true : false;
								return hasFeature;
							};
	
	
	/**
	 * Funcion que obtiene el token generado por los recursos REST a el usuario
	 * 
	 * @returns Header de acceso a los recursos REST
	 */
	var getTokenService = function(){
								var token = $("meta[name='_token']").attr("content");
								var headerToken = "Authorization";	    					
								var headers = {};
								
								headers[headerToken] = 'Bearer '+token;
								return headers;
							};

							
	/**
	 * Funciones publicas.
	 */
	return {
		validateSesion		: validateSesion,	
		hasAnyAuthority 	: hasAnyAuthority,
		loadFeatures		: loadFeatures,
		getTokenService		: getTokenService 
	}
})();