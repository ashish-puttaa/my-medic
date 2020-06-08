<!DOCTYPE html>

<%@ page import="java.util.*, shared.beans.PatientBean, shared.beans.NotificationBean, shared.dao.NotificationDAO" %>
<%@ page import="java.util.*, shared.beans.SessionBean, shared.dao.SessionDAO" %>

<html lang="en" dir="ltr">
  <head>
    <meta charset="utf-8">
    <title>Patient | Book Appointment</title>
    <link href="https://fonts.googleapis.com/css?family=Open+Sans:600&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Lato&display=swap" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.css" rel="stylesheet">
    <link rel="stylesheet" href="css/menu.css">
    <link rel="stylesheet" href="css/notification.css">
    <link rel="stylesheet" href="css/toast.css">

      <%
        String style_h = (String) request.getAttribute("style");

        if(style_h != null) {
          out.println(style_h);
        }
      %>

      <style> .back {
                /* left: 75vw;  */
                left: calc((40vw + (610px/2)) + ((100vw - (40vw + (610px/2)))/2 - (200px/2)));
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



<%
  if(session.getAttribute("bean") == null) {
          response.sendRedirect("login.jsp");
          return;
  }

    String uname = (String) session.getAttribute("user_id");
    NotificationDAO nob = NotificationDAO.getInstance();
    ArrayList<NotificationBean> notification_list = nob.getNotifications(uname);
  %>

  <body>

    <div id="toast">Some text some message..</div>

    <div class="heading">
      <h2>Book An Appointment</h2>
      <form id="notification_form" action="DisplayAppointments" method="post">
        <input type="hidden" name="selected" value="patient_open_app">
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
        <form action="DisplayAppointments" method="post">
          <input type="hidden" name="selected" value="patient_open_app">
          <input type="submit" value="View Status">
        </form>
        <%-- <form action="push.jsp" method="get">
          <%
          PatientBean PB = (PatientBean) session.getAttribute("bean");
          String id = String.valueOf(PB.getID());
          out.println("<input type=\"hidden\" name=\"user_id\" value=\"P" + id + "\">");
          %>
          <input type="submit" value="Push">
        </form> --%>
        <form action="Logout" method="get">
          <input type="submit" value="Logout">
        </form>
    </div>



    <div class="table-card">


      <div class="search-box">
        <form action="ListDoctors" method="post">
          <%
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
        <form action="ListDoctors" method="post">
          <input type="hidden" name="search_key" value="">
            <input type="submit" value="Clear">
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
              out.println("</form");
              out.println("</tr>");

              while(iter.hasNext()) {
                str_arr = iter.next();
                String doctor_id = str_arr[0];
                out.println("<tr class=\"table-record\">");
                out.println("<form method=\"get\" action=\"#\">");
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
                out.println("<button type=\"submit\" name=\"book\" onclick=\"form.action='bookappointment.jsp';\">Book</button></td>");

                out.println("</tr>  </form>");
              }
            }
          // }
          %>
      </table>
    </div>






    <%-- <script src="https://unpkg.com/ionicons@4.5.10-0/dist/ionicons.js"></script> --%>
    <script type="text/javascript" src="jquery-3.4.1.js"></script>

    <%-- <script type="text/javascript" src="js/toast.js"></script> --%>
    <%-- <script type="text/javascript" src="js/push.js"></script> --%>

    <script type="text/javascript">
        <%
            out.println("var count = " + notification_list.size() + ";");
            // out.println("var pushSocket = new WebSocket(\"ws://localhost:8080/MyApp/push/" +  uname + "\")");
            out.println("var pushSocket = new WebSocket(\"ws://172.22.153.170:8080/MyApp/push/" +  uname + "\")");
        %>

        var push_heading = "Appointment Update";
        // var push_image_url = "https://image.flaticon.com/icons/svg/145/145848.svg";
        // var push_image_url = "img/man.png";
        var push_image_url = "https://images.vexels.com/media/users/3/144185/isolated/preview/a9075b02366ea61e8995f8d5b08d0267-flat-doctor-cartoon-by-vexels.png";

    </script>

    <script type="text/javascript" src="js/notification.js"></script>

    <script type="text/javascript">
        $('.back input[type=submit]').click(function() {
            pushSocket.close();
            var parent_form = $('.back input[type=submit]').closest('form');
            parent_form.append('<input type="hidden" name="search_key" value=\"\"/>')
        });

        $('th').click(function() {
            var flag_value = true;
            var property = $(this).html();
            console.log(property);

            $('#tableheadform').append('<input type="hidden" name="sort_flag" value="' + flag_value + '"/>');
            $('#tableheadform').append('<input type="hidden" name="sort_property" value=\"' + property + '\"/>');
            $('#tableheadform').attr('action', 'ListDoctors');
            $('#tableheadform').attr('method', 'post');
            $("#tableheadform").submit();
        });
    </script>

  </body>
</html>
