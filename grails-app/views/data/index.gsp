<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <meta name="layout" content="main">
  <title>Import/Export</title>
</head>

<body>
<div id="content">
  <div class="row">
    <div class="col-md-8 col-md-offset-2">
      <h1>Import</h1>
      <g:if test="${flash.message}">
        <div class="alert alert-info">
          ${flash.message}
        </div>
      </g:if>
      <p>Upload a CSV that doesn't have column headers, has 5 columns, and has the following format:</p>
      <div class="table-responsive">
        <table class="table table-condensed table-hover">
          <tr>
            <th>Date as M/D/YYYY</th>
            <th>Description</th>
            <th>Amount</th>
            <th>Category</th>
            <th>Account Name (Must match the name in the system)</th>
          </tr>
          <tr>
            <td>1/4/2015</td>
            <td>Kroger</td>
            <td>123.09</td>
            <td>Groceries</td>
            <td>My Checking Account</td>
          </tr>
          <tr>
            <td>12/10/2014</td>
            <td>Longhorn Steakhouse</td>
            <td>54.12</td>
            <td>Restaurants</td>
            <td>Capital One Credit Card</td>
          </tr>
        </table>
      </div>

      <g:uploadForm action="import">
        <input type="file" name="file"><br>
        <g:submitButton name="upload" value="Import" class="btn btn-primary" />
      </g:uploadForm>
      <hr>
    </div>
  </div>
  <div class="row">
    <div class="col-md-8 col-md-offset-2">
      <h1>Export</h1>
      <p>Select the format you'd like to export your transactions to:</p>
      <ul>
        <li><g:link action="exportToExcel" target="_blank">Excel</g:link></li>
      </ul>
    </div>
  </div>
</div>

</body>
</html>