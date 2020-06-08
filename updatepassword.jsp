<!DOCTYPE html>
<%@ page import="java.util.*, shared.beans.SessionBean, shared.dao.SessionDAO" %>
<html lang="en" dir="ltr">
  <head>
    <meta charset="utf-8">
    <title>DocApp | Settings</title>
    <link rel="stylesheet" href="css/updatepassword.css">
    <link href="https://fonts.googleapis.com/css?family=Lato&display=swap" rel="stylesheet">
  </head>

  <%
      SessionDAO sob = SessionDAO.getInstance();
      HttpSession current_session = request.getSession();

      if(!sob.isSessionActive(current_session.getId())) {
        response.sendRedirect("login.jsp");
        return;
      }
  %>

  <%

  if(session.getAttribute("bean") == null) {
          response.sendRedirect("login.jsp");
          return;
  }

  %>

  <body>
    <div class="container">
      <div class="heading">
        <h1>Update Password</h1>
      </div>

      <div class="form">
        <form action="UpdatePassword" method="post">
          <label for="oldpass">Old Password</label>
          <input type="password" name="oldpass" placeholder="Your old password..." required>
          <label for="newpass">New Password</label>
          <input type="password" name="newpass" placeholder="Your new password..." required>
          <input type="submit" value="Update">
        </form>

        <form action="Display" method="get">
          <input type="submit" value="Back">
        </form>


      </div>


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
</html>
