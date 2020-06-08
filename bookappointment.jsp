<!DOCTYPE html>

<%@ page import="java.util.*" %>
<%@ page import="java.util.*, shared.beans.SessionBean, shared.dao.SessionDAO" %>

<html lang="en" dir="ltr">
  <head>
    <meta charset="utf-8">
    <title>DocApp | Book Appointment</title>
    <link href="https://fonts.googleapis.com/css?family=Open+Sans:600&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Lato&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="css/bookappointment.css">
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
    <div class="heading">
      <h2>Book An Appointment</h2>
    </div>

    <div class="table-card">
      <form class="" action="BookAppointment" method="post">
          <div class="datetime">
              <label for="time">Appointment Time</label>
                <input  type="datetime-local" name="time" placeholder="Your appoinment time...">
                <%
                  out.println("<input type=\"hidden\" name=\"id\" value=\"" + request.getParameter("id") + "\">");
                %>
                <input type="submit" value="Book Appointment">
            </div>

      </form>

      <div class="back">
          <form action="ListDoctors" method="post">
            <%
              String success_msg = (String) request.getAttribute("success_msg");
              if(success_msg == null) {
                success_msg = "Back";
              }

              out.println("<input type=\"submit\" value=\"" + success_msg + "\">");
            %>
          </form>
      </div>
    </div>
  </body>
</html>
