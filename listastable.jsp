<!DOCTYPE html>

<%@ page import="java.util.*" %>

<html lang="en" dir="ltr">
  <head>
    <meta charset="utf-8">

        <link href="https://fonts.googleapis.com/css?family=Open+Sans:600&display=swap" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css?family=Lato&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="css/listastable.css">



    <title>
      <%
      String msg = (String) request.getAttribute("heading");
      out.println("DocApp | " + msg);
      %>
  </title>

  <%
    String style = (String) request.getAttribute("style");

    if(style != null) {
      out.println(style);
    }
  %>


  </head>

  <%

  if((session.getAttribute("bean") == null) && !session.getAttribute("user").equals("admin")){
          response.sendRedirect("login.jsp");
          return;
  }

  %>

  <body>
    <div class="heading">
      <h2>
        <%
        out.println(msg);
        %>
      </h2>
    </div>
    <div class="table-card">

      <div class="search-box">

        <%
          String selected = request.getParameter("selected");
          String action = "AdminListDetails";
          String method = "get";
          if(selected.equals("patient_open_app")) {
            action = "DisplayAppointments";
            method = "post";
          }

          out.println("<form action=\"" + action + "\" method=\"" + method + "\">");
          out.println("<input type=\"hidden\" name=\"selected\" value=\"" + selected + "\">");

          String searchbox_first = "<input type=\"text\" name=\"search_key\" placeholder=\"Enter search text\" ";
          String searchbox_last = " required>";

          String search_key = (String) session.getAttribute("search_key");
          if(search_key != null) searchbox_first += "value = \"" + search_key + "\"";
          out.println(searchbox_first + searchbox_last);
        %>
          <%-- <input type="text" name="search_key" placeholder="Enter search text" required> --%>
          <input type="hidden" name="search_flag" value="true">
          <input type="submit" value="Search">
        </form>
        <% out.println("<form id=\"form_selected\" action=\"" + action + "\" method=\"" + method + "\">");
             out.println("<input id=\"selected\" type=\"hidden\" name=\"selected\" value=\"" + selected + "\">"); %>
             <input type="hidden" name="search_key" value="">
             <input type="submit" value="Clear">
        </form>
        <% out.println("<form action=\"" + action + "\" method=\"" + method + "\">");
             out.println("<input id=\"selected\" type=\"hidden\" name=\"selected\" value=\"" + selected + "\">"); %>
             <input type="hidden" name="download_flag" value="true">
             <input type="hidden" name="download_format" value="pdf">
             <input type="submit" value="Download as PDF">
        </form>
        <% out.println("<form action=\"" + action + "\" method=\"" + method + "\">");
             out.println("<input id=\"selected\" type=\"hidden\" name=\"selected\" value=\"" + selected + "\">"); %>
             <input type="hidden" name="download_flag" value="true">
             <input type="hidden" name="download_format" value="xls">
             <input type="submit" value="Download as XLS">
        </form>

      </div>

      <table>
        <%
        ArrayList<String[]> list = (ArrayList<String[]>) request.getAttribute("list");

        // if(list != null) {

          if(list ==  null || list.size() == 1) {
            out.println("No results avaliable");
            return;
          }

          Iterator<String[]> iter =  list.iterator();

          if(iter.hasNext()) {
            String[] str_arr = iter.next();
            out.println("<tr class=\"table-heading\">");
            out.println("<form id=\"tableheadform\" action=\"#\">");
            for(String str : str_arr) {

              String style_th = "";
              if(request.getParameter("sort_property") != null && request.getParameter("sort_property").equals(str)) {
                style_th = "style=\"color:black;\"";
              }

              out.println("<th " + style_th + ">" + str + "</th>");
            }
            out.println("</form>");
            out.println("</tr>");

            while(iter.hasNext()) {
              str_arr = iter.next();
              out.println("<tr class=\"table-record\">");
              for(String str : str_arr) {
                out.println("<td>" + str + "</td>");
              }
              out.println("</tr>");
            }
          }
        // }


        %>
    </table>

    <%
    String error_msg = (String) request.getAttribute("error_msg");
   if(error_msg != null) {
     out.println("<h4 class=\"error\">" + error_msg + "</h4>");
   }
     %>


    </div>

    <div class="back">
      <%
      String user = (String) session.getAttribute("user");

      if(user.equals("admin")) {
        out.println("<form  action=\"AdminListDetails\" method=\"get\"><input type=\"hidden\" name=\"selected\" value=\"removedoctors\"><input type=\"submit\" value=\"Back\"></form>");
      }
      if(user.equals("patient")) {

          out.println("<form  action=\"ListDoctors\" method=\"post\">");
          out.println("<input type=\"submit\" value=\"Back\"></form>");
      }

      if(user.equals("doctor")) {
        out.println("<form  action=\"" + user + "menu.jsp\" method=\"post\"><input type=\"submit\" value=\"Back\"></form>");
      }

      %>
    </div>

    <script type="text/javascript" src="jquery-3.4.1.js"></script>
    <script type="text/javascript">

    $('.back input[type=submit]').click(function() {
        var parent_form = $('.back input[type=submit]').closest('form');
        parent_form.append('<input type="hidden" name="search_key" value=\"\"/>')
      });

    $('th').click(function() {
      var flag_value = true;
      var property = $(this).html();

      var action = $('#form_selected').attr('action');
      // console.log(action);
      var method = $('#form_selected').attr('method');
      // console.log(method);
      var selected = $('#selected').val();

      $('#tableheadform').append('<input type="hidden" name="sort_flag" value="' + flag_value + '"/>');
      $('#tableheadform').append('<input type="hidden" name="sort_property" value=\"' + property + '\"/>');
      $('#tableheadform').append('<input type="hidden" name="selected" value="' + selected + '">');
      $('#tableheadform').attr('action', action);
      $('#tableheadform').attr('method', method);
      $("#tableheadform").submit();
    });

    </script>

  </body>
</html>
