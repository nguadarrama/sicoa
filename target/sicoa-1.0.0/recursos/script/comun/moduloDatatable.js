/*
* ****************************************************
* * Aplicaci&oacute;n Base
* * Versi&oacute;n 1.0.0
* * Secretar&iacute;a de Gobernaci&oacute;n - DGTIC
* ****************************************************
* 
*/

/**
* Modulo que personaliza el manejo de un componente Datatable.
* 
* Para hacer uso de este modulo se requiere se haya importado la libreria assets/datatables/* 
*/
var moduloDatatableJS = (function(selector) {
	
	/**Variable que contiene las etiquetas de componente en español*/
	var __DATATABLE_ETIQUETAS = {
				    	"sProcessing":     "<div class='messageProcessing'><i class='fa fa-spinner fa-spin'></i> Cargando......</div>",
				    	"sLengthMenu":     "Mostrar _MENU_ registros",
				    	"sZeroRecords":    "No se encontraron registros",
				    	"sEmptyTable":     "No se encontraron registros",
				    	"sInfo":           "Resultado _START_ - _END_ de _TOTAL_ registros",
				    	"sInfoEmpty":      "Resultado 0 - 0 de 0 registros",
				    	"sInfoFiltered":   "(filtrado de un total de _MAX_ registros)",
				    	"sInfoPostFix":    "",
				    	"sSearch":         "Buscar:",
				    	"sUrl":            "",
				    	"sInfoThousands":  ",",
				    	"sLoadingRecords": "Cargando...",
				    	"oPaginate": {
				    		"sFirst":    "<<",
				    		"sLast":     ">>",
				    		"sNext":     ">",
				    		"sPrevious": "<"
				    	},
				    	"oAria": {
				    		"sSortAscending":  ": Activar para ordenar la columna de manera ascendente",
				    		"sSortDescending": ": Activar para ordenar la columna de manera descendente"
				    	}
				    };
	
	/**Mensaje de error generico*/
	var __DATATABLE_ETIQUETAS_ERROR = 'A ocurrido un error al recuperar la informaci\u00f3n comuniquese con su administrador';
	
	/**selector de la tabla*/
	var selectorDatatable = selector;

	
	
		
	/**
	*	Funcion que obtiene la instancia del componente datatable.
	*
	* @return intancia del datatable
	*/
	var _getInstance = function(){
							var instance = null;
							var datatable = $(selector).dataTable();
							if(datatable != null){
								instance = datatable.api();
							}
							return instance;
						};
	
	/**
	*  Funcion que genera mensaje de error ante alguna inconsistencia
	*  Ejemplo de respuesta 
	*  
	*  <pre class="code">
	 *   {  "metadata":{
	 *   		"errors":[ "El token no es valido" ],
	 *   		"response": "ERROR"
	 *   	}
	 *   }
	 * </pre> 
	*  
	*
	*  @return json de respuesta del servicio rest
	*/
	var _muestraError = function(json){
						if(json == null || json.metadata == null){
							commonFunctionsJS.alert(__DATATABLE_ETIQUETAS_ERROR)	
						} else {
							var error = __DATATABLE_ETIQUETAS_ERROR+"<ul>";
							if(json.metadata.errors){
								$.each(json.metadata.errors, function(){
									error += "<li>"+this+"</li>"
								});	
							}							
							error += "</ul>"
							commonFunctionsJS.alert(error);							
						}						
					};
				
					

	/**
	 *   Funcion que genera un objeto javascrip para uso de "convertirFormularioToObject"
	 *   
	 *   @param objetoJS el objeto al que se integrara el nombre
	 *   @param nombre el nombre con el que sera identificado el valor dentro del objeto
	 *   @param valor el valor a almacenar en el JSON
	 *   
	 *   @returns el objeto JSON
	 **/
	var __agregarValor = function(objetoJS, nombre, valor) {
             if (objetoJS[nombre] !== undefined) {
                 if(typeof valor == 'object') {
                     objetoJS[nombre] = valor;
                 } else {
                     if (!objetoJS[nombre].push) {
                         objetoJS[nombre] = [objetoJS[nombre]];
                     }
                     objetoJS[nombre].push(valor);
                 }
             } else {
                 objetoJS[nombre] = valor;
             }
             return objetoJS;
         }
					
	/**
     * Funcion que convierte un formulario en un objeto JSON
     * 
     * @param selector selector con que identifica el formulario
     * @param incluirVacios bandera que indica si se debera incluir en el objeto json atributos vacios o blancos
     * 
     * @returns el objeto JSON
     */
	   var __convertirFormularioToObject = function(selector, incluirVacios)
				                       {
				                            var incluirValoresVacios = (incluirVacios == null)?false:incluirVacios;
				                            
				                            var objecto = {};
				                            var arrayFormulario = $(selector).serializeArray();
				                            $.each(arrayFormulario, function() {
				                                var campoFormulario = this;
				                                var isValorVacio = campoFormulario.value == null || campoFormulario.value == "";
				                                var incluirCampoEnObjecto = (!isValorVacio)? true : ((incluirValoresVacios)? true : false);

				                                if(incluirCampoEnObjecto) {
				                                    var nombre = campoFormulario.name;
				                                    var propiedadSimple = ( nombre.indexOf(".") == -1 );

				                                    if(propiedadSimple){
				                                       objecto = __agregarValor(objecto, nombre,campoFormulario.value);
				                                    } else {
				                                        var nombres = nombre.split(".");
				                                        var nombreobjeto = nombres[0];
				                                        var nombreatributoobjecto = nombres[1];
				                                        var objetoSub = objecto[nombreobjeto] == null ? {} : objecto[nombreobjeto];

				                                        objetoSub = __agregarValor(objetoSub, nombreatributoobjecto, campoFormulario.value);
				                                        objecto = __agregarValor(objecto, nombreobjeto, objetoSub);
				                                    }
				                                }
				                            });
				                            return objecto;
				                        };
				                        
				                        
    /**
     * Funcion que convierte un formulario en una cadena serializada.
     * 
     * @param selector selector con que identifica el formulario
     * @param incluirVacios bandera que indica si se debera incluir en el objeto json atributos vacios o blancos
     * 
     * @returns el formulario en una cadena serializada
     */
      var __convertirFormularioToSerialized = function(selector, incluirVacios)
				                          {
				                               var incluirValoresVacios = (incluirVacios == null)?false:incluirVacios;
				                               var formSerialize = "";
				                               if(incluirValoresVacios){
				                            	   formSerialize = $(selector).serialize();
				                               } else {
				                                   formSerialize = $(selector)
				                                                   .find(":input")
				                                                   .filter(function(index, element) {
				                                                       return $(element).val() != "";
				                                                   }).serialize();
				                               }

				                               return formSerialize;
				                           };
	
   /**
    * Funcion que almacena los datos del formulario en una cache para prevenir el cambio de parametros entre paginas
    * 
    * @param selectorForm selector del formulario
    * 
    */
	var _generaFormDataCache = function(selectorForm){
					
					var filtros = {};
					var filtrosSerialized = "";
					if( selectorForm != null){
						filtros = __convertirFormularioToObject(selectorForm);
						filtrosSerialized = __convertirFormularioToSerialized(selectorForm);
					}
					$(selectorDatatable).data("datosBusqueda", filtros);
					$(selectorDatatable).data("datosBusquedaSerialized", filtrosSerialized);
	};


	
    /**
     * Funcion que agrega la informacion de un formulario al cache de la tabla
     * 
     * @param selectorForm selector del formulario a almacenar en el cache
     */
	var setFormCacheBusquedaClient = function(selectorForm) {
		_generaFormDataCache(selectorForm);
	}
	
	
	/**
	 * Funcion que obtiene los datos del formulario con que se realizo la busqueda de su cache
	 * 
	 * @return los datos del formulario almacenados en el cache en un objeto json
	 */
	var __obtenFormDataCache = function(){
								var datosBusquedaForm = $(selectorDatatable).data("datosBusqueda");
								return datosBusquedaForm;
							};
	
							
	/**
	 * Funcion que obtiene los datos del formulario serializados 
	 * 
	 * @return los datos del formulario almacenados en una cadena serialize
	 */
    var __obtenFormSerializedDataCache = function(){
                                var datosBusquedaForm = $(selectorDatatable).data("datosBusquedaSerialized");
                                return datosBusquedaForm;
                            };

	/** 
	 * Funcion que inicializa un componente datatable con un arreglo de json generado directamente en el cliente, se complementa con la funcion 
	 * moduloDatatableJS.updateDataJsonClient.
	 * 
	 * Ejemplo 
	 * 
	 * var columnas=[ {    "data":"usuario",
	 *				            "sDefaultContent":"",
	 *				            "class":""
	 *				       },{  "data":"departamento",
	 *				            "sDefaultContent":"",
	 *				            "class":""
	 *				       },{
	 *				    	   "render": function ( data, type, full, meta ) {
	 *				                var btnVerDetalle = '<button class="ver btn btn-warning btn-xs ctooltip" title="Ver"><span class="fa fa-eye"></span></button>';
	 *				                var label = "<div class=\"accion\">"+btnVerDetalle+"</div>";
	 * 				                return label;
	 *				            },
	 *				            "class":"text-center"
	 *				       }];
	 *	
	 *	 moduloDatatableJS("#datatable_democliente").inicializaDataTableClienteJson(columnas);
	 *
	 *   var jsonList = [];
	 *	 var usuario = null;
	 *	 usuario = {"usuario":"cliente 1", "departamento":"depto 1"};
	 *	 jsonList.push(usuario);
	 *	 ...
	 *	 usuario = {"usuario":"cliente 6", "departamento":"depto 1"};
	 *	 jsonList.push(usuario);
	 *		
	 *	 moduloDatatableJS("#datatable_democliente").updateDataJsonClient(jsonList,
     *                function(){
     *                    commonFunctionsJS.inicializatooltip(".ctooltip");
     *       });
	 * 
	 * 
	 * @param columnas			La configuracion de las columnas a considerar
	 * @param opcionesDataTable opciones adicionales de configuracion de la tabla 
	 * 			opcionesDataTable  = {
										iDisplayLength : 10 		//registros por pagina de datos a mostrar (10 default)										
									};
	 */	
	var inicializaDataTableClienteJson = function(columnas,  opcionesDataTable ) {
									var opcionesCliente = {
											"pagingType"	: "full_numbers",
											"responsive": true,
											"ordering"		: false,		//Oculta las opciones de ordenar
											"lengthChange" 	: false,		//Oculta la posibilidad de cambiar el numero de resultados
											"filter"		: false,       	//Oculta el filtro general
											"serverSide"	: false,		//Habilita el paginado por medio del servidor de aplicaciones
											"iDisplayLength": ( opcionesDataTable == null || opcionesDataTable.iDisplayLength == null ) ? 10 : opcionesDataTable.iDisplayLength,
											"columns" :	columnas,
											"language" : __DATATABLE_ETIQUETAS,
											"data" : [],
									};
									$(selectorDatatable).DataTable(opcionesCliente);							
							};
							
		/** 
		 * Funcion que actualiza la fuente de datos del JSON. utilizado para el inicializaDataTableClienteJson. 
		 * 
		 * @param jsonDataArray fuente de informacion datos de array.
		 * @param callback Funcion a ejecutar posterior a la actualizacion de resultados 
		 * @param footerCallBack Funcion a ejecutar posterior a la actualizacion de resultados para personalizacion del pie de tabla
		 */
       var updateDataJsonClient = function(jsonDataArray, callback, footerCallBack) {

                                    var arrayData = (jsonDataArray == null)?[] : jsonDataArray;
                                    limpiarResultados();
                                   
                                    var instanceDatatable = _getInstance();
                                    instanceDatatable.rows.add(arrayData);
                                    instanceDatatable.draw();
                                    if(callback!=null){
                                        callback();
                                    }
                                    if(footerCallBack != null){
                                    	footerCallBack(instanceDatatable);
                                    }
                                };


	
		/**
		 * Funcion que inicializa un componente datatable con un servicio REST como fuente de informacion (datatable remoto), para un paginado de lado servidor		 * 
		 * Nota : Las peticiones que realiza ya integran el token de seguridad para consumo de recursos REST.
		 * 
		 * Ejemplo.
		 * 
		 * var columnas=[ {    "data":"usuario",
		 *		            "sDefaultContent":"",
		 *		            "class":""
		 *		       },{  "data":"departamento",
		 *		            "sDefaultContent":"",
		 *		            "class":""
		 *		       },{
		 *		    	   "render": function ( data, type, full, meta ) {
		 *		                var btnVerDetalle = '<button class="ver btn btn-warning btn-xs ctooltip" title="Ver"><span class="fa fa-eye"></span></button>';
		 *		                var label = "<div class=\"accion\">"+btnVerDetalle+"</div>";
		 *		                return label;
		 * 	                },
		 *	                "class":"text-center"
		 *		       }];
		 *
		 * moduloDatatableJS("#datatable_demo").inicializaDataTableServer(
		 *		columnas,
		 *		commonFunctionsJS.obtenerUrlServicios("/demo/datatable/restringido"),
		 *		{
		 *			callback : function(){
		 *				commonFunctionsJS.inicializatooltip(".ctooltip");						
		 *			}
		 *		}
		 *	);
		 *
		 *  moduloDatatableJS("#datatable_demo").buscar();
		 * 
		 * 
		 * @param columnas				La configuracion de las columnas
		 * @param url				 	Endpoint del servicio REST que genera la fuente de informacion. 
		 * @param opcionesDataTable   	opciones adicionales de configuracion de la tabla  opcionales
		 * 			opcionesDataTable  = {
											iDisplayLength : 10, 		//registros por pagina de datos a mostrar (10 default)
											callback : null ,			//Funcion a ejecutar una vez concluido la busqueda ( ejemplo : inicializar componentes )
											footerCallBack : null 		//Funcion a ejecutar posterior a la actualizacion de resultados para personalizacion del pie de tabla											
										};
		 */
		var inicializaDataTableServer = function(columnas, url,  opcionesUsuarioDataTable ) {
												
									var opcionesServidor = {
														"pagingType"	: "full_numbers",
														"bSort"			: false,
														"ordering"		: false,		//Oculta las opciones de ordenar
														"lengthChange" 	: false,		//Oculta la posibilidad de cambiar el numero de resultados
														"filter"		: false,       	//Oculta el filtro general
														"serverSide"	: true,		//Habilita el paginado por medio del servidor de aplicaciones
														"iDisplayLength": opcionesUsuarioDataTable.iDisplayLength == null ? 10 : opcionesUsuarioDataTable.iDisplayLength,
														"columns": columnas,
														"fnDrawCallback" : function (oSettings) {	//Metodo que se ejecuta al finalizar la peticion
																if(opcionesUsuarioDataTable.callback!=null){
																	opcionesUsuarioDataTable.callback();
																}
														},
														"footerCallBack": function() {
															if(opcionesUsuarioDataTable.footerCallBack != null){
																opcionesUsuarioDataTable.footerCallBack(_getInstance());
															}
														}
												};
														
										 $(selectorDatatable).DataTable({
															"pagingType"	: "full_numbers",
															"bSort"			: opcionesServidor.bSort,	
															"responsive": true,
															"ordering"		: false,			//Oculta las opciones de ordenar
															"lengthChange" 	: false,		//Oculta la posibilidad de cambiar el numero de resultados
															"filter"		: false,        	//Oculta el filtro general
															"serverSide"	: true,			//Habilita el paginado por medio del servidor de aplicaciones
															"iDisplayLength": opcionesServidor.iDisplayLength,		//Define el numero maximo por tabla					                   
															"fnDrawCallback": opcionesServidor.fnDrawCallback,
															"footerCallback": opcionesServidor.footerCallBack,
															"deferLoading":0,
															"columns":opcionesServidor.columns,
															"language" : __DATATABLE_ETIQUETAS,
															"processing" : true,
															"fnServerData": function ( sSource, aoData, fnCallback, oSettings ) {
											                    	var datosBusquedaForm = __obtenFormDataCache();
																	
																	var dataDataTable = {};
											                    	$.each(aoData, function() {
											     			            if (dataDataTable[this.name] !== undefined) {
											     			                if (!dataDataTable[this.name].push) {
											     			                	dataDataTable[this.name] = [dataDataTable[this.name]];
											     			                }
											     			                dataDataTable[this.name].push(this.value == null ? "" : this.value);
											     			            } else {
											     			            	dataDataTable[this.name] = this.value == null ? "" : this.value;
											     			            }
											     			        });
											                    	$.extend(dataDataTable,datosBusquedaForm);					                    	

                                                                  var promisePing = securityJS.validateSesion();
                                                                  $.when(promisePing).done(function(){
                                                                        oSettings.jqXHR = $.ajax( {
                                                                                "contentType" : 'application/x-www-form-urlencoded',
                                                                                "dataType" : 'json',
                                                                                "type": "GET",
                                                                                "url": url,
                                                                                "headers" : securityJS.getTokenService(),
                                                                                "data": dataDataTable,                                                                                
                                                                                "success": function(respuesta){
                                                                                	var resultado = {"data":[], "draw":dataDataTable.draw, "recordsTotal":0, "recordsFiltered":"0"};
                                                                                	if(respuesta.data){
                                                                                		resultado.data = (respuesta.data)?respuesta.data : [];
                                                                                	}
                                                                                	if(respuesta.pagination){
                                                                                		resultado.recordsTotal = (respuesta.pagination.total)?respuesta.pagination.total:0;
                                                                                		resultado.recordsFiltered = (respuesta.pagination.total)?respuesta.pagination.total:0;
                                                                                	}
                                                                                	
                                                                                	fnCallback(resultado);
                                                                                },
                                                                                error: function (jqXHR, textStatus, error){                                                                                	
                                                                                    _muestraError(jqXHR.responseJSON);
                                                                                }
                                                                            });
											                      });
											                },
												    });
							};
							
	/**
	 * Funcion que realiza una busqueda de informacion para un datatable remoto 
	 * 
	*  @param selectorFormFiltros (opcional : null default) Id del selector del formulario con los filtros que delimitan la busqueda, a nivel servidor.
	*/
	var buscar	 = function(selectorFormFiltros){

				_generaFormDataCache(selectorFormFiltros);
				
				var $datatable = _getInstance();
				$datatable.draw();
	};
							
							
	/**
	 * Funcion que obtiene el dato (JSON) asociado a una fila del datatable.
	 * 
	 * @return el dato de JSON
	 */						
	var obtenerDatosRowSeleccionado = function(tr) {
										var row = _getInstance().row(tr); 
										return row.data();
									};

    /**
    * Funcion que obtiene los filtros de busqueda que se serializaron en el cache
    * 
    * @return el filtro de busqueda serializado
    */
    var obtenerFiltrosBusquedaSerialized = function(){
                                    var formBusquedaSerialized = __obtenFormSerializedDataCache();
                                    return formBusquedaSerialized
                                };


                                
                                
    /**
	* Funcion que elimina la instancia del datatable 
	* 
	*/
    var destruirInstancia = function(){
    	var $datatable = _getInstance();
		$datatable.destroy();
    }
    
    
    /**
	* Funcion que evalua si el set de datos es mayor a 0
	*/
    var tieneResultados = function(){    
    	var $datatable = _getInstance();    	
    	var tieneDatos = $datatable.data().any();
		return tieneDatos;
    }
    
    
    /**
	* Funcion limpia la tabla de resultados. Para un datable cliente
	*/
    var limpiarResultados = function(){
    	var instanceDatatable = _getInstance();
    	instanceDatatable.clear();
    	instanceDatatable.draw();
    }
    
    /**
	* Funcion devuelve la instancia de datatable del objeto seleccionado
	*/
    var obtenerInstancia = function() {
    	return _getInstance();
    };
    
    
	/**
	 * Funciones publicas.
	 */
	return {
		inicializaDataTableServer : inicializaDataTableServer,	
		inicializaDataTableClienteJson : inicializaDataTableClienteJson,
		updateDataJsonClient : updateDataJsonClient,
		setFormCacheBusquedaClient : setFormCacheBusquedaClient,		
		buscar : buscar,
		obtenerDatosRowSeleccionado: obtenerDatosRowSeleccionado,
		obtenerFiltrosBusquedaSerialized : obtenerFiltrosBusquedaSerialized,
		destruirInstancia: destruirInstancia,
		tieneResultados : tieneResultados,
		limpiarResultados:limpiarResultados,
		obtenerInstancia:obtenerInstancia
	};
	
});