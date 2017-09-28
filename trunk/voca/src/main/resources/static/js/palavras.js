var datatable;
var dataList; 
var dataRemove;
var dataSalvar;
$(document).ready(function(){
	dataList = $("#data-list").attr("data-list");
	dataRemove = $("#data-remove").attr("data-remove");
	dataSalvar = $("#data-salvar").attr("data-salvar");
	initDataTable();
	
	$('#minhasPalavras tbody').on( 'click', 'button.btn-danger', function () {
        var data = datatable.row( $(this).parents('tr') ).data();
        removerPalavra(data.id);
    } );

	$('#minhasPalavras tbody').on( 'click', 'button.btn-purple', function () {
		var data = datatable.row( $(this).parents('tr') ).data();
		
		$("#idioma").val("");
		$("#categoria").val("");
		
		$("#id").val(data.id);

		$("#idioma option").each(function(){
		var opti = $(this);
		if(opti.text() == data.idioma){
			$("#idioma").val(opti.val());
			return;
			}
		});
		
		$("#palavra").val(data.palavra);
		$("#traducao").val(data.traducao);

		$("#categoria option").each(function(){
			var opti = $(this);
			if(opti.text() == data.categoria){
				$("#categoria").val(opti.val());
				return;
			}
		});
	} );
	
	
	$("#btnSubmit").click(function (event) {

        //stop submit the form, we will post it manually.
        event.preventDefault();

        fire_ajax_submit();

    });
});


function fire_ajax_submit() {

    // Get form
    var form = $('#form')[0];

    var data = new FormData(form);

    $("#btnSubmit").prop("disabled", true);

    $.ajax({
        type: "POST",
        enctype: 'multipart/form-data',
        url: dataSalvar,
        data: data,
        //http://api.jquery.com/jQuery.ajax/
        //https://developer.mozilla.org/en-US/docs/Web/API/FormData/Using_FormData_Objects
        processData: false, //prevent jQuery from automatically transforming the data into a query string
        contentType: false,
        cache: false,
        timeout: 600000,
        success: function (data) {
        	if(data.length != 0){
        		$(data).each(function(e){
        			addErroMessage("", data[e]);
        		});
        	}else{
				addGrowl("Yeah","Palavra salva", "success")
        		reloadDataTable();
				$("#form")[0].reset();
				$("#id").val("");
        	}
            $("#btnSubmit").prop("disabled", false);

        },
        error: function (e) {
        	addGrowl("Ops!", e.responseText, "danger")
            $("#btnSubmit").prop("disabled", false);
        }
    });
}

function removerPalavra(id) {
	var response = $.ajax({
		url : dataRemove,
		type : 'POST',
		data : {
			"id" : id
		},
		statusCode : {
			200 : function(xhr) {
				reloadDataTable();
				addGrowl("Yeah","Palavra removida", "success")
			},
			500 : function(xhr) {
				addGrowl("Ops!", "Alguma coisa deu errado", "danger")
			}
		}
	});

}

function initDataTable(){
	datatable = $("#minhasPalavras").DataTable({
		"ajax": dataList,
		"columns": [
            { "data": "id" },
            { "data": "palavra" },
            { "data": "traducao" },
            { "data": "categoria" },
            { "data": "idioma" },
            {"render": function (data, type, JsonResultRow, meta) {
                return '<img class="img-thumbnail" src="data:image/png;base64,'+JsonResultRow.imagem+'">';
            }},
            { "data": "" },
        ],
		 "columnDefs": [
			    {
			      "data": null,
			      "defaultContent": "<button type='button' style='display: inline-grid;margin-left:14px' class='btn btn-danger'><span class='glyphicon glyphicon-trash'></span> <span>Remover</span></button> " +
			      					"<button type='button' style='display: inline-grid;' class='btn btn-purple'><span class='glyphicon glyphicon-pencil'></span> <span>Editar</span></button>",
			      "targets": -1
			    }
			  ],
		"info" : false,
		"ordering" : false,
		"pageLength" : 5,
		"searching" : true,
		"lengthChange" : false,
		"language" : {
			"sEmptyTable" : "Nenhum registro encontrado",
			"sInfo" : "Mostrando de _START_ até _END_ de _TOTAL_ registros",
			"sInfoEmpty" : "Mostrando 0 até 0 de 0 registros",
			"sInfoFiltered" : "(Filtrados de _MAX_ registros)",
			"sInfoPostFix" : "",
			"sInfoThousands" : ".",
			"sLengthMenu" : "_MENU_ resultados por página",
			"sLoadingRecords" : "Carregando...",
			"sProcessing" : "Processando...",
			"sZeroRecords" : "Nenhum registro encontrado",
			"sSearch" : "Pesquisar",
			"oPaginate" : {
				"sNext" : "Próximo",
				"sPrevious" : "Anterior",
				"sFirst" : "Primeiro",
				"sLast" : "Último"
			},
			"oAria" : {
				"sSortAscending" : ": Ordenar colunas de forma ascendente",
				"sSortDescending" : ": Ordenar colunas de forma descendente"
			}
		}
	});
}

function reloadDataTable(){
	datatable.ajax.reload( null, false );
}
