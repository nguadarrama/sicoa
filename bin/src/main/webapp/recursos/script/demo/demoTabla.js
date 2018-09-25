var demoTablaJS  = (function() {
	
	var init = function(){
		inicializaDatatable();
		inicializaBotonBuscar();
		
		inicializaDatatableCliente();
		inicializaBotonBuscarCliente();
		inicializa();
	};
	
	
	var inicializaBotonBuscar= function(){
		$("#btn-buscar").click(function(){
			moduloDatatableJS("#datatable_demo").buscar();
		});
	}
	
	var inicializaDatatable = function(){
		
		var columnas=[ {    "data":"usuario",
				            "sDefaultContent":"",
				            "class":""
				       },{  "data":"departamento",
				            "sDefaultContent":"",
				            "class":""
				       },{
				    	   "render": function ( data, type, full, meta ) {
				                var btnVerDetalles = '<button class="ver btn btn-warning btn-xs ctooltip" title="Ver"><span class="fa fa-eye"></span></button>';
				                var botones = "<div class=\"accion\">"+btnVerDetalles+"</div>";
				                return botones;
			                },
			                "class":"text-center"
				       }];
						
		
		moduloDatatableJS("#datatable_demo").inicializaDataTableServer(
				columnas,
				commonFunctionsJS.obtenerUrlServicios("/demo/datatable/restringido"),
				{
					callback : function(){
						commonFunctionsJS.inicializatooltip(".ctooltip");						
					}
				}
			);
		
		  $("#datatable_demo").on('click','td .ver',function(event){
			  	var  renglon =  $(this).closest("tr");
		   		var dato = moduloDatatableJS("#datatable_demo").obtenerDatosRowSeleccionado(renglon);
		   		commonFunctionsJS.alert(JSON.stringify(dato));
	        });
	};
	
	
	var inicializaDatatableCliente = function(){
		var columnas=[ {    "data":"usuario",
					            "sDefaultContent":"",
					            "class":""
					       },{  "data":"departamento",
					            "sDefaultContent":"",
					            "class":""
					       },{
					    	   "render": function ( data, type, full, meta ) {
					                var btnVerDetalle = '<button class="ver btn btn-warning btn-xs ctooltip" title="Ver"><span class="fa fa-eye"></span></button>';
					                var label = "<div class=\"accion\">"+btnVerDetalle+"</div>";
					                return label;
					            },
					            "class":"text-center"
					       }];
		
		 moduloDatatableJS("#datatable_democliente").inicializaDataTableClienteJson(columnas);
		 
		 $("#datatable_democliente").on('click','td .ver',function(event){
			  	var  renglon =  $(this).closest("tr");
		   		var dato = moduloDatatableJS("#datatable_democliente").obtenerDatosRowSeleccionado(renglon);
		   		commonFunctionsJS.alert(JSON.stringify(dato));
	        });
	}
	
	var inicializaBotonBuscarCliente= function(){
		$("#btn-buscar-cliente").click(function(){
			var jsonList = [];
			var usuario = null;
			usuario = {"usuario":"cliente 1", "departamento":"depto 1"};
			jsonList.push(usuario);
			usuario = {"usuario":"cliente 2", "departamento":"depto 1"};
			jsonList.push(usuario);
			usuario = {"usuario":"cliente 3", "departamento":"depto 2"};
			jsonList.push(usuario);
			usuario = {"usuario":"cliente 4", "departamento":"depto 4"};
			jsonList.push(usuario);
			usuario = {"usuario":"cliente 5", "departamento":"depto 3"};
			jsonList.push(usuario);
			usuario = {"usuario":"cliente 6", "departamento":"depto 1"};
			jsonList.push(usuario);
			
			
			moduloDatatableJS("#datatable_democliente").updateDataJsonClient(jsonList,
                     function(){
                         commonFunctionsJS.inicializatooltip(".ctooltip");
            });
		});
	}
	

	
	var inicializa= function(){
				var columnas=[ {    "data":"usuario",
		            "sDefaultContent":"",
		            "class":""
		       },{  "data":"departamento",
		            "sDefaultContent":"",
		            "class":""
		       },{
		    	   "render": function ( data, type, full, meta ) {
		                var label = "parametro 1 : "+ ( (full.parametroUno == null) ? "" : full.parametroUno );
		                label += "<br/>";
		                label += "parametro 2 : "+ ( (full.parametroDos == null) ? "" : full.parametroDos );
		                return label;
		            },
		            "class":"text-center"
		       },{
		    	   "render": function ( data, type, full, meta ) {
		                var btnVerDetalle = '<button class="ver btn btn-warning btn-xs ctooltip" title="Ver"><span class="fa fa-eye"></span></button>';
		                var label = "<div class='accion'>"+btnVerDetalle+"</div>";
		                return label;
		            },
		            "class":"text-center"
		       }];
		
				moduloDatatableJS("#datatable_democliente_form").inicializaDataTableServer(
						columnas,
						commonFunctionsJS.obtenerUrlServicios("/demo/datatable/publico/form"),
						{
							callback : function(){
								commonFunctionsJS.inicializatooltip(".ctooltip");						
							}
						}
					);
			
			$("#datatable_democliente_form").on('click','td .ver',function(event){
			  	var  renglon =  $(this).closest("tr");
					var dato = moduloDatatableJS("#datatable_democliente_form").obtenerDatosRowSeleccionado(renglon);
					commonFunctionsJS.alert(JSON.stringify(dato));
			});
			
			$("#btn-buscar-cliente_form").click(function(){
				moduloDatatableJS("#datatable_democliente_form").buscar("#formBusqueda");				
			});
	}

	return {
		init : init		
    };	
})();