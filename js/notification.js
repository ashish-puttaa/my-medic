pushSocket.onmessage = function(event) {

  if (event.data.localeCompare("") != 0) {
    console.log(event.data);

    if (!'Notification' in window)
      alert("Your browser does not support Notifications");
    else {
      if (Notification.permission === 'denied') {
        toastFunction();
      }

      if (Notification.permission !== 'granted') {
        document.getElementById("toast").innerHTML = event.data;

        Notification.requestPermission(function(permission) {
          Notification.permission = permission;
        });
      }

      var notification = new Notification("Appointment Update", {
        body: event.data,
        icon: push_image_url
      });

      notification.onclick = function(event) {
        window.open("DisplayAppointments");
      }

    }

  } else {
    console.log("Connected");
  }

  document.getElementById("badge_count").innerHTML = count;
  count++;
}

pushSocket.onopen = function(event) {
  pushSocket.send("");
};

pushSocket.onclose = function(event) {
  console.log("Socket Closed by Server");
};




$(document).ready(function() {
  $("#notification_icon").click(function() {
    pushSocket.close();
    $("#notification_form").submit();
  });
});


$('button').click(function() {
  pushSocket.close();
});

$('input[type=submit]').click(function() {
  pushSocket.close();
});





function toastFunction() {
  var x = document.getElementById("toast");

  x.className = "show";

  setTimeout(function() {
    x.className = x.className.replace("show", "");
  }, 3000);
}