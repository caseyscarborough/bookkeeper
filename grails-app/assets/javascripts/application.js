// This is a manifest file that'll be compiled into application.js.
//
// Any JavaScript file within this directory can be referenced here using a relative path.
//
// You're free to add application-wide JavaScript to this file, but it's generally better 
// to create separate JavaScript files as needed.
//
//= require jquery/dist/jquery.min
//= require bootstrap/dist/js/bootstrap.min
//= require flat-ui/dist/js/flat-ui.min
//= require devbridge-autocomplete/dist/jquery.autocomplete.min
//= require bootstrap-datepicker/js/bootstrap-datepicker
//= require highcharts/highcharts
//= require highcharts/modules/drilldown
//= require spin.js/spin.js
//= require urls
//= require_self

if (typeof jQuery !== 'undefined') {
	(function($) {
		$('#spinner').ajaxStart(function() {
			$(this).fadeIn();
		}).ajaxStop(function() {
			$(this).fadeOut();
		});
	})(jQuery);
}

function showErrorMessage(selector, message, field) {
	$(selector + "-message").html(message);
	$(".domain-property").parent().removeClass('has-error');
	$(".modal-domain-property").parent().removeClass('has-error');
	$("#" + field).focus().parent().addClass('has-error');
	$(selector).show();
}

function deleteTransaction(id, success, error) {
	$.ajax({
		type: "delete",
		url: TRANSACTION_DELETE_URL + "/" + id,
		success: function(response) {
			if (success && typeof success === 'function') {
				success(response)
			}
		},
		error: function(response) {
			if (error && typeof error === 'function') {
				error(response)
			}
		}
	});
}

function updateTransaction(data, success, error) {
	var formData = new FormData();
	$.each(data, function (k, v) {
		formData.append(k, v);
	});
	$.ajax({
		type: "post",
		data: formData,
		url: TRANSACTION_UPDATE_URL,
		dataType: 'json',
		processData: false,
		contentType: false,
		success: function(response) {
			if (success && typeof success === 'function') {
				success(response)
			}
		},
		error: function(response) {
			if (error && typeof error === 'function') {
				error(response)
			}
		}
	});
}

function createTransaction(data, success, error) {
	var formData = new FormData();
	$.each(data, function (k, v) {
		formData.append(k, v);
	});
	$.ajax({
		type: "post",
		data: formData,
		url: TRANSACTION_CREATE_URL,
		dataType: 'json',
		processData: false,
		contentType: false,
		success: function(response) {
			if (success && typeof success === 'function') {
				success(response)
			}
		},
		error: function(response) {
			if (error && typeof error === 'function') {
				error(response)
			}
		}
	});
}

function getData(selector) {
	var data = {};
	$(selector).each(function () {
		if ($(this).attr("disabled") === "disabled") {
		} else {
			data[$(this).attr("name")] = $(this).val();
		}
	});
	return data;
}

// TODO: Refactor the following two functions
function updateToAccount() {
	if ($("#subCategory option:selected").attr("data-type") === "Transfer") {
		$("#toAccount").removeAttr("disabled");
	} else {
		$("#toAccount").attr("disabled", "disabled");
	}
}

function updateEditToAccount() {
	if ($("#edit-subCategory option:selected").attr("data-type") === "Transfer") {
		$("#edit-toAccount").removeAttr("disabled");
	} else {
		$("#edit-toAccount").attr("disabled", "disabled");
	}
}