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
	time_input.mask('00:00');
	
	$( document ).ajaxStart(function() {
		blockUI();
	});
	
	$( document ).ajaxComplete(function() {
		unblockUI();
	});

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

function addGrowl(summary, text, type){
	$.notify("<strong>"+summary+"</strong> "+text, {
		animate: {
			enter: 'animated bounceInDown',
			exit: 'animated bounceOutUp'
		},
		type: type,
	});
}

function blockUI(){
	$.blockUI({ 
        message: $('#displayBox'), 
        css: { 
        	 backgroundColor: 'transparent',
        	 border: 'none'
        } 
    });
}

function unblockUI(){
	$.unblockUI();
}
