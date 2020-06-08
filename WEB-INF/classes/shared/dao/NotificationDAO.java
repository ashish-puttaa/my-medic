package shared.dao;
import java.io.*;
import java.util.*;
import java.sql.*;
import com.mysql.jdbc.Driver;
import shared.beans.PatientBean;
import shared.beans.DoctorBean;
import shared.beans.NotificationBean;

public class NotificationDAO {
private static NotificationDAO instance = null;
Connection connection = null;

private NotificationDAO() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/docapp", "root", "password");
}

public static NotificationDAO getInstance() throws SQLException, ClassNotFoundException {
        if(instance == null) {
                instance = new NotificationDAO();
        }
        return instance;
}


public void setNotification(DoctorBean SenderBean, PatientBean ReceiverBean, String message) throws SQLException {
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
                String sql = "INSERT INTO notification (sender_id, receiver_id, message, view_status, sent_status) VALUES (?, ?, ?, ?, ?)";
                pst = connection.prepareStatement(sql);
                final int unread = 0;
                final int not_sent = 0;

                pst.setString(1, "D" + String.valueOf(SenderBean.getID()));
                pst.setString(2, "P" + String.valueOf(ReceiverBean.getID()));
                pst.setString(3, message);
                pst.setInt(4, unread);
                pst.setInt(5, not_sent);

                pst.executeUpdate();
        }
        finally {
                if(rs != null)
                        rs.close();
                if(pst != null)
                        pst.close();
        }
}

public void setNotification(PatientBean SenderBean, DoctorBean ReceiverBean, String message) throws SQLException {
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
                String sql = "INSERT INTO notification (sender_id, receiver_id, message, view_status, sent_status) VALUES (?, ?, ?, ?, ?)";
                pst = connection.prepareStatement(sql);
                final int unread = 0;
                final int not_sent = 0;

                pst.setString(1, "P" + String.valueOf(SenderBean.getID()));
                pst.setString(2, "D" + String.valueOf(ReceiverBean.getID()));
                pst.setString(3, message);
                pst.setInt(4, unread);
                pst.setInt(5, not_sent);

                pst.executeUpdate();
        }
        finally {
                if(rs != null)
                        rs.close();
                if(pst != null)
                        pst.close();
        }
}


public void changeNotificationsToRead(String id) throws SQLException {
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
                pst = connection.prepareStatement("UPDATE notification SET view_status = ? WHERE receiver_id = ?");
                final int read = 1;

                pst.setInt(1, read);
                pst.setString(2, id);

                pst.executeUpdate();
        }
        finally {
                if(rs != null)
                        rs.close();
                if(pst != null)
                        pst.close();
        }
}

public void changeNotificationsToRead(PatientBean ReceiverBean) throws SQLException {
        changeNotificationsToRead("P" + ReceiverBean.getID());
}

public void changeNotificationsToRead(DoctorBean ReceiverBean) throws SQLException {
        changeNotificationsToRead("D" + ReceiverBean.getID());
}


public ArrayList<NotificationBean> getNotifications(String id) throws SQLException {
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList<NotificationBean> list = new ArrayList<>();

        try {
                String query = "SELECT * from notification WHERE view_status = ? AND receiver_id = ?";
                pst = connection.prepareStatement(query);
                final int unread = 0;

                pst.setInt(1, unread);
                pst.setString(2, id);

                rs = pst.executeQuery();


                while(rs.next()) {
                        NotificationBean NB = new NotificationBean();

                        NB.setSenderID(rs.getString("sender_id"));
                        NB.setReceiverID(rs.getString("receiver_id"));
                        NB.setMessage(rs.getString("message"));
                        NB.setViewStatus(rs.getInt("view_status"));
                        NB.setSentStatus(rs.getInt("sent_status"));

                        list.add(NB);
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

public ArrayList<NotificationBean> getNotifications(DoctorBean ReceiverBean) throws SQLException {
        return getNotifications("D" + ReceiverBean.getID());
}

public ArrayList<NotificationBean> getNotifications(PatientBean ReceiverBean) throws SQLException {
        return getNotifications("P" + ReceiverBean.getID());
}


public ArrayList<NotificationBean> getUnsentNotifications(String id) throws SQLException {
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList<NotificationBean> list = new ArrayList<>();

        try {
                String query = "SELECT * from notification WHERE view_status = ? AND sent_status = ? AND receiver_id = ?";
                pst = connection.prepareStatement(query);
                final int unread = 0;
                final int not_sent = 0;

                pst.setInt(1, unread);
                pst.setInt(2, not_sent);
                pst.setString(3, id);

                rs = pst.executeQuery();


                while(rs.next()) {
                        NotificationBean NB = new NotificationBean();

                        NB.setSenderID(rs.getString("sender_id"));
                        NB.setReceiverID(rs.getString("receiver_id"));
                        NB.setMessage(rs.getString("message"));
                        NB.setViewStatus(rs.getInt("view_status"));
                        NB.setSentStatus(rs.getInt("sent_status"));

                        list.add(NB);
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

public ArrayList<NotificationBean> getUnsentNotifications(DoctorBean ReceiverBean) throws SQLException {
        return getUnsentNotifications("D" + ReceiverBean.getID());
}

public ArrayList<NotificationBean> getUnsentNotifications(PatientBean ReceiverBean) throws SQLException {
        return getUnsentNotifications("P" + ReceiverBean.getID());
}


public ArrayList<NotificationBean> getAllUnsentNotifications() throws SQLException {
        Statement st = null;
        ResultSet rs = null;

        ArrayList<NotificationBean> list = new ArrayList<>();

        try {
                st = connection.createStatement();
                String query = "SELECT * FROM notification WHERE sent_status = 0";

                rs = st.executeQuery(query);

                while(rs.next()) {
                        NotificationBean NB = new NotificationBean();

                        NB.setSenderID(rs.getString("sender_id"));
                        NB.setReceiverID(rs.getString("receiver_id"));
                        NB.setMessage(rs.getString("message"));
                        NB.setViewStatus(rs.getInt("view_status"));
                        NB.setSentStatus(rs.getInt("sent_status"));

                        list.add(NB);
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

public ArrayList<NotificationBean> getAllNotifications() throws SQLException {
        Statement st = null;
        ResultSet rs = null;

        ArrayList<NotificationBean> list = new ArrayList<>();

        try {
                st = connection.createStatement();
                String query = "SELECT * FROM notification";

                rs = st.executeQuery(query);

                while(rs.next()) {
                        NotificationBean NB = new NotificationBean();

                        NB.setSenderID(rs.getString("sender_id"));
                        NB.setReceiverID(rs.getString("receiver_id"));
                        NB.setMessage(rs.getString("message"));
                        NB.setViewStatus(rs.getInt("view_status"));
                        NB.setSentStatus(rs.getInt("sent_status"));

                        list.add(NB);
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


public void changeNotificationsToSent(String receiver_id) throws SQLException {
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
                pst = connection.prepareStatement("UPDATE notification SET sent_status = ? WHERE receiver_id = ?");
                final int sent = 1;

                pst.setInt(1, sent);
                pst.setString(2, receiver_id);

                pst.executeUpdate();
        }
        finally {
                if(rs != null)
                        rs.close();
                if(pst != null)
                        pst.close();
        }
}

public void changeNotificationsToSent(PatientBean ReceiverBean) throws SQLException {
        changeNotificationsToSent("P" + ReceiverBean.getID());
}

public void changeNotificationsToSent(DoctorBean ReceiverBean) throws SQLException {
        changeNotificationsToSent("D" + ReceiverBean.getID());
}


public void changeNotificationToSent(int notification_id) throws SQLException {
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
                pst = connection.prepareStatement("UPDATE notification SET sent_status = ? WHERE id = ?");
                final int sent = 1;

                pst.setInt(1, sent);
                pst.setInt(2, notification_id);

                pst.executeUpdate();
        }
        finally {
                if(rs != null)
                        rs.close();
                if(pst != null)
                        pst.close();
        }
}

public void changeNotificationToRead(int notification_id) throws SQLException {
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
                pst = connection.prepareStatement("UPDATE notification SET view_status = ? WHERE id = ?");
                final int read = 1;

                pst.setInt(1, read);
                pst.setInt(2, notification_id);

                pst.executeUpdate();
        }
        finally {
                if(rs != null)
                        rs.close();
                if(pst != null)
                        pst.close();
        }
}


}
