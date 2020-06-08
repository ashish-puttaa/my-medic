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

@WebServlet("/ManageAppointments")
public class ManageAppointmentsServlet extends HttpServlet {
public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();

        if(session.getAttribute("bean") == null) {
                response.sendRedirect("login.jsp");
                return;
        }

        String user = (String) session.getAttribute("user");

        try {
                if(user.equals("doctor")) {
                        DoctorDAO dob = DoctorDAO.getInstance();
                        DoctorBean DB = (DoctorBean) session.getAttribute("bean");
                        ArrayList<AppointmentBean> appointment_list = dob.getAppointments(DB);

                        Iterator<AppointmentBean> iter = appointment_list.iterator();

                        int appointment_id = Integer.parseInt(request.getParameter("id"));

                        int status = Integer.parseInt(request.getParameter("status"));

                        AppointmentBean AB = new AppointmentBean();
                        AB.setID(appointment_id);
                        AB.setAppointmentStatus(status);

                        dob.changeAppointmentStatus(AB);

                        String notification_message = "Appointment with " + DB.getName() + " at " + "";

                        request.setAttribute("selected", "doctor_manage_app");
                        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/DisplayAppointments?selected=doctor_manage_app");
                        requestDispatcher.forward(request, response);

                        return;
                }
        }

        catch(SQLException e) {
                System.out.println(e);
        }
        catch(ClassNotFoundException e) {
                System.out.println(e);
        }

}
}
