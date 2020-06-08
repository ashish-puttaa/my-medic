<!DOCTYPE html>

<%@ page import="java.util.*" %>
<%@ page import="java.util.*, shared.beans.SessionBean, shared.dao.SessionDAO" %>

<html lang="en" dir="ltr">
  <head>
    <meta charset="utf-8">
      <link rel="stylesheet" href="css/displayprofile.css">
      <link href="https://fonts.googleapis.com/css?family=Lato&display=swap" rel="stylesheet">
      <link href="https://fonts.googleapis.com/css?family=Open+Sans:600&display=swap" rel="stylesheet">
    <title>DocApp | Profile</title>
  </head>

  <%
      SessionDAO sob = SessionDAO.getInstance();
      HttpSession current_session = request.getSession();

      if(!sob.isSessionActive(current_session.getId())) {
        response.sendRedirect("login.jsp");
        return;
      }
  %>

  <body>
    <p class="profile-heading">PROFILE</p>
    <div class="profile-card">
      <div class="profile-contents">
        <table>
          <%
            ArrayList<String[]> list = (ArrayList<String[]>) request.getAttribute("list");

            Iterator<String[]> iter = list.iterator();

            while(iter.hasNext()) {
              String[] str_arr = iter.next();
              out.println("<tr><td>" + str_arr[0] + "</td><td> : </td><td>" + str_arr[1] + "</td></tr>");
            }
          %>
        </table>
      </div>

      <%
        String user = (String) session.getAttribute("user");

        if(user.equals("patient")) {

            out.println("<form  action=\"ListDoctors\" method=\"post\">");
            out.println("<input type=\"submit\" value=\"Back\"></form>");
          }

          if(user.equals("doctor")) {
            out.println("<form  action=\"DisplayAppointments\" method=\"post\">");
            out.println("<input type=\"hidden\" name=\"selected\" value=\"doctor_manage_app\">");
            out.println("<input type=\"submit\" value=\"Back\"></form>");
          }
      %>

      <form action="updatepassword.jsp" method="post">
        <input type="submit" value="Update Password">
      </form>

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
