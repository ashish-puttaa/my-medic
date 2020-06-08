package shared.dao;
import java.io.*;
import java.util.*;
import java.sql.*;
import com.mysql.jdbc.Driver;
import shared.beans.PatientBean;
import shared.beans.DoctorBean;
import shared.beans.AppointmentBean;

public class PatientDAO {
private static PatientDAO instance = null;
Connection connection = null;

private PatientDAO() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/docapp", "root", "password");
}

public String searchQueryConstructor(String[] params_num, String[] params_str, String search_key) {
        if(!search_key.equals("")) {
                String search_query = " WHERE ";
                if(search_key.matches("[0-9]+")) {
                        for(String param : params_num) {

                                search_query += param + " = " + search_key + " OR ";
                        }

                }

                boolean first = true;
                for(String param : params_str) {

                        if(param.contains("Status".toLowerCase())) {
                                if(!search_key.matches("[0-9]+")) {
                                        int key = 99;
                                        if(search_key.equalsIgnoreCase("Pending"))
                                                key = 0;
                                        if(search_key.equalsIgnoreCase("Accepted"))
                                                key = 1;
                                        if(search_key.equalsIgnoreCase("Rejected"))
                                                key = 2;
                                        search_query += " OR " + param + " = " + key;
                                }
                                continue;
                        }

                        if(first) {
                                search_query += param + " LIKE \'%" + search_key + "%\'";
                                first = false;
                        }
                        else {
                                search_query += " OR " + param + " LIKE \'%" + search_key + "%\'";
                        }
                }

                // query += search_query;
                return search_query;
        }

        return "";
}

public static PatientDAO getInstance() throws SQLException, ClassNotFoundException {
        if(instance == null) {
                instance = new PatientDAO();
        }
        return instance;
}

public boolean registerPatient(PatientBean PB, String password) throws SQLException {
        Statement st = null;

        try {
                st = connection.createStatement();
                String query = String.format("INSERT INTO patient (f_name, l_name, age, blood_group, weight, height, phone) VALUES ('%s', '%s', %d, '%s', %d, %d, '%s')", PB.getFirstName(), PB.getLastName(), PB.getAge(), PB.getBloodGroup(), PB.getWeight(), PB.getHeight(), PB.getPhone());
                st.addBatch(query);
                query = String.format("INSERT INTO patient_login (patient_id, email, password) VALUES (LAST_INSERT_ID(), '%s', '%s')", PB.getEmail(), password);
                st.addBatch(query);

                st.executeBatch();
        }
        catch(Exception e) {
                return false;
        }
        finally {
                if(st != null)
                        st.close();
        }
        return true;
}

public PatientBean login(String p_email, String p_password) throws SQLException {
        Statement st = null;
        PatientBean PB = null;

        try {
                st = connection.createStatement();
                String query = "SELECT patient_id FROM patient_login WHERE email = '" + p_email + "' AND password='" + p_password + "'";
                ResultSet rs = st.executeQuery(query);

                if(!rs.isBeforeFirst()) {
                        return null;
                }

                rs.next();
                int patient_id = rs.getInt("patient_id");
                rs.close();

                // query = "SELECT id, f_name, l_name, age, blood_group, height, weight, phone FROM patient WHERE id = " + patient_id;
                query = "SELECT * FROM patient WHERE id = " + patient_id;
                // query = "SELECT patient.id, patient.f_name, patient.l_name, patient.age, patient.blood_group, patient.height, patient.weight, patient.phone, patient_login.email FROM patient INNER JOIN patient_login ON patient.id = patient_login.patient_id AND patient.id = " + patient_id;
                // query = "SELECT patient.*, patient_login.email FROM patient INNER JOIN patient_login ON patient.id = patient_login.patient_id AND patient.id = " + patient_id;
                rs = st.executeQuery(query);

                while(rs.next()) {
                        PB = new PatientBean();
                        PB.setID(rs.getInt("id"));
                        PB.setFirstName(rs.getString("f_name"));
                        PB.setLastName(rs.getString("l_name"));
                        PB.setEmail(p_email);
                        PB.setAge(rs.getInt("age"));
                        PB.setBloodGroup(rs.getString("blood_group"));
                        PB.setHeight(rs.getInt("height"));
                        PB.setWeight(rs.getInt("weight"));
                        PB.setPhone(rs.getString("phone"));
                }


        }
        finally {
                // if(rs != null)
                //    rs.close();
                if(st != null)
                        st.close();
        }
        return PB;
}

public ArrayList<PatientBean> getAllPatients(String search_key) throws SQLException {
        ArrayList<PatientBean> list = new ArrayList<>();
        Statement st = null;
        ResultSet rs = null;

        try {
                st = connection.createStatement();
                // String query = "SELECT * FROM patient";
                String query = "SELECT patient.*, patient_login.email FROM patient INNER JOIN patient_login ON patient.id = patient_login.patient_id";


                String[] params_str = {"patient.f_name", "patient.l_name", "patient.blood_group", "patient.phone", "patient_login.email", "patient_login.password"};
                String[] params_num = {"patient.id", "patient.age", "patient.height", "patient.weight"};

                String search_query = searchQueryConstructor(params_num, params_str, search_key);

                rs = st.executeQuery(query + search_query);
                System.out.println("\n\nQuery : " + query + search_query);
                System.out.println("Search key : " + search_key);

                if(!rs.isBeforeFirst()) {
                        return null;
                }

                while(rs.next()) {
                        PatientBean PB = new PatientBean();
                        PB.setID(rs.getInt("id"));
                        PB.setFirstName(rs.getString("f_name"));
                        PB.setLastName(rs.getString("l_name"));
                        PB.setEmail(rs.getString("email"));
                        PB.setAge(rs.getInt("age"));
                        PB.setBloodGroup(rs.getString("blood_group"));
                        PB.setHeight(rs.getInt("height"));
                        PB.setWeight(rs.getInt("weight"));
                        PB.setPhone(rs.getString("phone"));

                        list.add(PB);
                }
        }
        finally {
                if(rs != null)
                        rs.close();
                if(st != null)
                        st.close();
        }

        return list;

}

public ArrayList<PatientBean> getAllPatients() throws SQLException {
        return getAllPatients("");
}

public void addPatient(PatientBean PB) throws SQLException {
        PreparedStatement pst = null;

        try {
                String sql = "INSERT INTO patient (f_name, l_name, email, age) VALUES (?, ?, ?, ?)";
                pst = connection.prepareStatement(sql);

                pst.setString(1, PB.getFirstName());
                pst.setString(2, PB.getLastName());
                pst.setString(3, PB.getEmail());
                pst.setInt(4, PB.getAge());

                pst.executeUpdate();
        }
        finally {
                if(pst != null)
                        pst.close();
        }
}

public void removePatient(String name) throws SQLException {
        Statement st = null;

        try {
                st = connection.createStatement();
                String sql = "DELETE FROM patient WHERE CONCAT(f_name, ' ', l_name) = '" + name + "'";
                st.executeUpdate(sql);
        }
        finally {
                if(st != null)
                        st.close();
        }
}

public void updatePatient(PatientBean PB) throws SQLException {
        PreparedStatement pst = null;

        try {
                String sql = "UPDATE patient SET f_name = ?, l_name = ?, age = ?, blood_group = ?, height = ?, weight = ?, phone = ? WHERE id = ?";

                pst = connection.prepareStatement(sql);

                pst.setString(1, PB.getFirstName());
                pst.setString(2, PB.getLastName());
                pst.setInt(3, PB.getAge());
                pst.setString(4, PB.getBloodGroup());
                pst.setInt(5, PB.getHeight());
                pst.setInt(6, PB.getWeight());
                pst.setString(7, PB.getPhone());
                pst.setInt(8, PB.getID());

                pst.executeUpdate();
        }
        finally {
                if(pst != null)
                        pst.close();
        }
}

public PatientBean getPatient(String email) throws SQLException {
        Statement st = null;
        PatientBean PB = null;
        ResultSet rs = null;

        try {
                st = connection.createStatement();
                // String query = "SELECT * FROM patient WHERE email = '" + email + "'";
                String query = "SELECT patient.*, patient_login.email FROM patient INNER JOIN patient_login ON patient.id = patient_login.patient_id AND patient_login.email = " + email;
                rs = st.executeQuery(query);

                while(rs.next()) {
                        PB = new PatientBean();
                        PB.setID(rs.getInt("id"));
                        PB.setFirstName(rs.getString("f_name"));
                        PB.setLastName(rs.getString("l_name"));
                        PB.setEmail(rs.getString("email"));
                        PB.setAge(rs.getInt("age"));
                        PB.setBloodGroup(rs.getString("blood_group"));
                        PB.setHeight(rs.getInt("height"));
                        PB.setWeight(rs.getInt("weight"));
                        PB.setPhone(rs.getString("phone"));
                }
        }
        finally {
                if(rs != null)
                        rs.close();
                if(st != null)
                        st.close();
        }
        return PB;
}

public void bookAppointment(AppointmentBean AB) throws SQLException {
        PreparedStatement pst = null;

        try {
                String sql = "INSERT INTO appointment (patient_id, doctor_id, status, time, description) VALUES (?, ?, ?, ?, ?)";
                pst = connection.prepareStatement(sql);

                pst.setInt(1, AB.getPatient().getID());
                pst.setInt(2, AB.getDoctor().getID());
                pst.setInt(3, AB.getAppointmentStatus());
                pst.setString(4, AB.getAppointmentTime());
                pst.setString(5, AB.getAppointmentDescription());

                pst.executeUpdate();
        }
        finally {
                if(pst != null)
                        pst.close();
        }
}

public ArrayList<AppointmentBean> getAppointments(PatientBean PB, String search_key) throws SQLException {
        Statement st = null;
        ResultSet rs = null;
        ArrayList<AppointmentBean> list = new ArrayList<>();

        try {
                st = connection.createStatement();
                // String query = "SELECT patient.f_name as p_f_name, patient.l_name as p_l_name, patient.age as p_age, patient.blood_group as p_blood_group, patient.height as p_height, patient.weight as p_weight, patient.phone as p_phone, appointment.*, doctor.f_name as d_f_name, doctor.l_name as d_l_name, doctor.age as d_age, doctor.specialization as d_specialization FROM patient INNER JOIN appointment ON patient.id = appointment.patient_id INNER JOIN doctor ON doctor.id = appointment.doctor_id AND patient_id = " + PB.getID();
                // String query = "SELECT  appointment.*, doctor.id as d_id, doctor.f_name, doctor.l_name, doctor.age, doctor.specialization FROM patient INNER JOIN appointment ON patient.id = appointment.patient_id INNER JOIN doctor ON doctor.id = appointment.doctor_id AND patient_id = " + PB.getID();
                String query = "SELECT  appointment.*, doctor.id as d_id, doctor.f_name, doctor.l_name, doctor.age, doctor.specialization, doctor_login.email FROM patient INNER JOIN appointment ON patient.id = appointment.patient_id INNER JOIN doctor ON doctor.id = appointment.doctor_id INNER JOIN doctor_login ON doctor.id = doctor_login.doctor_id AND patient_id = " + PB.getID();

                String[] params_str = {"concat(doctor.f_name, ' ' ,doctor.l_name)", "doctor.specialization", "appointment.time", "appointment.status"};
                String[] params_num = {"appointment.id"};

                String search_query = searchQueryConstructor(params_num, params_str, search_key);

                rs = st.executeQuery(query + search_query);
                System.out.println("\n\nQuery : " + query + search_query);
                System.out.println("Search key : " + search_key);

                // rs = st.executeQuery(query);

                while(rs.next()) {
                        DoctorBean DB = new DoctorBean();
                        DB.setID(rs.getInt("d_id"));
                        DB.setFirstName(rs.getString("f_name"));
                        DB.setLastName(rs.getString("l_name"));
                        DB.setAge(rs.getInt("age"));
                        DB.setSpecialization(rs.getString("specialization"));
                        DB.setEmail(rs.getString("email"));

                        AppointmentBean AB = new AppointmentBean();
                        AB.setDoctor(DB);
                        AB.setPatient(PB);
                        AB.setID(rs.getInt("id"));
                        AB.setAppointmentStatus(rs.getInt("status"));
                        AB.setAppointmentTime(rs.getString("time"));
                        AB.setAppointmentDescription(rs.getString("description"));

                        list.add(AB);
                }
        }
        finally {
                if(rs != null)
                        rs.close();
                if(st != null)
                        st.close();
        }

        return list;
}

public ArrayList<AppointmentBean> getAppointments(PatientBean PB) throws SQLException {
        return getAppointments(PB, "");
}

public void updatePassword(String email, String password) throws SQLException {
        Statement st = null;

        try {
                st = connection.createStatement();
                String sql = String.format("UPDATE patient_login SET password = '%s' WHERE email = '%s'", password, email);
                st.executeUpdate(sql);
        }
        finally {
                if(st != null)
                        st.close();
        }
}

public String getPassword(String email) throws SQLException {
        Statement st = null;
        ResultSet rs = null;

        try {
                st = connection.createStatement();
                String query = "SELECT password FROM patient_login WHERE email = '" + email + "'";
                rs = st.executeQuery(query);

                rs.next();

                String password = rs.getString("password");

                return password;
        }
        finally {
                if(rs != null)
                        rs.close();
                if(st != null)
                        st.close();
        }
}


}
