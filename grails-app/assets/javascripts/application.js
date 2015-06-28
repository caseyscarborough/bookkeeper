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
//= require graphs
//= require_self

if (typeof jQuery !== 'undefined') {
    (function ($) {
        $('#spinner').ajaxStart(function () {
            $(this).fadeIn();
        }).ajaxStop(function () {
            $(this).fadeOut();
        });
    })(jQuery);
}

$(function () {
    $(".tooltip-link").tooltip();

    $(".sorted.asc").append('&nbsp;<small class="glyphicon glyphicon-triangle-bottom"></small>');
    $(".sorted.desc").append('&nbsp;<small class="glyphicon glyphicon-triangle-top"></small>');
});

function showErrorMessage(selector, message, field) {
    $(selector + "-message").html(message);
    $(".domain-property").parent().removeClass('has-error');
    $(".modal-domain-property").parent().removeClass('has-error');
    $("#" + field).focus().parent().addClass('has-error');
    $(selector).show();
}

function getTodaysDate() {
    var today = new Date();
    var dd = today.getDate();
    var mm = today.getMonth() + 1; //January is 0!
    var yyyy = today.getFullYear();

    if (dd < 10) {
        dd = '0' + dd
    }

    if (mm < 10) {
        mm = '0' + mm
    }
    return mm + '/' + dd + '/' + yyyy;
}

function getData(selector) {
    var data = {};
    $(selector).each(function () {
        if ($(this).attr("disabled") === "disabled") {
        } else if ($(this).attr("type") === "checkbox") {
            data[$(this).attr("name")] = $(this).is(':checked');
        } else {
            data[$(this).attr("name")] = $(this).val();
        }
    });
    return data;
}

function convertToFormData(data) {
    var formData = new FormData();
    $.each(data, function (k, v) {
        formData.append(k, v);
    });
    return formData;
}

function postRequestWithFileFormData(url, data, success, error) {
    $.ajax({
        type: "post",
        data: data,
        url: url,
        dataType: 'json',
        processData: false,
        contentType: false,
        success: function (response) {
            if (success && typeof success === 'function') {
                success(response)
            }
        },
        error: function (response) {
            if (error && typeof error === 'function') {
                error(response)
            }
        }
    });
}

function postRequest(url, data, success, error) {
    $.ajax({
        type: "post",
        data: data,
        url: url,
        dataType: 'json',
        success: function (response) {
            if (success && typeof success === 'function') {
                success(response)
            }
        },
        error: function (response) {
            if (error && typeof error === 'function') {
                error(response)
            }
        }
    });
}

function deleteRequest(url, success, error) {
    $.ajax({
        type: "delete",
        url: url,
        success: function (response) {
            if (success && typeof success === 'function') {
                success(response)
            }
        },
        error: function (response) {
            if (error && typeof error === 'function') {
                error(response)
            }
        }
    });
}

function setupAutocomplete(queryUrl, descriptionSelector, subcategorySelector, toAccountSelector) {
    $(descriptionSelector).autocomplete({
        serviceUrl: queryUrl,
        minChars: 3,
        autoSelectFirst: true,
        formatResult: function (suggestion, currentValue) {
            return suggestion.value + " (" + suggestion.data.category.name + ")";
        },
        onSelect: function (suggestion) {
            $(descriptionSelector).val(suggestion.data.description);
            $(subcategorySelector).val(suggestion.data.id);
            if (suggestion.data.toAccount !== null) {
                $(toAccountSelector).val(suggestion.data.toAccount);
            }
            updateToAccount();
        }
    });
}

function deleteTransaction(id, success, error) {
    deleteRequest(TRANSACTION_DELETE_URL + "/" + id, success, error);
}

function updateTransaction(data, success, error) {
    data = convertToFormData(data);
    postRequestWithFileFormData(TRANSACTION_UPDATE_URL, data, success, error);
}

function createTransaction(data, success, error) {
    data = convertToFormData(data);
    postRequestWithFileFormData(TRANSACTION_CREATE_URL, data, success, error);
}

function duplicateTransaction(data, success, error) {
    postRequest(TRANSACTION_DUPLICATE_URL, data, success, error);
}

function createAccount(data, success, error) {
    postRequest(ACCOUNT_CREATE_URL, data, success, error);
}

function updateAccount(data, success, error) {
    postRequest(ACCOUNT_UPDATE_URL, data, success, error);
}

function deleteAccount(id, success, error) {
    deleteRequest(ACCOUNT_DELETE_URL + "/" + id, success, error);
}

function createCategory(data, success, error) {
    postRequest(CATEGORY_CREATE_URL, data, success, error);
}

function updateCategory(data, success, error) {
    postRequest(CATEGORY_UPDATE_URL, data, success, error);
}

function deleteCategory(id, success, error) {
    deleteRequest(CATEGORY_DELETE_URL + "/" + id, success, error);
}

function createSubCategory(data, success, error) {
    postRequest(SUBCATEGORY_CREATE_URL, data, success, error);
}

function updateSubCategory(data, success, error) {
    postRequest(SUBCATEGORY_UPDATE_URL, data, success, error);
}

function deleteSubCategory(id, success, error) {
    deleteRequest(SUBCATEGORY_DELETE_URL + "/" + id, success, error);
}

// TODO: Refactor the following three functions
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

function updateMobileToAccount() {
    if ($("#mobile-subCategory option:selected").attr("data-type") === "Transfer") {
        $("#mobile-toAccount").removeAttr("disabled");
    } else {
        $("#mobile-toAccount").attr("disabled", "disabled");
    }
}