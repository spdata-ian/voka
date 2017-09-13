var dataSom;
var dataFinalizar;
var dataPraticar;
var dataOuvir;
var palavras = [];
var countPalavra = 0;
var countAcertos = 0;
var finalizou = false;
$(document).ready(function() {
	
	dataSom = $("#dataSom").attr("data-som")
	dataFinalizar = $("#dataFinalizar").attr("data-finalizar")
	dataPraticar = $("#dataPraticar").attr("data-praticar")
	dataOuvir = $("#dataOuvir").attr("data-ouvir")
	var validacao = true;
	var next = $("#verificar");
	$('#rootwizard').bootstrapWizard({onNext: function(tab, navigation, index) {
			// Make sure we entered the name
			if(validacao){
				countPalavra++;
			
			var tabId = tab.attr('data-id');

			var strong = $("strong[data-strong$="+tabId+"]");
			var span = $("span[data-span$="+tabId+"]");
			var msg = $("div[data-msg$="+tabId+"]");
			
			
			var resposta = $("input[id$="+tabId+"]");
			var palavra = $("input[data-palavra$="+tabId+"]");
			if(resposta.val() == ""){
				span.html("Você precisa digitar a palavra");
				msg.addClass("alert-danger");
				msg.show();
				return false;
			}
			
			var respostaText = resposta.val().trim().toLowerCase();
			var palavraText = palavra.val().trim().toLowerCase();
			var p = new Object();
			p.id = tabId;
			if(respostaText == palavraText){
				strong.html("Parabéns!");
				span.html(" Você acertou, vamos para próxima...");
				msg.removeClass("alert-danger");
				msg.addClass("alert-success");
				msg.show();
				somAcerto(true);
				p.acerto = true;
				palavras.push(p);
				countAcertos++;
			}else{
				strong.html("Que pena!");
				span.html(" O correto seria:<strong> "+palavra.val()+"</strong>, mas continue tentando");
				msg.removeClass("alert-success");
				msg.addClass("alert-danger");
				somAcerto(false);
				msg.show();
				p.acerto = false;
				palavras.push(p);
			}
				next.text("Próxima Palavra");
				validacao = false;
				return false;
		}

	}, onTabShow: function(tab, navigation, index) {
		var tabId = tab.attr('data-id');
		validacao = true;
		next.text("Validar");
		var $total = navigation.find('li').length;
		var $current = index+1;
		var $percent = ($current/$total) * 100;
		$('#rootwizard .progress-bar').css({width:$percent+'%'});
		 $("input[id$="+tabId+"]").attr("autofocus","autofocus");
		 if(tabId == "-1"){
			 $("#totalPalavras").html(countPalavra);
			 $("#totalAcertos").html(countAcertos);
			 $("#totalErros").html(countPalavra - countAcertos);
		 }
	}});
	
	$('#rootwizard .finish').click(function() {
		$.ajax({
			url : dataFinalizar,
			type : 'GET',
			dataType : 'json',
			data : {
				dados : JSON.stringify(palavras)
			},
			statusCode : {
				200 : function(xhr) {
					 finalizou = true;
					window.location.href = dataPraticar;
				}
			}
		});
	});
	
	$('#rootwizard .progress-bar').css({width:'0%'});
  	
	
//	window.onbeforeunload = function(event) {
//		var tabId = 0;
//		if(!finalizou){
//	    var s; 
//	    if(tabId == "-1"){
//	    	s = "É necessário computar o seu progesso. Clique em Finalizar";
//	    }else{
//	    	s = "Você ainda não terminou a pratica, desejar sair?";
//	    }
//
//	    event = event || window.event;
//	    if (event) {
//	        // This is for IE
//	        event.returnValue = s;
//	    }
//
//	    // This is for all other browsers
//	    return s;
//		}
//	}
	
});

function somAcerto(resultado) {

	var response = $.ajax({
		url : dataSom,
		type : 'GET',
		data : {
			acerto : resultado
		},
		statusCode : {
			200 : function(xhr) {
				var resposta = xhr.responseText;
				$("#audio").attr("src","data:audio/mpeg;base64,"+resposta);
				document.getElementById("audio").play();
			}
		}
	});

}

function validar(e){
    if(e.keyCode === 13){
        e.preventDefault(); // Ensure it is only this code that rusn
        $("li[class='next']").click();

    }
}

function ouvir(palavra, idioma){
	
	$.ajax({
		url : dataOuvir,
		type : 'GET',
		data : {
			palavra : palavra,
			idioma	: idioma
		},
		statusCode : {
			200 : function(xhr) {
				var resposta = xhr.responseText;
				$("#audio").attr("src", "data:audio/mpeg;base64," + resposta);
				document.getElementById("audio").play();
			}
		}
	});
	
	console.log(palavra);
	console.log(idioma);
}

