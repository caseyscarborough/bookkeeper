<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <meta name="layout" content="main">
  <title></title>
</head>

<body>
<div id="content">
  <div class="row">
    <div class="col-md-12">
      <h1>Import</h1>
      <p>Use at your own risk. Instructions to come.</p>
      <g:uploadForm action="upload">
        <input type="file" name="file">
        <g:submitButton name="upload" value="Upload" class="btn btn-primary" />
      </g:uploadForm>
    </div>
  </div>
</div>

</body>
</html>