var dataCategoria;

$(document).ready(function(){
	dataCategoria = $("#dataCategoria").attr("data-categoria")
	carregarCategoria();
	carregarCategoriaPalavrasAleatorias();
});


function carregarCategoria(){
	$.ajax({
		url : dataCategoria,
		type : 'GET',
		data : {
			idioma : $("#idiomaC").val()
		},
		statusCode : {
			200 : function(xhr) {
				$("#categoria").empty();
				var x = document.getElementById("categoria");
				$(xhr).each(function(e){
					var option = document.createElement("option");
					option.value = xhr[e].id;
					option.text = xhr[e].categoria;
					x.add(option);
				})
				
				
			}
		}
	});
}

function carregarCategoriaPalavrasAleatorias(){
	$.ajax({
		url : dataCategoria,
		type : 'GET',
		data : {
			idioma : $("#idiomaAlea").val()
		},
		statusCode : {
			200 : function(xhr) {
				$("#categoriaAle").empty();
				var x = document.getElementById("categoriaAle");
				$(xhr).each(function(e){
					var option = document.createElement("option");
					option.value = xhr[e].id;
					option.text = xhr[e].categoria;
					x.add(option);
				})
				
				
			}
		}
	});
}