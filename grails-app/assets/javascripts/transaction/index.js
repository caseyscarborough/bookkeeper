$(function () {
    setupAutocomplete(TRANSACTION_QUERY_URL, "#description", "#subCategory", "#toAccount");
    setupAutocomplete(TRANSACTION_QUERY_URL, "#edit-description", "#edit-subCategory", "#edit-toAccount");
    setupAutocomplete(TRANSACTION_QUERY_URL, "#mobile-description", "#mobile-subCategory", "#mobile-toAccount");

    $("#date").datepicker();
    $("#edit-date").datepicker();
    $("#mobile-date").datepicker();

    $("#show-new-mobile-transaction").click(function () {
        $("#mobile-new-transaction-form").show();
        $(this).hide();
        $("#hide-new-mobile-transaction").show();
    });

    $("#hide-new-mobile-transaction").click(function () {
        $("#show-new-mobile-transaction").show();
        $("#mobile-new-transaction-form").hide();
        $(this).hide();
    });

    $("#new-transaction-form").on('submit', function () {
        var data = getData(".domain-property");
        var fileData = $("#receipt").prop('files')[0];
        if (fileData) {
            data.receipt = fileData;
        }
        createTransaction(data, function () {
            window.location.reload();
        }, function (response) {
            showErrorMessage("#transaction-error", response.responseJSON.message, response.responseJSON.field);
        });
    });

    $("#mobile-new-transaction-form").on('submit', function () {
        var data = getData(".mobile-domain-property");
        createTransaction(data, function () {
            window.location.reload();
        }, function (response) {
            showErrorMessage("#transaction-error", response.responseJSON.message, "mobile-" + response.responseJSON.field);
        });
    });

    $("#edit-transaction-form").on('submit', function () {
        var data = getData(".modal-domain-property");
        var fileData = $("#edit-receipt").prop('files')[0];
        if (fileData) {
            data.receipt = fileData;
        }
        updateTransaction(data, function () {
            window.location.reload();
        }, function (response) {
            showErrorMessage("#transaction-edit-error", response.responseJSON.message, response.responseJSON.field);
        });
    });

    $(".transaction-delete").on('click', function () {
        var id = $(this).attr("data-id");
        deleteTransaction(id, function () {
            $(".transaction-" + id).fadeOut();
        }, function () {
        });
    });

    updateToAccount();
    $("#subCategory").on('change', function () {
        updateToAccount();
    });

    updateEditToAccount();
    $("#edit-subCategory").on('change', function () {
        updateEditToAccount();
    });

    updateMobileToAccount();
    $("#mobile-subCategory").on('change', function () {
        updateMobileToAccount();
    });

    $("#reset").click(function () {
        window.location.href = location.protocol + '//' + location.host + location.pathname
    });

    $("#search").click(function () {
        var currentUrl = location.protocol + '//' + location.host + location.pathname;
        var queryChar = '?';
        var category = $("#filter-category").val();
        var account = $("#filter-account").val();
        var description = $("#filter-description").val();
        var max = $("#filter-max").val();

        if (category !== null) {
            currentUrl += queryChar + 'category=' + category;
            queryChar = '&';
        }

        if (account !== null) {
            currentUrl += queryChar + 'account=' + account;
            queryChar = '&';
        }

        if (description !== "") {
            currentUrl += queryChar + 'description=' + description;
            queryChar = '&';
        }

        if (max !== null) {
            currentUrl += queryChar + 'max=' + max;
        }

        if (queryChar === '&' || max != params_max) {
            window.location.href = currentUrl;
        }
    });

    $(".transaction-edit").click(function () {
        var id = $(this).attr("data-id");
        $("#edit-id").val(id);
        $("#edit-date").val($("#transaction-" + id + "-date").html());
        $("#edit-amount").val($("#transaction-" + id + "-amount").html());
        $("#edit-description").val($("#transaction-" + id + "-description").html());
        $("#edit-fromAccount option:contains(" + $('#transaction-' + id + '-fromAccount').html() + ")").attr('selected', true);
        $("#edit-subCategory option:contains(" + $('#transaction-' + id + '-subCategory').html() + ")").attr('selected', true);
        $("#edit-transaction-modal").modal('show');
        updateEditToAccount();
    });

    $(".view-receipt").click(function () {
        var url = $(this).attr("data-url");
        $("#current-receipt").attr("src", url);
        $("#view-receipt-modal").modal('show');
    });

    $("#synchronize").click(function () {
        $(this).button('loading');
        $.ajax({
            url: TRANSACTION_SYNC_URL,
            success: function () {
                window.location.reload();
            }
        });
    })
});