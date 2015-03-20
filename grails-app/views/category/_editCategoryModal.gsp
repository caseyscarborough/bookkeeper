<div class="modal fade" id="edit-category-modal" tabindex="-1" role="dialog" aria-labelledby="edit-category-label" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <form id="edit-category-form" onsubmit="return false">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
          <h4 class="modal-title" id="edit-category-label">Edit Category</h4>
        </div>
        <div class="modal-body">
          <div id="edit-category-error" class="alert alert-danger" style="display:none">
            <div id="edit-category-error-message"></div>
          </div>

          <input id="edit-category-id" type="hidden" value="" name="id" class="modal-domain-property">

          <div class="form-group">
            <label for="edit-category-name">Name</label>
            <input type="text" class="form-control modal-domain-property" name="name" id="edit-category-name" placeholder="Name" autofocus="true">
          </div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
          <button type="submit" class="btn btn-primary">Save Changes</button>
        </div>
      </form>
    </div>
  </div>
</div>