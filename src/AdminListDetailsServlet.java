import java.io.*;
import java.util.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import shared.dao.DoctorDAO;
import shared.dao.PatientDAO;
import shared.beans.PatientBean;
import shared.beans.DoctorBean;
import shared.beans.AppointmentBean;
import shared.utilities.ListSorter;

@WebServlet("/AdminListDetails")
public class AdminListDetailsServlet extends HttpServlet {

public <T> ArrayList<T> sort(HttpServletRequest request, HttpServletResponse response, ArrayList<T> list) {
        HttpSession session = request.getSession();
        if(session.getAttribute("sort_order") == null) {
                session.setAttribute("sort_order", "asc");
        }

        String property = request.getParameter("sort_property");;
        ListSorter listSorter = ListSorter.getInstance();

        if(property.equals("Patient ID") || property.equals("Doctor ID")) {
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


public void doGet(HttpServletRequest request, HttpServletResponse response)  throws IOException, ServletException {
        String selected = request.getParameter("selected");
        String search_flag = request.getParameter("search_flag");
        String sort_flag = request.getParameter("sort_flag");
        // System.out.println(search_flag);


        try {

                if(selected.equals("viewpatients")) {
                        PatientDAO ob = PatientDAO.getInstance();

                        ArrayList<PatientBean> patient_list = ob.getAllPatients();

                        if(sort_flag != null && sort_flag.equals("true")) {
                                patient_list = sort(request, response, patient_list);
                        }

                        if(patient_list == null || patient_list.isEmpty()) {
                                String error_msg = "No patients yet.";
                                request.setAttribute("error_msg", error_msg);
                                request.setAttribute("heading", "Patients");
                                RequestDispatcher requestDispatcher = request.getRequestDispatcher("listastable.jsp");
                                requestDispatcher.forward(request, response);
                                return;
                        }

                        Iterator<PatientBean> iter = patient_list.iterator();

                        ArrayList<String[]> list = new ArrayList<>();

                        String[] s_arr = new String[] {"ID", "Name", "Age", "Blood Group", "Height", "Weight", "Phone", "Email", "Password"};

                        list.add(s_arr);

                        while(iter.hasNext()) {

                                PatientBean PB = iter.next();

                                String id = String.valueOf(PB.getID());
                                String name = PB.getName();
                                String age = String.valueOf(PB.getAge());
                                String blood_group = PB.getBloodGroup();
                                String height = String.valueOf(PB.getHeight());
                                String weight = String.valueOf(PB.getWeight());
                                String phone = PB.getPhone();
                                String email = PB.getEmail();
                                String password = ob.getPassword(email);

                                String[] str_arr = new String[] {id, name, age, blood_group, height, weight, phone, email, password};

                                list.add(str_arr);
                        }

                        request.setAttribute("list", list);
                        request.setAttribute("heading", "Patients");
                        request.setAttribute("style", "<style> .table-card { margin: 3em 3.5em; } </style>");

                        if(search_flag != null && search_flag.equals("true")) {
                                request.setAttribute("redirect_url", "listastable.jsp");
                                RequestDispatcher requestDispatcher = request.getRequestDispatcher("/Search");
                                requestDispatcher.forward(request, response);
                        }

                        RequestDispatcher requestDispatcher = request.getRequestDispatcher("listastable.jsp");
                        requestDispatcher.forward(request, response);
                        return;
                }


                if(selected.equals("viewdoctors") || selected.equals("removedoctors")) {
                        DoctorDAO ob = DoctorDAO.getInstance();

                        ArrayList<DoctorBean> doctor_list = ob.getAllDoctors();

                        if(sort_flag != null && sort_flag.equals("true")) {
                                doctor_list = sort(request, response, doctor_list);
                        }

                        if(doctor_list == null || doctor_list.isEmpty()) {
                                String error_msg = "No doctors yet.";
                                request.setAttribute("error_msg", error_msg);
                                request.setAttribute("heading", "Doctors");
                                RequestDispatcher requestDispatcher = request.getRequestDispatcher("listastable.jsp");
                                requestDispatcher.forward(request, response);
                                return;
                        }

                        Iterator<DoctorBean> iter = doctor_list.iterator();

                        ArrayList<String[]> list = new ArrayList<>();

                        String[] s_arr = new String[] {"ID", "Name", "Age", "Specialization", "Email", "Password"};

                        list.add(s_arr);

                        while(iter.hasNext()) {

                                DoctorBean DB = iter.next();

                                String id = String.valueOf(DB.getID());
                                String name = DB.getName();
                                String age = String.valueOf(DB.getAge());
                                String specialization = DB.getSpecialization();
                                String email = DB.getEmail();
                                String password = ob.getPassword(email);

                                String[] str_arr = new String[] {id, name, age, specialization, email, password};

                                list.add(str_arr);
                        }

                        request.setAttribute("list", list);



                        // if(selected.equals("viewdoctors")) {
                        //         request.setAttribute("heading", "Doctors");
                        //         request.setAttribute("style", "<style> .table-card { margin: 3em 18em; } </style>");
                        //         RequestDispatcher requestDispatcher = request.getRequestDispatcher("listastable.jsp");
                        //         requestDispatcher.forward(request, response);
                        //         return;
                        // }

                        request.setAttribute("heading", "Admin");


                        if(search_flag != null && search_flag.equals("true")) {
                                request.setAttribute("redirect_url", "adminmenu.jsp");
                                RequestDispatcher requestDispatcher = request.getRequestDispatcher("/Search");
                                requestDispatcher.forward(request, response);
                        }

                        if(selected.equals("removedoctors")) {
                                // request.setAttribute("search_key", "dwight@gmail.com");
                                RequestDispatcher requestDispatcher = request.getRequestDispatcher("adminmenu.jsp");
                                requestDispatcher.forward(request, response);
                                return;
                        }


                }

                if(selected.equals("viewappointments")) {
                        DoctorDAO ob = DoctorDAO.getInstance();

                        ArrayList<AppointmentBean> appointment_list = ob.getAllAppointments();

                        if(sort_flag != null && sort_flag.equals("true")) {
                                appointment_list = sort(request, response, appointment_list);
                        }


                        if(appointment_list == null || appointment_list.isEmpty()) {
                                String error_msg = "No appointments have been made yet.";
                                request.setAttribute("error_msg", error_msg);
                                request.setAttribute("heading", "Appointments");
                                RequestDispatcher requestDispatcher = request.getRequestDispatcher("listastable.jsp");
                                requestDispatcher.forward(request, response);
                                return;
                        }

                        Iterator<AppointmentBean> iter = appointment_list.iterator();

                        ArrayList<String[]> list = new ArrayList<>();

                        String[] s_arr = new String[] {"ID", "Patient ID", "Doctor ID", "Time", "Status"};

                        list.add(s_arr);

                        while(iter.hasNext()) {
                                AppointmentBean AB = iter.next();
                                String id = String.valueOf(AB.getID());
                                String patient_id = String.valueOf(AB.getPatient().getID());
                                String doctor_id = String.valueOf(AB.getDoctor().getID());
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

                                String[] str_arr = new String[] {id, patient_id, doctor_id, time, status_str};
                                list.add(str_arr);
                        }


                        request.setAttribute("list", list);
                        request.setAttribute("heading", "Appointments");
                        request.setAttribute("style", "<style> .table-card { margin: 3em 21em; } </style>");

                        if(search_flag != null && search_flag.equals("true")) {
                                request.setAttribute("redirect_url", "listastable.jsp");
                                RequestDispatcher requestDispatcher = request.getRequestDispatcher("/Search");
                                requestDispatcher.forward(request, response);
                        }

                        RequestDispatcher requestDispatcher = request.getRequestDispatcher("listastable.jsp");
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
}
