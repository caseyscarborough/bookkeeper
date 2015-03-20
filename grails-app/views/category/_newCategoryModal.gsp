<div class="modal fade" id="create-category-modal" tabindex="-1" role="dialog" aria-labelledby="create-category-label" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <form id="create-category-form" onsubmit="return false">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
          <h4 class="modal-title" id="create-category-label">Edit Category</h4>
        </div>
        <div class="modal-body">
          <div id="create-category-error" class="alert alert-danger" style="display:none">
            <div id="create-category-error-message"></div>
          </div>

          <input id="create-category-id" type="hidden" value="" name="id" class="modal-domain-property">

          <div class="form-group">
            <label for="create-category-name">Name</label>
            <input type="text" class="form-control modal-domain-property" name="name" id="create-category-name" placeholder="Name" autofocus="true">
          </div>

          <div class="form-group">
            <label for="create-category-type">Default Type</label>
            <select id="create-category-type" name="type" class="form-control create modal-domain-property">
              <g:each in="${categoryTypes}" var="type">
                <option value="${type.name()}">${type.name}</option>
              </g:each>
            </select>
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