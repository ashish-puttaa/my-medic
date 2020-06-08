<!DOCTYPE html>

<%@ page import="java.util.*, shared.beans.NotificationBean, shared.dao.NotificationDAO" %>

<html lang="en" dir="ltr">

<head>
  <meta charset="utf-8">
  <title></title>
  <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.css" rel="stylesheet">
    <link rel="stylesheet" href="css/menu.css">
  <style media="screen">
        .button-badge {
          background-color: #1779ba;
          text-decoration: none;
          padding: .7rem 1rem;
          position: relative;
          display: inline-block;
          border-radius: 25%;
          transition: all ease 0.4s;
        }

        .button-badge:hover {
          box-shadow: 0 1px 5px rgba(0, 0, 0, 0.1);
        }

        .badge {
          position: absolute;
          top: -7px;
          right: -7px;
          font-size: .8em;
          background-color: red;
          color: white;
          border-radius: 50%;
          width: 20px;
          height: 20px;
          text-align: center;
        }

        .fa {
          color: white;
          font-size: 20px;
        }


  </style>


  <style>

          /* The snackbar - position it at the bottom and in the middle of the screen */
        #snackbar {
        visibility: hidden; /* Hidden by default. Visible on click */
        min-width: 250px; /* Set a default minimum width */
        margin-left: -125px; /* Divide value of min-width by 2 */
        background-color: #333; /* Black background color */
        color: #fff; /* White text color */
        text-align: center; /* Centered text */
        border-radius: 2px; /* Rounded borders */
        padding: 16px; /* Padding */
        position: fixed; /* Sit on top of the screen */
        z-index: 1; /* Add a z-index if needed */
        left: 50%; /* Center the snackbar */
        bottom: 30px; /* 30px from the bottom */
        }

        /* Show the snackbar when clicking on a button (class added with JavaScript) */
        #snackbar.show {
        visibility: visible; /* Show the snackbar */
        /* Add animation: Take 0.5 seconds to fade in and out the snackbar.
        However, delay the fade out process for 2.5 seconds */
        -webkit-animation: fadein 0.5s, fadeout 0.5s 2.5s;
        animation: fadein 0.5s, fadeout 0.5s 2.5s;
        }

        /* Animations to fade the snackbar in and out */
        @-webkit-keyframes fadein {
        from {bottom: 0; opacity: 0;}
        to {bottom: 30px; opacity: 1;}
        }

        @keyframes fadein {
        from {bottom: 0; opacity: 0;}
        to {bottom: 30px; opacity: 1;}
        }

        @-webkit-keyframes fadeout {
        from {bottom: 30px; opacity: 1;}
        to {bottom: 0; opacity: 0;}
        }

        @keyframes fadeout {
        from {bottom: 30px; opacity: 1;}
        to {bottom: 0; opacity: 0;}
        }

  </style>

</head>


<%
  String uname = (String) session.getAttribute("user-id");
  NotificationDAO nob = NotificationDAO.getInstance();
  ArrayList<NotificationBean> list = nob.getNotifications(uname);
%>


<body>

  <%-- <button onclick="myFunction()">Show Snackbar</button> --%>
  <div id="snackbar">Some text some message..</div>

  <div class="heading">
    <%-- <p id="para"></p> --%>
    <a id="notification_icon" href="#" class="button-badge">
      <i class="fa fa-bell"></i>
      <span id="badge_count" class="badge alert">0</span>
    </a>
  </div>


  <script type="text/javascript">
      function myFunction() {
        // Get the snackbar DIV
        var x = document.getElementById("snackbar");

        // Add the "show" class to DIV
        x.className = "show";

        // After 3 seconds, remove the show class from DIV
        setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
      }
  </script>


  <script type="text/javascript">
    // var notifications = [];

  <%
    out.println("var count = " + list.size() + ";");
    out.println("var pushSocket = new WebSocket(\"ws://localhost:8080/MyApp/push/" +  uname + "\")"); %>

    pushSocket.onmessage = function(event) {
      // document.getElementById("para").innerHTML = event.data;
      // document.write(event.data + "<br>");

      document.getElementById("snackbar").innerHTML = event.data;


      document.getElementById("badge_count").innerHTML = count;
      count++;
      myFunction();

      console.log(event.data);
      //do ui update here
    }

    pushSocket.onopen = function(event) {
      //sending empty message to initialize socket connnection
      pushSocket.send("");
    };

    pushSocket.onclose = function(event) {
      //send empty message to initialize socket connnection
      console.log("Socket Closed by Server");
    };
  </script>



</body>

</html>
