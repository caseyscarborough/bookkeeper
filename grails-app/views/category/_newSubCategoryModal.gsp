<div class="modal fade" id="create-subcategory-modal" tabindex="-1" role="dialog" aria-labelledby="create-subcategory-label" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <form id="create-subcategory-form" onsubmit="return false">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
          <h4 class="modal-title" id="create-subcategory-label">New SubCategory</h4>
        </div>
        <div class="modal-body">
          <div id="subcategory-create-error" class="alert alert-danger" style="display:none">
            <div id="subcategory-create-error-message"></div>
          </div>
          <div class="form-group">
            <label for="create-name">Name</label>
            <input type="text" class="form-control create modal-domain-property" name="name" id="create-name" placeholder="Name" autofocus="true">
          </div>

          <div class="form-group">
            <label for="create-category">Category</label>
            <select class="form-control create modal-domain-property" name="category" id="create-category">
              <g:each in="${categories}" var="category">
                <option value="${category.id}">${category.name}</option>
              </g:each>
            </select>
          </div>

          <div class="form-group">
            <label for="create-type">Type</label>
            <select id="create-type" name="type" class="form-control create modal-domain-property">
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
