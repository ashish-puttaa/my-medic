import java.io.*;
import java.util.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import shared.dao.DoctorDAO;
import shared.dao.PatientDAO;
import shared.beans.DoctorBean;
import shared.beans.PatientBean;

@WebServlet("/UpdatePassword")
public class UpdatePasswordServlet extends HttpServlet {
public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        HttpSession session = request.getSession();
        if(session.getAttribute("bean") == null) {
                response.sendRedirect("login.jsp");
                return;
        }

        String user = (String) session.getAttribute("user");

        try {

                PatientBean PB = null;
                DoctorBean DB = null;
                String email = "";
                boolean flag = false;
                String old_password = request.getParameter("oldpass");
                String new_password = request.getParameter("newpass");

                if(user.equals("patient")) {

                        PB = (PatientBean) session.getAttribute("bean");
                        PatientDAO ob = PatientDAO.getInstance();
                        email = PB.getEmail();

                        if(ob.login(email, old_password) == null) {
                                flag = true;
                        }
                        else {
                                ob.updatePassword(email, new_password);
                        }
                }

                if(user.equals("doctor")) {
                        DB = (DoctorBean) session.getAttribute("bean");
                        DoctorDAO ob = DoctorDAO.getInstance();
                        email = DB.getEmail();

                        if(ob.login(email, old_password) == null) {
                                flag = true;
                        }
                        else {
                                ob.updatePassword(email, new_password);
                        }
                }



                if(flag == true) {
                        String error_msg = "Your old password does not match ! Try Again";
                        request.setAttribute("error_msg", error_msg);
                        RequestDispatcher requestDispatcher = request.getRequestDispatcher("updatepassword.jsp");
                        requestDispatcher.forward(request, response);
                        return;
                }


                String success_msg = "Password Changed !";
                request.setAttribute("success_msg", success_msg);
                RequestDispatcher requestDispatcher = request.getRequestDispatcher("updatepassword.jsp");
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
