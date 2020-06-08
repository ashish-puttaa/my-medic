import java.io.*;
import java.util.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import shared.beans.DoctorBean;
import shared.dao.DoctorDAO;
import shared.utilities.ListSorter;

@WebServlet("/ListDoctors")
public class ListDoctorsServlet extends HttpServlet {

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


public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        HttpSession session = request.getSession();
        if(session.getAttribute("bean") == null) {
                response.sendRedirect("login.jsp");
                return;
        }

        String user = (String) session.getAttribute("user");
        String search_flag = request.getParameter("search_flag");
        String search_key = request.getParameter("search_key");
        String sort_flag = request.getParameter("sort_flag");

        if(search_key == null) {
                // System.out.println("Inside null");
                search_key = (String) session.getAttribute("search_key");
        }

        else if(search_key != null || search_key.equals("")) {
                // System.out.println("Inside not null");
                session.setAttribute("search_key", search_key);
        }

        try {
                if(user.equals("patient")) {
                        DoctorDAO ob = DoctorDAO.getInstance();
                        int arraysize = 4;
                        ArrayList<DoctorBean> doctor_list = ob.getAllDoctors();

                        if(search_key != null) {
                                doctor_list = ob.getAllDoctors(search_key);
                        }
                        else
                                doctor_list = ob.getAllDoctors();

                        if(doctor_list == null || doctor_list.isEmpty()) {
                                String error_msg = "No matching results were found.";
                                request.setAttribute("error_msg", error_msg);
                                request.setAttribute("heading", "Doctors");
                                RequestDispatcher requestDispatcher = request.getRequestDispatcher("patientmenu.jsp");
                                requestDispatcher.forward(request, response);
                                return;
                        }

                        if(sort_flag != null && sort_flag.equals("true")) {
                                doctor_list = sort(request, response, doctor_list);
                        }

                        Iterator<DoctorBean> iter = doctor_list.iterator();

                        ArrayList<String[]> doctor_list_str = new ArrayList<>();

                        String[] s_arr = new String[] {"ID", "Name", "Age", "Specialization"};

                        doctor_list_str.add(s_arr);

                        while(iter.hasNext()) {

                                DoctorBean DB = iter.next();
                                String id = String.valueOf(DB.getID());
                                String name = DB.getFirstName() + " " + DB.getLastName();
                                String age = String.valueOf(DB.getAge());
                                String specialization = DB.getSpecialization();

                                String[] str_arr = new String[] {id, name, age, specialization};

                                doctor_list_str.add(str_arr);
                        }

                        request.setAttribute("list", doctor_list_str);
                        request.setAttribute("heading", "Available Doctors");

                        if(search_flag != null && search_flag.equals("true")) {
                                request.setAttribute("redirect_url", "patientmenu.jsp");
                                RequestDispatcher requestDispatcher = request.getRequestDispatcher("/Search");
                                requestDispatcher.forward(request, response);
                                return;
                        }

                        String selected = request.getParameter("selected");
                        RequestDispatcher requestDispatcher = request.getRequestDispatcher("patientmenu.jsp");
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
