function toastFunction() {
  // Get the toast DIV
  var x = document.getElementById("toast");

  // Add the "show" class to DIV
  x.className = "show";

  // After 3 seconds, remove the show class from DIV
  setTimeout(function() {
    x.className = x.className.replace("show", "");
  }, 3000);
}