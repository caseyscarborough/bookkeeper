$(function () {
    $(".delete-subcategory").click(function () {
        var id = $(this).attr("data-id");
        deleteSubCategory(id, function () {
            $("#subcategory-" + id).fadeOut();
        }, function (response) {
            alert(response.responseJSON.message);
        });
    });

    $(".delete-category").click(function () {
        var id = $(this).attr("data-id");
        deleteCategory(id, function () {
            window.location.reload();
        }, function (response) {
            alert(response.responseJSON.message);
        });
    });

    $(".edit-category").click(function () {
        var id = $(this).attr("data-id");
        var name = $(this).attr("data-name");
        $("#edit-category-id").val(id);
        $("#edit-category-name").val(name);
        $("#edit-category-modal").modal('show');
    });

    $("#edit-category-form").on('submit', function () {
        var data = {id: $("#edit-category-id").val(), name: $("#edit-category-name").val()};
        updateCategory(data, function () {
            window.location.reload()
        }, function (response) {
            showErrorMessage("#edit-category-error", response.responseJSON.message, "edit-category-" + response.responseJSON.field);
        });
    });

    $(".new-category").click(function () {
        $("#create-category-modal").modal('show');
    });

    $("#create-category-form").on('submit', function () {
        var data = {name: $("#create-category-name").val(), type: $("#create-category-type").val()};
        createCategory(data, function () {
            window.location.reload()
        }, function (response) {
            showErrorMessage("#create-category-error", response.responseJSON.message, "create-category-" + response.responseJSON.field);
        });
    });

    $(".edit-subcategory").click(function () {
        var id = $(this).attr("data-id");
        var category = $(this).attr("data-category");
        var type = $("#subcategory-" + id + "-type").html();
        console.log(category);
        $("#edit-id").val(id);
        $("#edit-name").val($("#subcategory-" + id + "-name").html());
        $("#edit-category option:contains(" + category + ")").attr('selected', true);
        $("#edit-type option:contains(" + type + ")").attr('selected', true);
        $("#edit-subcategory-modal").modal('show');
    });

    $("#edit-subcategory-form").on('submit', function () {
        var data = {};
        $(".edit.modal-domain-property").each(function () {
            data[$(this).attr("name")] = $(this).val();
        });
        updateSubCategory(data, function () {
            window.location.reload();
        }, function (response) {
            showErrorMessage("#subcategory-error", response.responseJSON.message, response.responseJSON.field);
        });
    });

    $("#create-subcategory-form").on('submit', function () {
        var data = {};
        $(".create.modal-domain-property").each(function () {
            if ($(this).attr("disabled") === "disabled") {
            } else {
                data[$(this).attr("name")] = $(this).val();
            }
        });

        createSubCategory(data, function () {
            window.location.reload();
        }, function (response) {
            showErrorMessage("#subcategory-create-error", response.responseJSON.message, "create-" + response.responseJSON.field);
        });
    });

    $(".new-subcategory").click(function () {
        $("#create-subcategory-modal").modal('show');
    });

    $(".show-subcategories").click(function () {
        var id = $(this).attr("data-id");
        $("#subcategories-" + id).show();
        $(this).hide();
        $("#hide-" + id).show();
    });

    $(".hide-subcategories").click(function () {
        var id = $(this).attr("data-id");
        $("#subcategories-" + id).hide();
        $(this).hide();
        $("#show-" + id).show();
    });
});