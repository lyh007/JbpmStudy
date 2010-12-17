<html>
<head>
  <style type="text/css">
   ${CSS!".body {font-family:sans-serif;}"}
  </style>
</head>
  <body>

    <form action="${form.action}" method="POST" enctype="multipart/form-data">
    
      <h3>Your employee, ${employee_name} would like to go on vacation</h3>
      Number of days: ${number_of_days}<br/>
      
      <hr>
      
      In case you reject, please provide a reason:<br/>
      <input type="textarea" name="reason"/><br/>
      
      <#list outcome.values as transition>
          <input type="submit" name="outcome" value="${transition}">
      </#list>
      
    </form>
  </body>
</html>