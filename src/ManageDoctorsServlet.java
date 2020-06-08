import java.io.*;
import java.util.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import shared.beans.DoctorBean;
import shared.dao.DoctorDAO;
import shared.dao.PatientDAO;
import shared.beans.AppointmentBean;
import shared.beans.PatientBean;

@WebServlet("/ManageDoctors")
public class ManageDoctorsServlet extends HttpServlet {
public void doGet(HttpServletRequest request, HttpServletResponse response)  throws IOException, ServletException {

        String selected = request.getParameter("selected");

        if(selected.equals("adddoctor")) {

                DoctorBean DB = new DoctorBean();

                String fname = request.getParameter("fname");
                String lname = request.getParameter("lname");
                int age = Integer.parseInt(request.getParameter("age"));
                String specialization = request.getParameter("specialization");
                String email = request.getParameter("email");
                String password = request.getParameter("password");

                DB.setFirstName(fname);
                DB.setLastName(lname);
                DB.setAge(age);
                DB.setSpecialization(specialization);
                DB.setEmail(email);

                try {
                        DoctorDAO dob = DoctorDAO.getInstance();
                        dob.addDoctor(DB, password);
                }
                catch(SQLException e) {
                        System.out.println(e);
                }
                catch(ClassNotFoundException e) {
                        System.out.println(e);
                }

                String success_msg = "Added Doctor Successfully !";
                request.setAttribute("success_msg", success_msg);
                RequestDispatcher requestDispatcher = request.getRequestDispatcher("adddoctor.jsp");
                requestDispatcher.forward(request, response);
        }

        if(selected.equals("updatedoctor")) {
                String id = request.getParameter("did");

                try {
                        DoctorDAO dob = DoctorDAO.getInstance();
                        DoctorBean DB = dob.getDoctor(Integer.parseInt(id));

                        if(DB == null) {
                                String success_msg = "Doctor ID not found !";
                                request.setAttribute("success_msg", success_msg);
                                RequestDispatcher requestDispatcher = request.getRequestDispatcher("updatedoctor.jsp");
                                requestDispatcher.forward(request, response);
                                return;
                        }

                        String fname = request.getParameter("fname");
                        String lname = request.getParameter("lname");
                        String age = request.getParameter("age");
                        String specialization = request.getParameter("specialization");

                        if(fname.length() != 0)
                                DB.setFirstName(fname);
                        if(lname.length() != 0)
                                DB.setLastName(lname);
                        if(age.length() != 0)
                                DB.setAge(Integer.parseInt(age));
                        if(specialization.length() != 0)
                                DB.setSpecialization(specialization);

                        dob.updateDoctor(DB);
                        request.setAttribute("id", DB.getID());
                        String success_msg = "Updated Doctor Successfully !";
                        request.setAttribute("success_msg", success_msg);
                        RequestDispatcher requestDispatcher = request.getRequestDispatcher("updatedoctor.jsp");
                        requestDispatcher.forward(request, response);
                }
                catch(SQLException e) {
                        System.out.println(e);
                }
                catch(ClassNotFoundException e) {
                        System.out.println(e);
                }
        }

        if(selected.equals("removedoctors")) {

                String doctor_id = request.getParameter("id");

                try {
                        DoctorDAO dob = DoctorDAO.getInstance();
                        dob.removeDoctor(Integer.parseInt(doctor_id));

                        String success_msg = "Removed. Click Here.";
                        request.setAttribute("success_msg", success_msg);

                        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/AdminListDetails?selected=removedoctors");
                        requestDispatcher.forward(request, response);
                        return;
                }
                catch(SQLException e) {
                        System.out.println(e);
                }
                catch(ClassNotFoundException e) {
                        System.out.println(e);
                }

        }

}
}
