<!DOCTYPE html>

<%@ page import="java.util.*, java.lang.*" %>

<html lang="en" dir="ltr">
  <head>
    <meta charset="utf-8">
    <link href="https://fonts.googleapis.com/css?family=Open+Sans:600&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Lato&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="css/menu.css">

      <title>
        <%
            String msg = (String) request.getAttribute("heading");
            out.println("Admin | " + msg);
        %>
      </title>

      <style> .table-card { margin: 8em 0 0 1em; } </style>

  </head>

  <% session.setAttribute("user", "admin"); %>

  <body>
    <div class="heading">
      <h2>
        <%
        out.println(msg);
        %>
      </h2>
    </div>

    <div class="back">
      <form action="adddoctor.jsp" method="get">
        <input type="submit" value="Add Doctor">
      </form>
        <form action="AdminListDetails" method="get">
          <input type="hidden" name="selected" value="viewpatients">
          <input type="submit" value="View Patients">
        </form>
        <form action="AdminListDetails" method="get">
          <input type="hidden" name="selected" value="viewappointments">
          <input type="submit" value="View Appointments">
        </form>
        <form action="login.jsp" method="get">
          <input type="submit" value="Back">
        </form>
    </div>


    <div class="table-card">

      <div class="search-box">
        <form action="AdminListDetails" method="get">
        <%
          String selected = request.getParameter("selected");
          out.println("<input type=\"hidden\" name=\"selected\" value=\"" + selected + "\">");

          String searchbox_first = "<input type=\"text\" name=\"search_key\" placeholder=\"Enter search text\" ";
          String searchbox_last = " required>";

          String search_key = (String) session.getAttribute("search_key");
          if(search_key != null) searchbox_first += "value = \"" + search_key + "\"";
          out.println(searchbox_first + searchbox_last);
          %>
          <input type="hidden" name="search_flag" value="true">
          <input type="submit" value="Search">
        </form>
        <form action="AdminListDetails" method="get">
          <% out.println("<input type=\"hidden\" name=\"selected\" value=\"" + selected + "\">"); %>
          <input type="hidden" name="search_key" value="">
            <input type="submit" value="Clear">
        </form>

        <form action="AdminListDetails" method="get">
             <% out.println("<input id=\"selected\" type=\"hidden\" name=\"selected\" value=\"" + selected + "\">"); %>
             <input type="hidden" name="download_flag" value="true">
             <input type="hidden" name="download_format" value="pdf">
             <input type="submit" value="Download as PDF">
        </form>

        <form action="AdminListDetails" method="get">
             <% out.println("<input id=\"selected\" type=\"hidden\" name=\"selected\" value=\"" + selected + "\">"); %>
             <input type="hidden" name="download_flag" value="true">
             <input type="hidden" name="download_format" value="xls">
             <input type="submit" value="Download as XLS">
        </form>
      </div>


        <table>
          <%
          ArrayList<String[]> list = (ArrayList<String[]>) request.getAttribute("list");

          // if(list != null) {
            if(list == null || list.size() == 1) {
              out.println("No results avaliable");
              return;
            }

            Iterator<String[]> iter =  list.iterator();

            if(iter.hasNext()) {
              String[] str_arr = iter.next();

              out.println("<tr class=\"table-heading\">");
              out.println("<form id=\"tableheadform\" action=\"#\">");
              for(String str : str_arr) {

                String style = "";
                if(request.getParameter("sort_property") != null && request.getParameter("sort_property").equals(str)) {
                  style = "style=\"color:black;\"";
                }

                out.println("<th " + style + ">" + str + "</th>");
              }
              out.println("<th>Select</th>");
              out.println("</form>");
              out.println("</tr>");


              while(iter.hasNext()) {
                str_arr = iter.next();
                String doctor_id = str_arr[0];
                out.println("<tr class=\"table-record\">");
                out.println("<form method=\"get\" action=\"updatedoctor.jsp\">");
                for(String str : str_arr) {
                  out.println("<td>" + str + "</td>");
                }

                boolean isNumeric =  true;

                for(int i =0; i<doctor_id.length(); i++) {
                  if(!Character.isDigit(doctor_id.charAt(i))) {
                    isNumeric = false;
                    break;
                  }
                }

                if(!isNumeric) {
                  int open_last_index = doctor_id.indexOf(">");
                  int close_first_index = doctor_id.lastIndexOf("<");

                  doctor_id = doctor_id.substring(open_last_index + 1, close_first_index);
                }

                out.println("<td><input type=\"hidden\" name=\"id\" value=\"" + doctor_id + "\">");
                out.println("<input type=\"hidden\" name=\"selected\" value=\"removedoctors\">");
                out.println("<button type=\"submit\" name=\"remove\" onclick=\"form.action='/MyApp/ManageDoctors';\">Remove</button>");
                out.println("<button type=\"submit\" name=\"update\">Update</button></td>");

                out.println("</form");

                out.println("</tr>");
              }
            }
          // }
          %>
      </table>

    </div>

    <script type="text/javascript" src="jquery-3.4.1.js"></script>
    <script type="text/javascript">

    $('.back input[type=submit]').click(function() {
        var parent_form = $('.back input[type=submit]').closest('form');
        parent_form.append('<input type="hidden" name="search_key" value=\"\"/>');
      });

      $('th').click(function() {
        var flag_value = true;
        var property = $(this).html();

        $('#tableheadform').append('<input type="hidden" name="sort_flag" value="' + flag_value + '"/>');
        $('#tableheadform').append('<input type="hidden" name="sort_property" value=\"' + property + '\"/>');
        $('#tableheadform').append('<input type="hidden" name="selected" value="removedoctors">');
        $('#tableheadform').attr('action', 'AdminListDetails');
        $("#tableheadform").submit();
      });
    </script>

  </body>
</html>
