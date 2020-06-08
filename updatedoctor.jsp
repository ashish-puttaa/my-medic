<!DOCTYPE html>

<%@ page import="shared.dao.DoctorDAO, shared.beans.DoctorBean" %>

<html lang="en" dir="ltr">
  <head>
    <meta charset="utf-8">
      <link rel="stylesheet" href="css/registerpatient.css">
      <link href="https://fonts.googleapis.com/css?family=Open+Sans:600&display=swap" rel="stylesheet">
    <title>DocApp | Register</title>
  </head>
  <body>
    <div class="heading">
      <h1>UPDATE DOCTOR</h1>
    </div>

    <div class="register-card">

      <form action="ManageDoctors" method="get">

        <%
            Object id_obj = request.getParameter("id");
            int id;

            if(id_obj == null) {
              id = (int)request.getAttribute("id");
            }
            else {
              id = Integer.parseInt(request.getParameter("id"));
            }

            DoctorDAO ob = DoctorDAO.getInstance();
            DoctorBean DB = ob.getDoctor(id);

            out.println("<label for=\"did\">ID</label>");
            out.println("<input type=\"number\" name=\"did\" placeholder=\"Doctor's ID...\" value = \"" + DB.getID() + "\" readonly>");
            out.println("<label for=\"fname\">First Name</label>");
            out.println("<input type=\"text\" name=\"fname\" placeholder=\"Doctor's new first name...\" value=\"" + DB.getFirstName() + "\" >");
            out.println("<label for=\"lname\">Last Name</label>");
            out.println("<input type=\"text\" name=\"lname\" placeholder=\"Doctor's new last name...\" value=\"" + DB.getLastName() + "\">");
            out.println("<label for=\"age\">Age</label>");
            out.println("<input type=\"number\" name=\"age\" placeholder=\"Doctor's new age...\" value=\"" + DB.getAge() + "\" >");
            out.println("<label for=\"specialization\">Specialization</label>");
            out.println("<input type=\"text\" name=\"specialization\"  placeholder=\"Doctor's new specialization...\" value=\"" + DB.getSpecialization() + "\">");

        %>

        <input type="submit" name="update "value="Update Doctor" />
        <input type='hidden' name='selected' value="updatedoctor" />

      </form>

      <form action="AdminListDetails" action="get">
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
</html>
