<!DOCTYPE html>
<html lang="en" dir="ltr">
  <head>
    <meta charset="utf-8">
      <link rel="stylesheet" href="css/registerpatient.css">
      <link href="https://fonts.googleapis.com/css?family=Open+Sans:600&display=swap" rel="stylesheet">
    <title>DocApp | Register</title>
  </head>
  <body>
    <div class="heading">
      <h1>SIGN UP</h1>
    </div>

    <div class="register-card">
      <form action="Register" method="post">
        <label for="fname">First Name</label>
        <input type="text" name="fname" placeholder="Your first name..." required>
        <label for="lname">Last Name</label>
        <input type="text" name="lname" placeholder="Your last name..." required>
        <label for="age">Age</label>
        <input type="number" name="age" placeholder="Your age..." required>
        <label for="bloodgroup">Blood Group</label>
        <select name="bloodgroup" required>
          <option value="" disabled selected>Select your Blood Group</option>
          <option value="A+">A+</option>
          <option value="O+">O+</option>
          <option value="B+">B+</option>
          <option value="AB+">AB+</option>
          <option value="A-">A-</option>
          <option value="O-">O-</option>
          <option value="B-">B-</option>
          <option value="AB-">AB-</option>
        </select>
        <label for="height">Height (cm)</label>
        <input type="number" name="height"  placeholder="Your height..." required>
        <label for="weight">Weight (kg)</label>
        <input type="number" name="weight"  placeholder="Your weight..." required>
        <label for="phone">Phone</label>
        <input type="text" name="phone"  placeholder="Your phone number..." required>
        <label for="email">Email</label>
        <input type="email" name="email" placeholder="Your email..." required>
        <label for="password">Password</label>
        <input type="password" name="password" placeholder="Set your password..." required>
        <input type="submit" name="register "value="Register">
      </form>

      <form action="login.jsp">
        <input type="submit" value="Back">
      </form>
    </div>
  </body>
</html>
