<!DOCTYPE html>
<html lang="en" dir="ltr">
  <head>
    <meta charset="utf-8">
      <link rel="stylesheet" href="css/registerpatient.css">
      <link href="https://fonts.googleapis.com/css?family=Open+Sans:600&display=swap" rel="stylesheet">
    <title>DocApp | Register</title>
  </head>

  <body>
    <div class="heading">
      <h1>ADD DOCTOR</h1>
    </div>

    <div class="register-card">
      <form action="ManageDoctors" method="get">
        <label for="fname">First Name</label>
        <input type="text" name="fname" placeholder="Doctor's first name..." required>
        <label for="lname">Last Name</label>
        <input type="text" name="lname" placeholder="Doctor's last name..." required>
        <label for="age">Age</label>
        <input type="number" name="age" placeholder="Doctor's age..." required>
        <label for="specialization">Specialization</label>
        <input type="text" name="specialization"  placeholder="Doctor's specialization..." required>
        <label for="email">Email</label>
        <input type="email" name="email" placeholder="Doctor's email..." required>
        <label for="password">Password</label>
        <input type="password" name="password" placeholder="Doctor's intital password..." required>
        <input type="submit" name="add "value="Add Doctor">
        <input type='hidden' name='selected' value="adddoctor" />
      </form>

      <form action="AdminListDetails">
        <input type="hidden" name="selected" value="removedoctors">
        <input type="submit" value="Back">
      </form>

      <%
          String success_msg = (String) request.getAttribute("success_msg");
          if(success_msg != null) {
              out.println("<h4 style=\"color: blue;\">" + success_msg + "</h4>");
          }
       %>

    </div>
  </body>

  <script type="text/javascript" src="jquery-3.4.1.js"></script>
  <script>
  $('input[type=submit]').click(function() {
    console.log("Done");
      var parent_form = $(this).closest('form');
      parent_form.append('<input type="hidden" name="search_key" value=\"\"/>');
    });
  </script>
</html>
