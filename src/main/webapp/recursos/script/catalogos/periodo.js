$(document).ready(function() {
	
	$('#tablePeriodos').DataTable({
	    "scrollY": "500px",
	    "scrollCollapse": true
	  });

	$("#fechaInicio").datepicker({
	format: 'dd-MM-yyyy',
	beforeShowDay: $.datepicker.noWeekends,
//	minDate: '-0D',
	maxDate: '12M',
	zIndexOffset: 100000,
	beforeShow: function () {
        var $datePicker = $("#fechaInicio");
        var zIndexModal = $datePicker.closest(".modal").css("z-index");
        $datePicker.css("z-index", zIndexModal + 1);
     },
     onSelect: function() { 
    	 var minDate = $('#fechaInicio').datepicker('getDate');
	     $("#fechaFin").datepicker("change", { minDate: minDate });
	 }   
    }).attr('required', 'required').
        keypress(function (event) {
            if (event.keyCode == 8) {
                event.preventDefault();
            }
        });

	$("#fechaFin").datepicker({
		format: 'dd-MM-yyyy',
		beforeShowDay: $.datepicker.noWeekends,
		minDate: '-0D',
//		maxDate: '12M',
		zIndexOffset: 100000,
		beforeShow: function () {
	        var $datePicker = $("#fechaFin");
	        var zIndexModal = $datePicker.closest(".modal").css("z-index");
	        $datePicker.css("z-index", zIndexModal + 1);
	     },
	     onSelect: function() { 
	    	 var maxDate = $('#fechaFin').datepicker('getDate');
		     $("#fechaInicio").datepicker("change", { maxDate: maxDate });
		 }   
	    }).attr('required', 'required').
	        keypress(function (event) {
	            if (event.keyCode == 8) {
	                event.preventDefault();
	            }
	        });

	
	
	$('#tablePeriodos').on('click','.eBtn', function(event) { 					//botón edita
		event.preventDefault();
		var href = $(this).attr('href');
		
			$.get(href, function(periodo, status) {
				$('.periodoForm #idPeriodo').val(periodo.idPeriodo);
				$('.periodoForm #fechaInicio').val(periodo.fechaInicio);
				$('.periodoForm #fechaFin').val(periodo.fechaFin);
				$('.periodoForm #descripcion').val(periodo.descripcion);
				if (periodo.activo) {
					$('#activado').prop("checked", true);
				} else {
					$('#desactivado').prop("checked", true);
				}
				
			});
			
			$('.periodoForm #periodoModal').modal();
	});

	
	$('.nBtn').on('click', function(event) {					//botón nuevo
		$('.nuevoPeriodoForm #nuevoPeriodoModal').modal(); 
	});
	
	$('.nBtn').on('click', function(event) {					//botón edita
		$('.periodoform #periodoModal').modal(); 
	});
	
	$('.eliminaBtn').on('click', function(event) {			    //botón elimina
		event.preventDefault();
		var href = $(this).attr('href');
		
		$('#eliminarModal #delRef').attr('href', href);
		$('#eliminarModal').modal();
	});
	
	$("#buscarText").on("keyup", function() {
	    var value = $(this).val().toLowerCase();
	    $("#tablePeriodos tr").filter(function() {
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
    
    $(document).ready(function() { 
    	 
        $('#blockButton').click(function() { 
            $('div.test').block({ message: null }); 
        }); 
 
        $('#blockButton2').click(function() { 
            $('div.test').block({ 
                message: '<h1>Procesando</h1>', 
                css: { border: '3px solid #a00' } 
            }); 
        }); 
 
        $('#unblockButton').click(function() { 
            $('div.test').unblock(); 
        }); 
 
        $('a.test').click(function() { 
           
            return false; 
        }); 
    }); 
    
    $(document).ready(function() { 
        $('#blockEdita').click(function() { 
            $.blockUI();
            $('#modalEdicion').submit();
            setTimeout(function() { 
                $.unblockUI({ 
                    onUnblock: function(){ /**alert('onUnblock');**/ } 
                }); 
            }, 8000); 
        }); 
    });
    
    $(document).ready(function() { 
        $('#blockAlta').click(function() { 
            $.blockUI();
            $('#modalAlta').submit();
            setTimeout(function() { 
                $.unblockUI({ 
                    onUnblock: function(){ /**alert('onUnblock');**/ } 
                }); 
            }, 60000); 
        }); 
    });
    
    $(document).ajaxStart($.blockUI).ajaxStop($.unblockUI);
    
}); 