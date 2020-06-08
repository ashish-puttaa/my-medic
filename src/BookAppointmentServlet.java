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

@WebServlet("/BookAppointment")
public class BookAppointmentServlet extends HttpServlet {
public void doPost(HttpServletRequest request, HttpServletResponse response)  throws IOException, ServletException {

        HttpSession session = request.getSession();

        if(session.getAttribute("bean") == null) {
                response.sendRedirect("login.jsp");
                return;
        }

        try {
                PatientBean PB = (PatientBean) session.getAttribute("bean");
                int doctor_chosen_id = Integer.parseInt(request.getParameter("id"));
                String time = request.getParameter("time");

                DoctorBean DB_chosen = new DoctorBean();
                DoctorDAO dob = DoctorDAO.getInstance();
                ArrayList<DoctorBean> doctor_list = dob.getAllDoctors();
                Iterator<DoctorBean> iter = doctor_list.iterator();
                while(iter.hasNext()) {
                        DoctorBean DB = iter.next();
                        if(DB.getID() == doctor_chosen_id) {
                                DB_chosen = DB;
                                break;
                        }
                }

                AppointmentBean AB = new AppointmentBean();
                AB.setDoctor(DB_chosen);
                AB.setPatient(PB);
                AB.setAppointmentStatus(0);
                AB.setAppointmentTime(time);
                AB.setAppointmentDescription("description");

                PatientDAO pob = PatientDAO.getInstance();

                pob.bookAppointment(AB);

                String notification_message = PB.getName() + " booked an appointment at " + time + "";
                pob.setNotification(PB, DB_chosen, message);

                String success_msg = "Book Success. Click Here.";
                request.setAttribute("success_msg", success_msg);

                // response.sendRedirect("/MyApp/ListDoctors?selected=book");
                RequestDispatcher requestDispatcher = request.getRequestDispatcher("bookappointment.jsp");
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
