<div class="modal fade" id="edit-subcategory-modal" tabindex="-1" role="dialog" aria-labelledby="edit-subcategory-label" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <form id="edit-subcategory-form" onsubmit="return false">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
          <h4 class="modal-title" id="edit-subcategory-label">Edit SubCategory</h4>
        </div>
        <div class="modal-body">
          <div id="subcategory-edit-error" class="alert alert-danger" style="display:none">
            <div id="subcategory-edit-error-message"></div>
          </div>

          <input id="edit-id" type="hidden" value="" name="id" class="modal-domain-property">

          <div class="form-group">
            <label for="edit-name">Name</label>
            <input type="text" class="form-control modal-domain-property" name="name" id="edit-name" placeholder="Name" autofocus="true">
          </div>

          <div class="form-group">
            <label for="edit-category">Category</label>
            <select class="form-control modal-domain-property" name="category" id="edit-category">
              <g:each in="${categories}" var="category">
                <option value="${category.id}">${category.name}</option>
              </g:each>
            </select>
          </div>

          <div class="form-group">
            <label for="edit-type">Type</label>
            <select id="edit-type" name="type" class="form-control modal-domain-property">
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