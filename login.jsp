<!DOCTYPE html>
<%@ page import="java.util.*, shared.beans.SessionBean, shared.dao.SessionDAO" %>

<html lang="en" dir="ltr">

<head>
  <meta charset="utf-8">
  <link rel="stylesheet" href="css/login.css">
  <link href="https://fonts.googleapis.com/css?family=Open+Sans:600&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Lato&display=swap" rel="stylesheet">
  <title>DocApp | Home</title>
</head>

<%
    SessionDAO sob = SessionDAO.getInstance();
    HttpSession current_session = request.getSession();

    if(sob.isSessionActive(current_session.getId())) {
      RequestDispatcher requestDispatcher = request.getRequestDispatcher("Logout");
      requestDispatcher.forward(request, response);
      return;
    }
%>

<body>
  <div class="heading">
      <h1>HOME</h1>
  </div>
  <div class="login-card">
    <form action="Login" method="post">
      <label for="email">Email</label>
      <input type="email" name="email" placeholder="Your email..." required/>

      <label for="password">Password</label>
      <input type="password" name="password" placeholder="Your password..." required/>

      <label for="user">User</label>
      <select name="user" required>
        <option value=""disabled selected>Select User Type</option>
        <option value="patient">Patient</option>
        <option value="doctor">Doctor</option>
      </select>
      <input type="submit" name="login" value="Login">
    </form>

    <form action="registerpatient.jsp" method="post">
      <input type="submit" name="register" value="If you are a new Patient, Register Here">
    </form>

    <form action="AdminListDetails" method="get">
      <input type="hidden" name="selected" value="removedoctors">
      <input type="submit" name="admin" value="Admin">
    </form>

     <%
     String success_msg = (String) request.getAttribute("success_msg");
    if(success_msg != null) {
      out.println("<h4 style=\"color: blue;\">" + success_msg + "</h4>");
    }

     String error_msg = (String) request.getAttribute("error_msg");
     if(error_msg != null) {
        out.println("<h4 style=\"color: red;\">" + error_msg + "</h4>");
      }
      %>

  </div>
</body>

<script type="text/javascript" src="jquery-3.4.1.js"></script>
<script type="text/javascript">
$('.login-card input[type=submit]').click(function() {
      var parent_form = $('.login-card input[type=submit]').closest('form');
      parent_form.append('<input type="hidden" name="search_key" value=\"\"/>')
    });
</script>

</html>
