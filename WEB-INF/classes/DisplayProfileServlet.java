import java.io.*;
import java.util.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import shared.beans.PatientBean;
import shared.beans.DoctorBean;

@WebServlet("/Display")
public class DisplayProfileServlet extends HttpServlet {
public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        HttpSession session = request.getSession();
        if(session.getAttribute("bean") == null) {
                response.sendRedirect("login.jsp");
                return;
        }

        String user = (String) session.getAttribute("user");

        String[] data = null;
        String[] attributes = null;
        ArrayList<String[]> list = new ArrayList<>();

        if(user.equals("patient")) {
                PatientBean PB = (PatientBean) session.getAttribute("bean");

                data = new String[] {PB.getFirstName(), PB.getLastName(), String.valueOf(PB.getAge()), PB.getBloodGroup(), String.valueOf(PB.getHeight()), String.valueOf(PB.getWeight()), PB.getPhone(), PB.getEmail()};
                attributes = new String[] {"First Name", "Last Name", "Age", "Blood Group", "Height (cm)", "Weight (kg)", "Phone", "Email"};
        }

        if(user.equals("doctor")) {
                DoctorBean DB = (DoctorBean) session.getAttribute("bean");
                data = new String[] {DB.getFirstName(), DB.getLastName(), String.valueOf(DB.getAge()), DB.getSpecialization(), DB.getEmail()};
                attributes = new String[] {"First Name", "Last Name", "Age", "Specialization", "Email"};
        }

        for(int i=0; i<data.length && i<attributes.length; i++) {
                String[] arr = new String[2];
                arr[0] = attributes[i];
                arr[1] = data[i];
                list.add(arr);
        }

        request.setAttribute("list", list);

        RequestDispatcher requestDispatcher = request.getRequestDispatcher("displayprofile.jsp");
        requestDispatcher.forward(request, response);
        return;


}
}
