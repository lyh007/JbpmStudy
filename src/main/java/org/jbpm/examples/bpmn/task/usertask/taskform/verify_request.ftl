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
     
      <input type="submit" name="verificationResult" value="OK">
      <input type="submit" name="verificationResult" value="Not OK">
      
    </form>
  </body>
</html>