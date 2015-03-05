<div class="modal fade" id="edit-account-modal" tabindex="-1" role="dialog" aria-labelledby="edit-account-label"
     aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
            aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="edit-account-label">Edit Account</h4>
      </div>

      <form id="edit-account-form" role="form" onsubmit="return false">

        <div class="modal-body">
          <div id="account-edit-error" class="alert alert-danger" style="display:none">
            <div id="account-edit-error-message"></div>
          </div>
          <input type="hidden" id="edit-account-id" name="id" class="modal-domain-property" value="">

          <div class="form-group">
            <label for="edit-account-description">Description</label>
            <input type="text" placeholder="Description" name="description" class="form-control modal-domain-property"
                   id="edit-account-description">
          </div>

          <div class="form-group">
            <label for="edit-account-balance">Balance</label>
            <input type="number" placeholder="Balance" name="balance" step="0.01" class="form-control modal-domain-property"
                   id="edit-account-balance">
          </div>
        </div>

        <div class="modal-footer">
          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
          <button type="submit" class="btn btn-primary">Save</button>
        </div>
      </form>
    </div>
  </div>
</div>