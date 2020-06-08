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
import shared.utilities.ListSorter;

@WebServlet("/DisplayAppointments")
public class DisplayAppointmentsServlet extends HttpServlet {

enum Options {
        OPEN(new String[] {"ID", "Patient Name", "Patient Age", "Time"}, "No more Open Appointments.", "Open Appointments"),
        ALL(new String[] {"ID", "Patient Name", "Patient Age", "Time", "Status"}, "No Appointments yet.", "All Appointments"),
        MANAGE(new String[] {"ID", "Patient Name", "Patient Age", "Time"}, "No more Pending Appointments.", "Manage Appointments");

        private final String[] str_arr;
        private final String error_msg;
        private final String heading;

        private Options(String[] str_arr, String error_msg, String heading) {
                this.str_arr = str_arr;
                this.error_msg = error_msg;
                this.heading = heading;
        }

        private String[] getStringArray() {
                return this.str_arr;
        }

        private String getErrorMessage() {
                return this.error_msg;
        }

        private String getHeading() {
                return this.heading;
        }
}

public <T> ArrayList<T> sort(HttpServletRequest request, HttpServletResponse response, ArrayList<T> list) {
        HttpSession session = request.getSession();
        if(session.getAttribute("sort_order") == null) {
                session.setAttribute("sort_order", "asc");
        }

        String property = request.getParameter("sort_property");;
        ListSorter listSorter = ListSorter.getInstance();

        if(property.split(" ").length == 2) {
                String[] properties = property.split(" ");
                list = listSorter.sort(list, properties);
        }
        else {
                property = property.replaceAll(" ", "");
                list = listSorter.sort(list, new String[] {property});
        }

        if(session.getAttribute("sort_order").toString().equals("desc")) {
                Collections.reverse(list);
                session.setAttribute("sort_order", "asc");
        }
        else {
                session.setAttribute("sort_order", "desc");
        }
        return list;
}

public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        HttpSession session = request.getSession();

        if(session.getAttribute("bean") == null) {
                response.sendRedirect("login.jsp");
                return;
        }

        String user = (String) session.getAttribute("user");
        String search_flag = request.getParameter("search_flag");
        String sort_flag = request.getParameter("sort_flag");
        String selected = request.getParameter("selected");

        if(selected == null) {
                selected = (String) request.getAttribute("selected");
        }

        try {
                if(user.equals("doctor")) {
                        Options option = null;

                        if(selected.equals("doctor_open_app")) {
                                option = Options.OPEN;
                        }
                        if(selected.equals("doctor_all_app")) {
                                option = Options.ALL;
                        }

                        if(selected.equals("doctor_manage_app")) {
                                option = Options.MANAGE;
                        }
                        DoctorDAO dob = DoctorDAO.getInstance();
                        DoctorBean DB = (DoctorBean) session.getAttribute("bean");
                        ArrayList<AppointmentBean> appointment_list = dob.getAppointments(DB);

                        if(sort_flag != null && sort_flag.equals("true")) {
                                appointment_list = sort(request, response, appointment_list);
                        }

                        Iterator<AppointmentBean> iter = appointment_list.iterator();

                        ArrayList<String[]> list = new ArrayList<>();

                        String[] s_arr = option.getStringArray();

                        list.add(s_arr);

                        while(iter.hasNext()) {

                                AppointmentBean AB = iter.next();
                                String id = String.valueOf(AB.getID());
                                String name = AB.getPatient().getName();
                                String age = String.valueOf(AB.getPatient().getAge());
                                String time = AB.getAppointmentTime();
                                int status_int = AB.getAppointmentStatus();
                                String status_str = "Empty";

                                if(status_int == 0) {
                                        status_str = "Pending";
                                }
                                if(status_int == 1) {
                                        status_str = "Accepted";
                                }
                                if(status_int == 2) {
                                        status_str = "Rejected";
                                }

                                if(selected.equals("doctor_open_app")) {
                                        if(AB.getAppointmentStatus() == 1) {
                                                String[] str_arr = new String[] {id, name, age, time};
                                                list.add(str_arr);
                                        }
                                }

                                if(selected.equals("doctor_all_app")) {
                                        String[] str_arr = new String[] {id, name, age, time, status_str};
                                        list.add(str_arr);
                                }

                                if(selected.equals("doctor_manage_app")) {
                                        if(AB.getAppointmentStatus() == 0) {
                                                String[] str_arr = new String[] {id, name, age, time};
                                                list.add(str_arr);
                                        }
                                }
                        }

                        request.setAttribute("heading", option.getHeading());
                        request.setAttribute("list", list);

                        if(search_flag != null && search_flag.equals("true")) {
                                request.setAttribute("redirect_url", "doctormenu.jsp");
                                RequestDispatcher requestDispatcher = request.getRequestDispatcher("/Search");
                                requestDispatcher.forward(request, response);
                                return;
                        }

                        if(list.size() == 1) {
                                String error_msg = option.getErrorMessage();
                                request.setAttribute("error_msg", error_msg);
                                RequestDispatcher requestDispatcher = request.getRequestDispatcher("doctormenu.jsp");
                                requestDispatcher.forward(request, response);
                                return;
                        }


                        RequestDispatcher requestDispatcher = request.getRequestDispatcher("doctormenu.jsp");
                        requestDispatcher.forward(request, response);
                }


                if(user.equals("patient")) {

                        if(selected.equals("patient_open_app")) {
                                PatientDAO pob = PatientDAO.getInstance();
                                PatientBean PB = (PatientBean) session.getAttribute("bean");
                                ArrayList<AppointmentBean> appointment_list = pob.getAppointments(PB);

                                if(sort_flag != null && sort_flag.equals("true")) {
                                        appointment_list = sort(request, response, appointment_list);
                                }

                                if(appointment_list == null || appointment_list.isEmpty()) {
                                        String error_msg = "You haven't made any Appointments yet.";
                                        request.setAttribute("error_msg", error_msg);
                                        request.setAttribute("heading", "Appointments");
                                        RequestDispatcher requestDispatcher = request.getRequestDispatcher("listastable.jsp");
                                        requestDispatcher.forward(request, response);
                                        return;
                                }

                                Iterator<AppointmentBean> iter = appointment_list.iterator();
                                ArrayList<String[]> list = new ArrayList<>();
                                String[] s_arr = new String[] {"ID", "Doctor Name", "Doctor Specialization", "Time", "Status"};
                                list.add(s_arr);


                                while(iter.hasNext()) {
                                        AppointmentBean AB = iter.next();
                                        String id = String.valueOf(AB.getID());
                                        String d_name = AB.getDoctor().getName();
                                        String d_specialization = AB.getDoctor().getSpecialization();
                                        String time = AB.getAppointmentTime();
                                        int status_int = AB.getAppointmentStatus();
                                        String status_str = "Empty";

                                        if(status_int == 0) {
                                                status_str = "Pending";
                                        }
                                        if(status_int == 1) {
                                                status_str = "Accepted";
                                        }
                                        if(status_int == 2) {
                                                status_str = "Rejected";
                                        }

                                        String[] str_arr = new String[] {id, d_name, d_specialization, time, status_str};
                                        list.add(str_arr);
                                }

                                request.setAttribute("list", list);
                                request.setAttribute("heading", "Appointments");
                                // request.setAttribute("style", "<style> .table-card { margin: 3em 23.5em; } </style>");

                                if(search_flag != null && search_flag.equals("true")) {
                                        request.setAttribute("redirect_url", "listastable.jsp");
                                        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/Search");
                                        requestDispatcher.forward(request, response);
                                        return;
                                }

                                RequestDispatcher requestDispatcher = request.getRequestDispatcher("listastable.jsp");
                                requestDispatcher.forward(request, response);
                        }
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
