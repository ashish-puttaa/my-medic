import java.io.*;
import java.util.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import shared.beans.PatientBean;
import shared.dao.PatientDAO;
import shared.beans.DoctorBean;
import shared.dao.DoctorDAO;
import shared.beans.SessionBean;
import shared.dao.SessionDAO;
import javax.servlet.annotation.WebServlet;

@WebServlet("/Login")
public class LoginServlet extends HttpServlet {
public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String user = request.getParameter("user");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        PrintWriter out = response.getWriter();

        if(user.equals("patient")) {
                PatientDAO ob = null;
                try {
                        ob = PatientDAO.getInstance();
                        PatientBean PB = ob.login(email, password);
                        if(PB == null) {
                                String error_msg = "Invalid Patient Credentials ! Try Again.";
                                request.setAttribute("error_msg", error_msg);
                                RequestDispatcher requestDispatcher = request.getRequestDispatcher("login.jsp");
                                requestDispatcher.forward(request, response);
                        }
                        else {
                                HttpSession session = request.getSession();
                                session.setMaxInactiveInterval(300);

                                // --------- STORING SESSION IN DB ----------------

                                String session_id = session.getId();
                                long created_date = session.getCreationTime();
                                String user_type = "Patient";
                                int user_id = PB.getID();

                                SessionBean SB = new SessionBean();
                                SB.setID(session_id);
                                SB.setUserID(user_id);
                                SB.setUserType(user_type);
                                SB.setCreatedDate(created_date);

                                SessionDAO sob = SessionDAO.getInstance();
                                sob.addSession(SB);

                                // ------------------------------------------------

                                session.setAttribute("bean", PB);
                                session.setAttribute("user", user);
                                session.setAttribute("user_id", "P" + String.valueOf(PB.getID()));
                                // System.out.println("Session ID : " + session.getId());
                                // request.setAttribute("bean", PB);
                                RequestDispatcher requestDispatcher = request.getRequestDispatcher("ListDoctors");
                                requestDispatcher.forward(request, response);

                        }
                }
                catch(SQLException e) {
                        System.out.println(e);
                }
                catch(ClassNotFoundException e) {
                        System.out.println(e);
                }
        }

        if(user.equals("doctor")) {
                DoctorDAO ob = null;
                try {
                        ob = DoctorDAO.getInstance();
                        DoctorBean DB = ob.login(email, password);
                        if(DB == null) {
                                String error_msg = "Invalid Doctor Credentials ! Try Again.";
                                request.setAttribute("error_msg", error_msg);
                                RequestDispatcher requestDispatcher = request.getRequestDispatcher("login.jsp");
                                requestDispatcher.forward(request, response);
                        }
                        else {
                                HttpSession session = request.getSession();


                                // --------- STORING SESSION IN DB ----------------

                                String session_id = session.getId();
                                long created_date = session.getCreationTime();
                                String user_type = "Doctor";
                                int user_id = DB.getID();

                                SessionBean SB = new SessionBean();
                                SB.setID(session_id);
                                SB.setUserID(user_id);
                                SB.setUserType(user_type);
                                SB.setCreatedDate(created_date);

                                SessionDAO sob = SessionDAO.getInstance();
                                sob.addSession(SB);

                                // ------------------------------------------------

                                session.setAttribute("bean", DB);
                                session.setAttribute("user", user);
                                session.setAttribute("user_id", "D" + String.valueOf(DB.getID()));
                                // System.out.println("Session ID : " + session.getId());
                                // request.setAttribute("bean", PB);
                                request.setAttribute("selected", "doctor_manage_app");
                                // RequestDispatcher requestDispatcher = request.getRequestDispatcher("doctormenu.jsp");
                                RequestDispatcher requestDispatcher = request.getRequestDispatcher("DisplayAppointments");
                                requestDispatcher.forward(request, response);
                        }
                }
                catch(SQLException e) {
                        System.out.println(e);
                }
                catch(ClassNotFoundException e) {
                        System.out.println(e);
                }
        }
        out.close();
}
}
