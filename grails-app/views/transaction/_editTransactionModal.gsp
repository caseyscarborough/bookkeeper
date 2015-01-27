<div class="modal fade" id="edit-transaction-modal" tabindex="-1" role="dialog" aria-labelledby="edit-transaction-label" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <form id="edit-transaction-form" onsubmit="return false">
        <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="edit-transaction-label">Edit Transaction</h4>
      </div>
      <div class="modal-body">
          <div id="transaction-edit-error" class="alert alert-danger" style="display:none">
            <div id="transaction-edit-error-message"></div>
          </div>

          <input id="edit-id" type="hidden" value="" name="id" class="modal-domain-property">
          <div class="form-group">
            <label for="edit-description">Transaction Description</label>
            <input type="text" class="form-control modal-domain-property" name="description" id="edit-description" placeholder="Transaction Description" autofocus="true">
          </div>

          <div class="form-group">
            <label for="edit-amount">Amount</label>
            <input type="number" class="form-control modal-domain-property" name="amount" id="edit-amount" step="0.01" placeholder="Amount">
          </div>

          <div class="form-group">
            <label for="edit-date">Date</label>
            <input type="text" class="form-control modal-domain-property" name="date" id="edit-date" placeholder="Date">
          </div>

          <div class="form-group">
            <label for="edit-fromAccount">From Account</label>
            <select class="form-control modal-domain-property" name="fromAccount" id="edit-fromAccount">
              <g:each in="${accounts}" var="account">
                <option value="${account.id}">${account.description}</option>
              </g:each>
            </select>
          </div>

          <div class="form-group">
            <label for="edit-subCategory">Category</label>
            <select class="form-control modal-domain-property" name="subCategory" id="edit-subCategory">
              <g:each in="${categories}" var="category">
                <optgroup label="${category.name}">
                  <g:each in="${category.subcategories?.sort { it.name }}" var="subcategory">
                    <option data-type="${subcategory.type}" value="${subcategory.id}">${subcategory.name}</option>
                  </g:each>
                </optgroup>
              </g:each>
            </select>
          </div>

          <div class="form-group">
            <label for="edit-toAccount">To Account</label>
            <select class="form-control domain-property" id="edit-toAccount" name="toAccount" disabled="disabled">
              <g:each in="${accounts}" var="account">
                <option value="${account.id}">${account.description}</option>
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