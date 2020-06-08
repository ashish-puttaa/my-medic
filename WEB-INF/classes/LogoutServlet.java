import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import javax.servlet.annotation.WebServlet;
import shared.dao.SessionDAO;

@WebServlet("/Logout")
public class LogoutServlet extends HttpServlet {
public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
                HttpSession session = request.getSession();
                String session_id = session.getId();

                SessionDAO ob = SessionDAO.getInstance();
                ob.removeSession(session_id);

                session.invalidate();
                response.sendRedirect("login.jsp");
        }
        catch(SQLException e) {
                System.out.println(e);
        }
        catch(ClassNotFoundException e) {
                System.out.println(e);
        }
        return;
}
}
