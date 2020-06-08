import java.io.*;
import java.util.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import shared.dao.PatientDAO;
import shared.beans.PatientBean;

@WebServlet("/Register")
public class RegisterPatientServlet extends HttpServlet {
public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        PrintWriter out = response.getWriter();
        String fname = request.getParameter("fname");
        String lname = request.getParameter("lname");
        int age = Integer.parseInt(request.getParameter("age"));
        String bloodgroup = request.getParameter("bloodgroup");
        int height = Integer.parseInt(request.getParameter("height"));
        int weight = Integer.parseInt(request.getParameter("weight"));
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        PatientBean PB = new PatientBean();
        PB.setFirstName(fname);
        PB.setLastName(lname);
        PB.setAge(age);
        PB.setBloodGroup(bloodgroup);
        PB.setHeight(height);
        PB.setWeight(weight);
        PB.setPhone(phone);
        PB.setEmail(email);

        try {
                PatientDAO ob = PatientDAO.getInstance();
                ob.registerPatient(PB, password);
        }
        catch(SQLException e) {
                System.out.println(e);
        }
        catch(ClassNotFoundException e) {
                System.out.println(e);
        }

        out.println("Registration Successful");

        String success_msg = "Registration Successful ! Please Login to continue.";
        request.setAttribute("success_msg", success_msg);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("login.jsp");
        requestDispatcher.forward(request, response);
}
}
