<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <meta name="layout" content="main">
  <title></title>
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
      <p>
        <table class="table table-condensed table-hover">
          <tr>
            <th>Date in M/DD/YYYY format</th>
            <th>Description of transaction</th>
            <th>Amount with no dollar sign</th>
            <th>Category Name (If it doesn't exist, it will be created)</th>
            <th>Account Name (Must match exactly how it is in the system)</th>
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
      </p>
      <g:uploadForm action="upload">
        <input type="file" name="file">
        <g:submitButton name="upload" value="Upload" class="btn btn-primary" />
      </g:uploadForm>
    </div>
  </div>
</div>

</body>
</html>