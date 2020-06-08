<!DOCTYPE html>

<%@ page import="java.util.*, shared.beans.NotificationBean, shared.dao.NotificationDAO" %>
<%@ page import="java.util.*, shared.beans.SessionBean, shared.dao.SessionDAO" %>

<html lang="en" dir="ltr">
  <head>
    <meta charset="utf-8">
    <link href="https://fonts.googleapis.com/css?family=Open+Sans:600&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Lato&display=swap" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.css" rel="stylesheet">
    <link rel="stylesheet" href="css/menu.css">
    <link rel="stylesheet" href="css/notification.css">
    <link rel="stylesheet" href="css/toast.css">

      <title>
        <%
        String uname = (String) session.getAttribute("user_id");
        NotificationDAO nob = NotificationDAO.getInstance();
        ArrayList<NotificationBean> notification_list = nob.getNotifications(uname);
        String msg = (String) request.getAttribute("heading");
        out.println("Doctor | " + msg);
        %>
    </title>

    <style media="screen">
    .table-card {
      margin: 12em calc(40vw - (800px/2));
    }
    #toast {
      left: calc(50vw - (504px/2));
    }
    </style>

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

    <div id="toast">Some text some message..</div>

    <div class="heading">
      <h2>
        <%
        out.println(msg);
        %>
      </h2>
      <form id="notification_form" action="DisplayAppointments" method="post">
        <input type="hidden" name="selected" value="doctor_manage_app">
          <input type="hidden" name="read_flag" value="true">
        <a id="notification_icon" href="#" class="button-badge">
          <i class="fa fa-bell"></i>
          <span id="badge_count" class="badge alert">0</span>
        </a>
      </form>
    </div>

    <div class="back">
      <form action="Display" method="get">
        <input type="submit" value="View Profile">
      </form>
        <form action="Logout" method="get">
          <input type="submit" value="Logout">
        </form>
    </div>


    <div class="table-card">

      <div class="search-box">

        <form id = "Change_form" style="margin-right: 15px;" action="DisplayAppointments" method="post">
           <select id="selected" name="selected">

            <%
              String[] values = {"doctor_all_app", "doctor_open_app", "doctor_manage_app"};
              String[] msg_arr = {"All Appointments", "Open Appointments", "Manage Appointments"};
              String[] text = {"All", "Open", "Manage"};

              for(int i=0; i<3; i++) {
                  String selected_str = "";
                  if(msg.equals(msg_arr[i])) {
                    selected_str = "selected";
                  }
                  out.println("<option value=\"" + values[i] + "\"" + selected_str + ">" + text[i] + "</option>");
              }
            %>

              <%-- <option value="" disabled selected>Select an option</option>
              <option value="doctor_all_app">All</option>
              <option value="doctor_open_app">Open</option>
              <option value="doctor_manage_app">Manage</option> --%>
           </select>
           <input type="submit" value="Change">
        </form>


        <form style="float: right; margin-left: 5px;" action="DisplayAppointments" method="post">
          <%
             String selected = request.getParameter("selected");
             if(selected == null) selected = (String) request.getAttribute("selected");
             out.println("<input id=\"selected\" type=\"hidden\" name=\"selected\" value=\"" + selected + "\">"); %>
             <input type="hidden" name="download_flag" value="true">
             <input type="hidden" name="download_format" value="pdf">
             <input type="submit" value="Download as PDF">
        </form>

        <form style="float: right; margin-left: 5px;" action="DisplayAppointments" method="post">
             <% out.println("<input id=\"selected\" type=\"hidden\" name=\"selected\" value=\"" + selected + "\">"); %>
             <input type="hidden" name="download_flag" value="true">
             <input type="hidden" name="download_format" value="xls">
             <input type="submit" value="Download as XLS">
        </form>

        <form style="float: right; margin-left: 5px;" action="DisplayAppointments" method="post">
          <%
            out.println("<input type=\"hidden\" name=\"selected\" value=\"" + selected + "\">");
          %>
          <input type="hidden" name="search_key" value="">
            <input type="submit" value="Clear">
        </form>

        <form style="float: right;" class="" action="DisplayAppointments" method="post">
        <%
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

      </div>

        <table>
          <%
          ArrayList<String[]> list = (ArrayList<String[]>) request.getAttribute("list");

          // if(list != null) {

            if(list == null || list.size() == 1) {
              // out.println("No results avaliable");
            }
            else {
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
                if(msg.equals("Manage Appointments"))
                    out.println("<th>Select</th>");
                out.println("</form>");
                out.println("</tr>");

                while(iter.hasNext()) {
                  str_arr = iter.next();
                  String appointment_id = str_arr[0];
                  out.println("<tr class=\"table-record\">");
                  out.println("<form method=\"post\" action=\"updatedoctor.jsp\">");
                  for(String str : str_arr) {
                    out.println("<td>" + str + "</td>");
                  }

                  boolean isNumeric =  true;

                  for(int i =0; i<appointment_id.length(); i++) {
                    if(!Character.isDigit(appointment_id.charAt(i))) {
                      isNumeric = false;
                      break;
                    }
                  }

                  if(!isNumeric) {
                    int open_last_index = appointment_id.indexOf(">");
                    int close_first_index = appointment_id.lastIndexOf("<");

                    appointment_id = appointment_id.substring(open_last_index + 1, close_first_index);
                  }


                  if(msg.equals("Manage Appointments")) {
                     out.println("<td><input type=\"hidden\" name=\"id\" value=\"" + appointment_id + "\">");
                     out.println("<button type=\"submit\" id=\"accept\" name=\"status\" value=\"1\" onclick=\"form.action='/MyApp/ManageAppointments';\">Accept</button>");
                     out.println("<button type=\"submit\" id=\"reject\" name=\"status\" value=\"2\" onclick=\"form.action='/MyApp/ManageAppointments';\">Reject</button></td>");
                  }

                  out.println("</form");

                  out.println("</tr>");
                }
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





    <script type="text/javascript" src="jquery-3.4.1.js"></script>

    <%-- <script type="text/javascript" src="js/toast.js"></script> --%>
    <%-- <script type="text/javascript" src="js/push.js"></script> --%>

    <script type="text/javascript">
    <%
      out.println("var count = " + notification_list.size() + ";");
      // out.println("var pushSocket = new WebSocket(\"ws://localhost:8080/MyApp/push/" +  uname + "\")");
      out.println("var pushSocket = new WebSocket(\"ws://172.22.153.170:8080/MyApp/push/" +  uname + "\")");
    %>

    var push_heading = "New Appointment";
    // var push_image_url = "https://images.vexels.com/media/users/3/144185/isolated/preview/a9075b02366ea61e8995f8d5b08d0267-flat-doctor-cartoon-by-vexels.png";
    var push_image_url = "img/man.png";
    </script>

    <script type="text/javascript" src="js/notification.js"></script>



    <script type="text/javascript">
      $('.back input[type=submit]').click(function() {
          pushSocket.close();
          var parent_form = $('.back input[type=submit]').closest('form');
          parent_form.append('<input type="hidden" name="search_key" value=\"\"/>');
      });

      $('#Change_form input[type=submit]').click(function() {
          var parent_form = $('#Change_form input[type=submit]').closest('form');
          parent_form.append('<input type="hidden" name="search_key" value=\"\"/>')
      });

      $('th').click(function() {
          var flag_value = true;
          var property = $(this).html();
          console.log(property);

          var selected = $('#selected').val();
          console.log(selected);

          $('#tableheadform').append('<input type="hidden" name="sort_flag" value="' + flag_value + '"/>');
          $('#tableheadform').append('<input type="hidden" name="sort_property" value=\"' + property + '\"/>');
          $('#tableheadform').append('<input type="hidden" name="selected" value="' + selected + '">');
          $('#tableheadform').attr('action', 'DisplayAppointments');
          $('#tableheadform').attr('method', 'post');
          $("#tableheadform").submit();
          console.log("done");
      });
    </script>

  </body>
</html>
