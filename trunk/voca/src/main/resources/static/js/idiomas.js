var datatable;
var dataList;
var dataRemove;
var dataSave;
var dataUpdate;
var dataValidaIdioma;
$(document).ready(
		function() {
			dataList = $("#data-list").attr("data-list");
			dataRemove = $("#data-remove").attr("data-remove");
			dataSave = $("#data-save").attr("data-save");
			dataUpdate = $("#data-update").attr("data-update");
			dataValidaIdioma = $("#data-valida-idioma").attr(
					"data-valida-idioma");
			initDataTable();
			updateIdiomasList();
			
			$('#meusIdiomas tbody').on(
					'click',
					'button',
					function() {
						var data = datatable.row($(this).parents('tr')).data();
						if (confirm("Todas as palavras do idioma "
								+ data.idioma
								+ " serão removidas. Deseja Continuar?")) {
							removerIdioma(data.id);
						}
					});



			$('#prefetch .typeahead').on("blur", function() {
				var idioma = $(this);
				if(idioma.val() != ""){
					
				var response = $.ajax({
					url : dataValidaIdioma,
					type : 'GET',
					data : {
						"idioma" : idioma.val()
					},
					statusCode : {
						200 : function(xhr) {
							var resposta = xhr.responseText;
							if (resposta != "") {
								addErroMessage("", resposta);
								idioma.val("");
							} else {
								$("#messagem").hide()
							}
						}
					}
				});
				}
			});
			
			$('#prefetch .typeahead').typeahead({
			    highlight: true,
			},
			{
			  name: 'brands',
			  source: function(query, syncResults, asyncResults) {
				  if(query.length > 2){
				    $.get(dataUpdate +'?query=' + query, function(data) {
				      asyncResults(data);
				    });
				  }
			  }
			})
			
			
		});

function removerIdioma(id) {

	var response = $.ajax({
		url : dataRemove,
		type : 'POST',
		data : {
			"id" : id
		},
		statusCode : {
			200 : function(xhr) {
				updateIdiomasList();
				reloadDataTable();
				addGrowl("Yeah","Idioma removido", "success")
			},
			500 : function(xhr) {
				addGrowl("Ops!", "Alguma coisa deu errado", "danger")
			}
		}
	});

}

function salvarIdioma() {
	var iu = new Object();
	iu.id = null;
	iu.idioma = new Object();
	iu.idioma.idioma = $("#idioma").val();
	if (iu.idioma.idioma == "") {
		addErroMessage("Campo Obrigatório!", "Selecione um idioma");
		return false;
	}
	var response = $.ajax({
		contentType : 'application/json; charset=utf-8',
		dataType : 'json',
		url : dataSave,
		type : 'POST',
		data : JSON.stringify(iu),
		statusCode : {
			200 : function(xhr) {
				updateIdiomasList();
				reloadDataTable();
				addGrowl("Yeah","Novo idioma adicionado", "success")
				$("#idioma").val("");
			},
			500 : function(xhr) {
				addGrowl("Ops!", "Alguma coisa deu errado", "danger")
				$("#idioma").val("");
			}
		}
	});

}

function updateIdiomasList() {



}

function initDataTable() {
	datatable = $("#meusIdiomas")
			.DataTable(
					{
						"ajax" : dataList,
						"columns" : [ {
							"data" : "id"
						}, {
							"data" : "idioma"
						}, {
							"data" : "palavras"
						}, {
							"data" : ""
						}, ],
						"columnDefs" : [ {
							"data" : null,
							"defaultContent" : "<button type='button' class='btn btn-danger'><span class='glyphicon glyphicon-trash'></span> <span>Remover</span></button>",
							"targets" : -1
						} ],
						"info" : false,
						"ordering" : false,
						"pageLength" : 7,
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

function reloadDataTable() {
	datatable.ajax.reload();
}

