var dataSom;
var dataFinalizar;
var dataPraticar;
var dataOuvir;
var palavras = [];
var countPalavra = 0;
var countAcertos = 0;
var finalizou = false;
$(document)
		.ready(
				function() {

					dataSom = $("#dataSom").attr("data-som")
					dataFinalizar = $("#dataFinalizar").attr("data-finalizar")
					dataPraticar = $("#dataPraticar").attr("data-praticar")
					dataOuvir = $("#dataOuvir").attr("data-ouvir")
					var validacao = true;
					var next = $("#verificar");
					$('#rootwizard')
							.bootstrapWizard(
									{
										onNext : function(tab, navigation,
												index) {
											// Make sure we entered the name
											if (validacao) {
												countPalavra++;

												var tabId = tab.attr('data-id');

												var strong = $("strong[data-strong$="
														+ tabId + "]");
												var span = $("span[data-span$="
														+ tabId + "]");
												var msg = $("div[data-msg$="
														+ tabId + "]");

												var resposta = $("input[id$="
														+ tabId + "]");
												var palavra = $("input[data-palavra$="
														+ tabId + "]");
												if (resposta.val() == "") {
													span
															.html("Você precisa digitar a palavra");
													msg
															.addClass("alert-danger");
													msg.show();
													return false;
												}

												var respostaText = resposta
														.val().trim()
														.toLowerCase();
												var palavraText = palavra.val()
														.trim().toLowerCase();
												var p = new Object();
												p.id = tabId;
												if (respostaText == palavraText) {
													strong.html("Parabéns!");
													span
															.html(" Você acertou, vamos para próxima...");
													msg
															.removeClass("alert-danger");
													msg
															.addClass("alert-success");
													msg.show();
													somAcerto(true);
													p.acerto = true;
													palavras.push(p);
													countAcertos++;
												} else {
													strong.html("Que pena!");
													span
															.html(" O correto seria:<strong> "
																	+ palavra
																			.val()
																	+ "</strong>, mas continue tentando");
													msg
															.removeClass("alert-success");
													msg
															.addClass("alert-danger");
													somAcerto(false);
													msg.show();
													p.acerto = false;
													palavras.push(p);
												}
												next.text("Próxima Palavra");
												validacao = false;
												return false;
											}

										},
										onTabShow : function(tab, navigation,
												index) {
											var tabId = tab.attr('data-id');
											validacao = true;
											next.text("Validar");
											var $total = navigation.find('li').length;
											var $current = index + 1;
											var $percent = ($current / $total) * 100;
											$('#rootwizard .progress-bar').css(
													{
														width : $percent + '%'
													});
											$("input[id$=" + tabId + "]").attr(
													"autofocus", "autofocus");
											if (tabId == "-1") {
												// $("#totalPalavras").html(countPalavra);
												// $("#totalAcertos").html(countAcertos);
												// $("#totalErros").html(countPalavra
												// - countAcertos);
												// code before the pause
												setTimeout(function() {
													showChart();
												}, 100);
											}
										}
									});

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

					$('#rootwizard .progress-bar').css({
						width : '0%'
					});
				});

function somAcerto(resultado) {
	var c = resultado ? "acerto" : "erro";

	ion.sound({
		sounds : [ {
			name : c
		} ],

		// main config
		path : dataSom + "?acerto=" + resultado + "&a=",
		preload : true,
		multiplay : true,
		volume : 0.4
	});

	playSoud(c);

}

function validar(e) {
	if (e.keyCode === 13) {
		e.preventDefault(); // Ensure it is only this code that rusn
		$("li[class='next']").click();

	}
}

function ouvir(palavra, idioma) {
	configureSound(idioma, palavra);
	playSoud(palavra);
}

function configureSound(lan, word) {
	ion.sound({
		sounds : [ {
			name : word
		} ],

		// main config
		path : dataOuvir + "?l=" + lan + "&w=" + word + "&a=",
		preload : true,
		multiplay : true,
		volume : 0.9

	});
}

// play sound
function playSoud(word) {
	ion.sound.play(word);
}

// CHART

Chart.defaults.global.animation.duration = 2000
var dataP;
function configureChart() {

	dataP = {
		labels : [ "Acertos", "Erros" ],
		datasets : [ {
			backgroundColor : [ 'rgba(54, 162, 235, 1)',
					'rgba(255, 99, 132, 1)', 'rgba(255, 206, 86, 1)',
					'rgba(75, 192, 192, 0.2)', 'rgba(153, 102, 255, 0.2)',
					'rgba(255, 159, 64, 0.2)' ],
			borderColor : [ 'rgba(54, 162, 235, 1)', 'rgba(255,99,132,1)',
					'rgba(255, 206, 86, 1)', 'rgba(75, 192, 192, 1)',
					'rgba(153, 102, 255, 1)', 'rgba(255, 159, 64, 1)' ],
			borderWidth : 2,
			data : valores(),
		} ]
	};
}

var optionsP = {
	maintainAspectRatio : false,
	display : true,
	position : 'top',

	pieceLabel : {
		// render 'label', 'value', 'percentage', 'image' or custom function,
		// default is 'percentage'
		render : 'percentage',

		// precision for percentage, default is 0
		precision : 0,

		// identifies whether or not labels of value 0 are displayed, default is
		// false
		showZero : true,

		// font size, default is defaultFontSize
		fontSize : 18,

		// font color, can be color array for each data or function for dynamic
		// color, default is defaultFontColor
		fontColor : '#fff',

		// font style, default is defaultFontStyle
		fontStyle : 'normal',

		// font family, default is defaultFontFamily
		fontFamily : "'Helvetica Neue', 'Helvetica', 'Arial', sans-serif",

		// draw label in arc, default is false
		arc : true,

		// position to draw label, available value is 'default', 'border' and
		// 'outside'
		// default is 'default'
		position : 'default',

		// draw label even it's overlap, default is false
		overlap : true
	}
};

var ctx = document.getElementById("chartP");

function showChart() {
	configureChart()
	var myPieChart = new Chart(ctx, {
		type : 'pie',
		data : dataP,
		options : optionsP

	});

}

function valores() {
	console.log('pegando valores')
	var ar = [];
	ar.push(countAcertos);
	ar.push((countPalavra - countAcertos))
	// ar.push(1);
	// ar.push(1);
	return ar;
}
