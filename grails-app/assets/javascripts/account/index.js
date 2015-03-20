function calculateCurrentTotal() {
    var total = 0;
    $(".include-in-total").each(function () {
        if ($(this).prop("checked")) {
            var balance = $("#account-" + $(this).attr("data-id") + "-balance");
            var isDebt = balance.attr("data-is-debt");
            var actualBalance = parseFloat(balance.html());
            total += isDebt === "true" ? -actualBalance : actualBalance;
        }
    });
    $("#total-balance").html(total.toFixed(2));
}

$(function () {
    calculateCurrentTotal();
    $(".include-in-total").change(function () {
        calculateCurrentTotal();
    });

    $("#new-account-form").on('submit', function () {
        var data = {};
        $(".domain-property").each(function () {
            data[$(this).attr("id")] = $(this).val();
        });

        $.ajax({
            type: "post",
            data: data,
            url: "${createLink(controller: 'account', action: 'save')}",
            dataType: 'json',
            success: function () {
                window.location.reload()
            },
            error: function (response) {
                showErrorMessage("#account-error", response.responseJSON.message, response.responseJSON.field);
            }
        });
    });

    $(".account-delete").on('click', function () {
        var id = $(this).attr("data-id");
        $.ajax({
            type: "delete",
            url: "${createLink(controller: 'account', action: 'delete')}/" + id,
            success: function () {
                $(".account-" + id).fadeOut();
                $("#account-error").hide();
            },
            error: function (response) {
                showErrorMessage("#account-error", response.responseJSON.message, response.responseJSON.field);
            }
        });
    });

    $(".account-edit").on('click', function () {
        var id = $(this).attr("data-id");
        $("#edit-account-description").val($("#account-" + id + "-description").html());
        $("#edit-account-id").val(id);
        $("#edit-account-balance").val($("#account-" + id + "-balance").html());
        if ($("#account-" + id + "-active").html() === "true") {
            console.log("Checked!");
            $("#edit-account-active").prop('checked', true);
        } else {
            console.log("Not checked!");
            $("#edit-account-active").prop('checked', false);
        }
        $("#edit-account-modal").modal('show');
    });

    $("#edit-account-form").on('submit', function () {
        var data = getData('.modal-domain-property');
        updateAccount(data, function () {
            window.location.reload();
        }, function (response) {
            showErrorMessage("#account-edit-error", response.responseJSON.message, "edit-account-" + response.responseJSON.field);
        })
    });
});