
$(document).ready(function() {
	
		  $('#tableUsuarios').DataTable({
		    "scrollY": "500px",
		    "scrollCollapse": true
		  });
		  $("#clavePerfil").multiSelect();
		 $('#clavePerfil').on('change',function() {
			  //alert($(this).val());
			});
		 
	$("#administrador").change(function() {
		  alert($("#administrador").val());
	});		 
	$('#tableUsuarios').on('click','.eBtn' , function(event) { 					//botón edita
		
		 event.preventDefault();
		var href = $(this).attr('href');
		var text = $(this).text();
			$.get(href, function(hmap, status) {
				$('.usuarioForm #claveUsuario').val(hmap.usuario.claveUsuario);
				$('.usuarioForm #nombre').val(hmap.usuario.nombre);
				$('.usuarioForm #apellidoPaterno').val(hmap.usuario.apellidoPaterno);
				$('.usuarioForm #apellidoMaterno').val(hmap.usuario.apellidoMaterno);
				$('.usuarioForm #activo').val(hmap.usuario.activo);
				$('.usuarioForm #bloqueado').val(hmap.usuario.bloqueado);
				if(hmap.usuario.bloqueado=='N'){
					$('.usuarioForm #bloqueado').attr('checked',true);
				}
				$('.usuarioForm #idHorario').val(hmap.usuario.idHorario);
				$('.usuarioForm #clavePerfil').val(hmap.usuario.clavePerfil);
				$('.usuarioForm #idHorario').val(hmap.usuario.idHorario.idHorario);
				$('.usuarioForm #rfc').val(hmap.usuario.rfc);
				$('.usuarioForm #nivel').val(hmap.usuario.nivel);
				$('.usuarioForm #puesto').val(hmap.usuario.idPuesto);
				$('.usuarioForm #fechaIngreso').val(hmap.usuario.fechaIngreso);
				$('.usuarioForm #unidadAdministrativa').val(hmap.usuario.idUnidad).delay(3000);
				var i=0;
				//alert("datos "+hmap.listaUsuarioPerfiles.length);
				for(i=0;hmap.listaUsuarioPerfiles.length>i;i++){
					alert(hmap.listaUsuarioPerfiles[i].clavePerfil.clavePerfil);
					if(hmap.listaUsuarioPerfiles[i].clavePerfil.clavePerfil=='0'){
						$('#administrador').prop('checked', true);
					}
					if(hmap.listaUsuarioPerfiles[i].clavePerfil.clavePerfil=='1'){
						$('#director').attr('checked', true);					
					}
					if(hmap.listaUsuarioPerfiles[i].clavePerfil.clavePerfil=='2'){
						$('#coordinador').prop('checked', true);
					}
					if(hmap.listaUsuarioPerfiles[i].clavePerfil.clavePerfil=='3'){
						
						$('#empleado').prop('checked', true);
					}
				}
				//alert("perfiles "+hmap.listaUsuarioPerfiles[0]);
				//alert(hmap.listaUsuarioPerfiles.length);
				var a=0;
				var selectPerfil = document.getElementById("perfiles");
				var selectPerfiles = document.getElementsByName("clavePerfil")[0];

				 /*for (value in array) {
				  var option = document.createElement("option");
				  option.text = array[value];
				  select.add(option);
				 }*/
//				var lengthPerfil = selectPerfil.options.length;
//				for(e=lengthPerfil;e>0;e--){
//					//alert($("#clavePerfil option[value='" + e + "']").val());
//					selectPerfil.remove(e-1);
////					alert(selectPerfil.value(e-1));
////					for(e=lengthPerfil;e>0;e--){
////						for(a=0;a<hmap.listaUsuarioPerfiles.length;a++){
////							alert()
//											
//					
//				}
				
//				for(a=0;a<hmap.listaUsuarioPerfiles.length;a++){
//					//alert(hmap.listaUsuarioPerfiles[a].clavePerfil.descripcion);
//					var option = document.createElement("option");
//					  option.text = hmap.listaUsuarioPerfiles[a].clavePerfil.descripcion;
//					  selectPerfil.add(option);
//					  //alert($("#perfiles option[value='" +a+ "']").val());
//				}
				
//				var opciones="<select  class='form-control' required='required' th:value='clavePerfil' name='clavePerfil' id='clavePerfil' multiple='multiple'> ";
//				for(a=0;a<hmap.listaUsuarioPerfiles.length;a++){
//					var bandera=false;
//					var seleccion;
//					for(u=0;u<hmap.listaPerfiles.length;u++){
//						alert(hmap.listaPerfiles[u].clavePerfil+" == "+hmap.listaUsuarioPerfiles[a].clavePerfil.clavePerfil);
//						if(hmap.listaPerfiles[u].clavePerfil==hmap.listaUsuarioPerfiles[a].clavePerfil.clavePerfil){
//							//alert("iguales");
//							seleccion=hmap.listaUsuarioPerfiles[a].clavePerfil.clavePerfil;
//							alert("iguales "+seleccion);
//						}
//					}
//					//var option = document.createElement("option");
//					 
//					       
//					opciones+="<option value='"+hmap.listaUsuarioPerfiles[a].clavePerfil.clavePerfil+"' text='"+hmap.listaUsuarioPerfiles[a].clavePerfil.descripcion+"' "; 
//					       
//				      
////					option.text = hmap.listaUsuarioPerfiles[a].clavePerfil.clavePerfil;
////					option.value=hmap.listaUsuarioPerfiles[a].clavePerfil.descripcion;
//					if(bandera==true){
//						opciones+="selected = 'selected' ";
//						alert("iguales");
//					}
//					opciones+="></option> ";
//					
//					alert("una vez");
//					var prueba="<a href='#'>valor</a>";
//					
//					//selectPerfiles.add(option);
//				}
//				opciones+="</select>";
//				alert(opciones);
//				$("#prueba").html(opciones);
//				$("#clavePerfil").multiSelect();
//				var length = selectPerfiles.options.length;
//				for (i = 0; i<length; i++) {
//					document.getElementById("clavePerfil").checked=true;
//					for(a=0;a<hmap.listaUsuarioPerfiles.length;a++){
//						//alert(hmap.listaUsuarioPerfiles[a].clavePerfil.descripcion);
//						//alert(hmap.listaUsuarioPerfiles[a].clavePerfil.descripcion+" "+hmap.listaUsuarioPerfiles[a].clavePerfil.clavePerfil+" "+$("#perfiles option[value='" +i+ "']").val());
//						if(hmap.listaUsuarioPerfiles[a].clavePerfil.clavePerfil==$("#perfiles option[value='" + i + "']").val()){
//							$("#clavePerfil option[value='" + i + "']").prop('selected','selected');
//							//alert(hmap.listaUsuarioPerfiles[a].clavePerfil.descripcion+" "+hmap.listaUsuarioPerfiles[a].clavePerfil.clavePerfil+" "+$("#perfiles option[value='" + i + "']").val());
//						}
//					
//					 
//					} 
//					
//				}
				
				
//				
				
			});
			
			$('.usuarioForm #usuarioModal').modal().slideUp(300).delay(400).fadeIn(400);
	});
	
	
	$('.eliminaBtn').on('click', function(event) {			    //botón elimina
		event.preventDefault();
		var href = $(this).attr('href');
		
		$('#eliminarModal #delRef').attr('href', href);
		$('#eliminarModal').modal();
	});
	
	$("#buscarText").on("keyup", function() {
	    var value = $(this).val().toLowerCase();
	    $("#tableHorarios tr").filter(function() {
	      $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
	    });
	  });
	
	$('.filterable .btn-filter').click(function(){
        var $panel = $(this).parents('.filterable'),
        $filters = $panel.find('.filters input'),
        $tbody = $panel.find('.table tbody');
        if ($filters.prop('disabled') == true) {
            $filters.prop('disabled', false);
            $filters.first().focus();
        } else {
            $filters.val('').prop('disabled', true);
            $tbody.find('.no-result').remove();
            $tbody.find('tr').show();
        }
    });

    $('.filterable .filters input').keyup(function(e){
        /* Ignore tab key */
        var code = e.keyCode || e.which;
        if (code == '9') return;
        /* Useful DOM data and selectors */
        var $input = $(this),
        inputContent = $input.val().toLowerCase(),
        $panel = $input.parents('.filterable'),
        column = $panel.find('.filters th').index($input.parents('th')),
        $table = $panel.find('.table'),
        $rows = $table.find('tbody tr');
        /* Dirtiest filter function ever ;) */
        var $filteredRows = $rows.filter(function(){
            var value = $(this).find('td').eq(column).text().toLowerCase();
            return value.indexOf(inputContent) === -1;
        });
        /* Clean previous no-result if exist */
        $table.find('tbody .no-result').remove();
        /* Show all rows, hide filtered ones (never do that outside of a demo ! xD) */
        $rows.show();
        $filteredRows.hide();
        /* Prepend no-result row if all rows are filtered */
        if ($filteredRows.length === $rows.length) {
            $table.find('tbody').prepend($('<tr class="no-result text-center"><td colspan="'+ $table.find('.filters th').length +'">No encontrado</td></tr>'));
        }
    });
	
}); 