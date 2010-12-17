<html>
<head>
  <style type="text/css">   
   ${CSS!".body {font-family:sans-serif;}"}
  </style>
</head>
  <body>
    
    <form action="${form.action}" method="POST" enctype="multipart/form-data">
    
      <h3>How many days would you like to go on vacation?</h3>
      <select name="number_of_days">
      	<option value="3">3 days</option>
      	<option value="5">5 days</option>
      	<option value="10">10 days</option>
      </select><br>
      
      <br/>
      <br/>
 
      Your name: <input type="text" name="employee_name" /><br/>
 
      <br/>
      <br/>
 
      <input type="submit" name="Done"/>

    </form>
  </body>
</html>