package shared.dao;
import java.io.*;
import java.util.*;
import java.sql.*;
import com.mysql.jdbc.Driver;
import shared.beans.PatientBean;
import shared.beans.DoctorBean;
import shared.beans.SessionBean;

public class SessionDAO {
private static SessionDAO instance = null;
Connection connection = null;

private SessionDAO() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/docapp", "root", "password");
}

public static SessionDAO getInstance() throws SQLException, ClassNotFoundException {
        if(instance == null) {
                instance = new SessionDAO();
        }
        return instance;
}

public void addSession(SessionBean SB) throws SQLException {
        PreparedStatement pst = null;

        try {
                String sql = "INSERT INTO sessions (id, user_id, created_date) VALUES (?, ?, ?)";
                pst = connection.prepareStatement(sql);

                pst.setString(1, SB.getID());
                pst.setString(2, SB.getUserIDAsString());
                pst.setLong(3, SB.getCreatedDate());

                pst.executeUpdate();

                // System.out.println("ADDED");
        }
        finally {
                if(pst != null) {
                        pst.close();
                }
        }
}

public SessionBean getSession(String session_id) throws SQLException {
        PreparedStatement pst = null;
        ResultSet rs = null;
        SessionBean SB = null;

        try {
                String sql = "SELECT * FROM sessions WHERE id = ?";
                pst = connection.prepareStatement(sql);
                pst.setString(1, session_id);

                if(!rs.next())
                        return null;

                SB = new SessionBean();
                SB.setID(rs.getString("id"));
                SB.setUserID(rs.getString("user_id"));
                SB.setCreatedDate(rs.getLong("created_date"));

        }
        finally {
                if(rs != null)
                        rs.close();
                if(pst != null)
                        pst.close();
        }

        return SB;
}


public void removeSession(String session_id) throws SQLException {
        PreparedStatement pst = null;

        try {
                String sql = "DELETE FROM sessions WHERE id = ?";
                pst = connection.prepareStatement(sql);
                pst.setString(1, session_id);

                pst.executeUpdate();
        }
        finally {
                if(pst != null)
                        pst.close();
        }
}

public boolean isSessionActive(String session_id) throws SQLException {
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
                String query = "SELECT * FROM sessions WHERE id = ?";
                pst = connection.prepareStatement(query);
                pst.setString(1, session_id);

                rs = pst.executeQuery();

                if(rs.next()) {
                        return true;
                }
                else {
                        return false;
                }
        }
        finally {
                if(rs != null)
                        rs.close();
                if(pst != null)
                        pst.close();
        }
}

public boolean isUserActive(String user_id) throws SQLException {
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
                String query = "SELECT * FROM sessions WHERE user_id = ?";
                pst = connection.prepareStatement(query);
                pst.setString(1, user_id);

                rs = pst.executeQuery();

                if(rs.next()) {
                        return true;
                }
                else {
                        return false;
                }
        }
        finally {
                if(rs != null)
                        rs.close();
                if(pst != null)
                        pst.close();
        }
}

public ArrayList<SessionBean> getAllSessions() throws SQLException {
        Statement st = null;
        ResultSet rs = null;
        ArrayList<SessionBean> list = new ArrayList<>();

        try {
                st = connection.createStatement();
                String query = "SELECT * from sessions";

                rs = st.executeQuery(query);


                while(rs.next()) {
                        SessionBean SB = new SessionBean();
                        SB.setID(rs.getString("id"));
                        SB.setUserID(rs.getString("user_id"));
                        SB.setCreatedDate(rs.getLong("created_date"));

                        list.add(SB);
                }

                return list;
        }
        finally {
                if(rs != null)
                        rs.close();
                if(st != null)
                        st.close();
        }

}

public ArrayList<SessionBean> getUserSessions(String userid) throws SQLException {
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList<SessionBean> list = new ArrayList<>();

        try {
                String query = "SELECT * FROM sessions WHERE user_id = ?";
                pst = connection.prepareStatement(query);

                pst.setString(1, userid);

                rs = pst.executeQuery();

                while(rs.next()) {
                        SessionBean SB = new SessionBean();

                        SB.setID(rs.getString("id"));
                        SB.setUserID(rs.getString("user_id"));
                        SB.setCreatedDate(rs.getLong("created_date"));

                        list.add(SB);
                }

                return list;
        }
        finally {
                if(rs != null)
                        rs.close();
                if(pst != null)
                        pst.close();
        }
}

}
