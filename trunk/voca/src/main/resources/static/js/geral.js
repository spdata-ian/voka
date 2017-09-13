$(function() {
	$('[data-toggle="tooltip"]').tooltip()

	var date_input = $(".js-date");
	date_input.datepicker({
		format : 'dd/mm/yyyy',
		todayHighlight : true,
		autoclose : true,
		language : 'pt-BR'
	});
	date_input.mask('00/00/0000');

	var month_input = $(".js-month");
	month_input.datepicker({
		format : 'mm/yyyy',
		todayHighlight : true,
		autoclose : true,
		language : 'pt-BR',
		startView : "months",
		minViewMode : "months"
	});
	month_input.mask('00/0000');

	var time_input = $(".js-hour");
	// time_input.datetimepicker({
	// format: 'HH:ii p',
	// autoclose: true,
	// // todayHighlight: true,
	// showMeridian: true,
	// startView: 1,
	// maxView: 1,
	// language: 'pt-BR'
	// });
	time_input.mask('00:00');

})

$(".js-tr").on("click", function(event) {
	var data = $(event.currentTarget).data('id');
	var url = $(event.currentTarget).data('url');
	window.location = url + "?data=" + data;

})

function clearAlertClasses(){
	$("#messagem").removeClass("alert-warning");
	$("#messagem").removeClass("alert-danger");
	$("#messagem").removeClass("alert-success");
}

function addSucsessMessage(sumary, text){
	clearAlertClasses();
	$("#sumary").html(sumary);
	$("#text").html(text)
	$("#messagem").addClass("alert-success");
	$("#messagem").show();
}

function addErroMessage(sumary, text){
	clearAlertClasses();
	$("#sumary").html(sumary);
	$("#text").html(text)
	$("#messagem").addClass("alert-danger");
	$("#messagem").show();
}
