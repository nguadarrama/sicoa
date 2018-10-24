$(document).ready(function() {
	
	$('#tablePeriodos').DataTable({
	    "scrollY": "500px",
	    "scrollCollapse": true
	  });

	

	$('#tablePeriodos').on('click','.eBtn', function(event) { 					//botón edita
		event.preventDefault();
		var href = $(this).attr('href');
		var text = $(this).text();
		
			$.get(href, function(periodo, status) {
				$('.periodoForm #idPeriodo').val(periodo.idPeriodo);
				$('.periodoForm #fechaInicio').val(periodo.fechaInicio);
				$('.periodoForm #fechaFin').val(periodo.fechaFin);
				$('.periodoForm #descripcion').val(periodo.descripcion);
				$('.periodoForm #activo').val(periodo.activo);
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
}); 