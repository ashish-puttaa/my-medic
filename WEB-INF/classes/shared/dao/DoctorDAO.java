package shared.dao;
import java.io.*;
import java.util.*;
import java.sql.*;
import com.mysql.jdbc.Driver;
import shared.beans.PatientBean;
import shared.beans.DoctorBean;
import shared.beans.AppointmentBean;

public class DoctorDAO {
private static DoctorDAO instance = null;

Connection connection = null;

private DoctorDAO() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/docapp", "root", "password");
}

public static DoctorDAO getInstance() throws SQLException, ClassNotFoundException {
        if(instance == null) {
                instance = new DoctorDAO();
        }
        return instance;
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


public DoctorBean login(String d_email, String d_password)  throws SQLException {
        Statement st = null;
        DoctorBean DB = null;
        ResultSet rs = null;

        try {
                st = connection.createStatement();
                String query = "SELECT doctor_id FROM doctor_login WHERE email = '" + d_email + "' AND password='" + d_password + "'";
                rs = st.executeQuery(query);

                if(!rs.isBeforeFirst()) {
                        return null;
                }

                rs.next();

                int doctor_id = rs.getInt("doctor_id");
                System.out.println("doctor_id : " + doctor_id);
                rs.close();

                // query = "SELECT id, f_name, l_name, age, specialization FROM doctor WHERE id = '" + doctor_id + "'";
                query = "SELECT * FROM doctor WHERE id = " + doctor_id;
                rs = st.executeQuery(query);

                while(rs.next()) {
                        DB = new DoctorBean();
                        DB.setID(rs.getInt("id"));
                        DB.setFirstName(rs.getString("f_name"));
                        DB.setLastName(rs.getString("l_name"));
                        DB.setEmail(d_email);
                        DB.setAge(rs.getInt("age"));
                        DB.setSpecialization(rs.getString("specialization"));
                }
        }
        finally {
                if(rs != null)
                        rs.close();
                if(st != null)
                        st.close();
        }
        return DB;
}

public ArrayList<DoctorBean> getAllDoctors(String search_key) throws SQLException {
        ArrayList<DoctorBean> list = new ArrayList<>();
        Statement st = null;
        ResultSet rs = null;

        try {
                st = connection.createStatement();
                // String query = "SELECT * FROM doctor";
                String query = "SELECT doctor.*, doctor_login.email FROM doctor INNER JOIN doctor_login ON doctor.id = doctor_login.doctor_id";

                String[] params_str = {"doctor.f_name", "doctor.l_name", "doctor.specialization", "doctor_login.email", "doctor_login.password"};
                String[] params_num = {"doctor.id", "doctor.age"};

                String search_query = searchQueryConstructor(params_num, params_str, search_key);

                rs = st.executeQuery(query + search_query);
                System.out.println("\n\n" + query + search_query);

                if(!rs.isBeforeFirst()) {
                        return null;
                }

                while(rs.next()) {
                        DoctorBean DB = new DoctorBean();
                        DB.setID(rs.getInt("id"));
                        DB.setFirstName(rs.getString("f_name"));
                        DB.setLastName(rs.getString("l_name"));
                        DB.setEmail(rs.getString("email"));
                        DB.setAge(rs.getInt("age"));
                        DB.setSpecialization(rs.getString("specialization"));

                        list.add(DB);
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

public ArrayList<DoctorBean> getAllDoctors() throws SQLException {
        return getAllDoctors("");
}

public void addDoctor(DoctorBean DB, String password)  throws SQLException {
        PreparedStatement pst1 = null;
        PreparedStatement pst2 = null;

        try {
                pst1 = connection.prepareStatement("INSERT INTO doctor (f_name, l_name, age, specialization) VALUES (?, ?, ?, ?)");
                pst1.setString(1, DB.getFirstName());
                pst1.setString(2, DB.getLastName());
                pst1.setInt(3, DB.getAge());
                pst1.setString(4, DB.getSpecialization());

                pst2 = connection.prepareStatement("INSERT INTO doctor_login (doctor_id, email, password) VALUES (LAST_INSERT_ID(), ?, ?)");
                pst2.setString(1, DB.getEmail());
                pst2.setString(2, password);

                pst1.executeUpdate();
                pst2.executeUpdate();
        }
        finally {
                if(pst1 != null)
                        pst1.close();
                if(pst2 != null)
                        pst2.close();
        }
}

public boolean removeDoctor(int id)  throws SQLException {
        Statement st = null;
        int rowsmodified = 0;

        try {
                st = connection.createStatement();
                // String sql = "DELETE FROM doctor WHERE CONCAT(f_name, ' ', l_name) = '" + name + "'";
                String sql = "DELETE FROM doctor WHERE id = " + id;
                rowsmodified = st.executeUpdate(sql);
        }
        finally {
                if(st != null)
                        st.close();
        }

        if(rowsmodified == 0) {
                return false;
        }

        return true;
}

public void updateDoctor(DoctorBean DB)  throws SQLException {
        PreparedStatement pst = null;

        try {
                pst = connection.prepareStatement("UPDATE doctor SET f_name = ?, l_name = ?, age = ?, specialization = ? WHERE id = ?");
                pst.setString(1, DB.getFirstName());
                pst.setString(2, DB.getLastName());
                pst.setInt(3, DB.getAge());
                pst.setString(4, DB.getSpecialization());
                pst.setInt(5, DB.getID());
                pst.executeUpdate();
        }
        finally {
                if(pst != null)
                        pst.close();
        }
}


public DoctorBean getDoctor(int id)  throws SQLException {
        Statement st = null;
        ResultSet rs = null;
        DoctorBean DB = null;

        try {
                st = connection.createStatement();
                // String query = "SELECT * FROM doctor WHERE username = '" + username + "'";
                // String query = "SELECT doctor.*, doctor_login.email FROM doctor INNER JOIN doctor_login ON doctor.id = doctor_login.doctor_id AND doctor_login.email = '" + email + "'";
                String query = "SELECT doctor.*, doctor_login.email FROM doctor INNER JOIN doctor_login ON doctor.id = doctor_login.doctor_id AND doctor_id = " + id;
                rs = st.executeQuery(query);

                while(rs.next()) {
                        DB = new DoctorBean();
                        DB.setID(rs.getInt("id"));
                        DB.setFirstName(rs.getString("f_name"));
                        DB.setLastName(rs.getString("l_name"));
                        DB.setAge(rs.getInt("age"));
                        DB.setSpecialization(rs.getString("specialization"));
                        DB.setEmail(rs.getString("email"));
                }
        }
        finally {
                if(rs != null)
                        rs.close();
                if(st != null)
                        st.close();
        }

        return DB;
}

public PatientBean getPatient(int id) throws SQLException {
        Statement st = null;
        PatientBean PB = null;
        ResultSet rs = null;

        try {
                st = connection.createStatement();
                // String query = "SELECT * FROM patient WHERE email = '" + email + "'";
                String query = "SELECT patient.*, patient_login.email FROM patient INNER JOIN patient_login ON patient.id = patient_login.patient_id AND patient_id = " + id;
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

public ArrayList<AppointmentBean> getAppointments(DoctorBean DB, String search_key)  throws SQLException {
        Statement st = null;
        ResultSet rs = null;
        ArrayList<AppointmentBean> list = new ArrayList<>();

        try {
                st = connection.createStatement();
                // String query = "SELECT patient.f_name as p_f_name, patient.l_name as p_l_name, patient.age as p_age, patient.blood_group as p_blood_group, patient.height as p_height, patient.weight as p_weight, patient.phone as p_phone, appointment.*, doctor.f_name as d_f_name, doctor.l_name as d_l_name, doctor.age as d_age, doctor.specialization as d_specialization FROM patient INNER JOIN appointment ON patient.id = appointment.patient_id INNER JOIN doctor ON doctor.id = appointment.doctor_id AND patient_id = " + PB.getID();
                // String query = "SELECT  appointment.*, doctor.id as d_id, doctor.f_name, doctor.l_name, doctor.age, doctor.specialization FROM patient INNER JOIN appointment ON patient.id = appointment.patient_id INNER JOIN doctor ON doctor.id = appointment.doctor_id AND patient_id = " + PB.getID();
                String query = "SELECT  appointment.*, patient.id as p_id, patient.f_name, patient.l_name, patient.age, patient.blood_group, patient.height, patient.weight, patient.phone, patient_login.email FROM patient INNER JOIN appointment ON patient.id = appointment.patient_id INNER JOIN doctor ON doctor.id = appointment.doctor_id INNER JOIN patient_login ON patient.id = patient_login.patient_id AND doctor_id = " + DB.getID();

                String[] params_str = {"concat(patient.f_name, ' ' ,patient.l_name)", "appointment.time", "appointment.status"};
                String[] params_num = {"appointment.id", "patient.age"};

                String search_query = searchQueryConstructor(params_num, params_str, search_key);

                rs = st.executeQuery(query + search_query);
                System.out.println("\n\nQuery : " + query + search_query);
                System.out.println("Search key : " + search_key);

                while(rs.next()) {
                        PatientBean PB = new PatientBean();
                        PB.setID(rs.getInt("p_id"));
                        PB.setFirstName(rs.getString("f_name"));
                        PB.setLastName(rs.getString("l_name"));
                        PB.setAge(rs.getInt("age"));
                        PB.setBloodGroup(rs.getString("blood_group"));
                        PB.setHeight(rs.getInt("height"));
                        PB.setWeight(rs.getInt("weight"));
                        PB.setPhone(rs.getString("phone"));
                        PB.setEmail(rs.getString("email"));

                        AppointmentBean AB = new AppointmentBean();
                        AB.setPatient(PB);
                        AB.setDoctor(DB);
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

public ArrayList<AppointmentBean> getAppointments(DoctorBean DB)  throws SQLException {
        return getAppointments(DB, "");
}

public AppointmentBean getAppointment(int id) throws SQLException {
        Statement st = null;
        ResultSet rs = null;
        AppointmentBean AB = null;

        try{
                st = connection.createStatement();

                String query = "SELECT  appointment.*, patient.id as p_id, patient.f_name as p_f_name, patient.l_name as p_l_name, patient.age as p_age, patient.blood_group p_blood_group, patient.height as p_height, patient.weight as p_weight, patient.phone as p_phone, patient_login.email as p_email, doctor.id as d_id, doctor.f_name as d_f_name, doctor.l_name as d_l_name, doctor.age as d_age, doctor.specialization as d_specialization, doctor_login.email as d_email FROM patient INNER JOIN appointment ON patient.id = appointment.patient_id INNER JOIN doctor ON doctor.id = appointment.doctor_id INNER JOIN patient_login ON patient.id = patient_login.patient_id INNER JOIN doctor_login ON doctor.id = doctor_login.doctor_id AND appointment.id = " + id;

                rs = st.executeQuery(query);
                if(!rs.next())
                        return null;

                PatientBean PB = new PatientBean();
                PB.setID(rs.getInt("p_id"));
                PB.setFirstName(rs.getString("p_f_name"));
                PB.setLastName(rs.getString("p_l_name"));
                PB.setAge(rs.getInt("p_age"));
                PB.setBloodGroup(rs.getString("p_blood_group"));
                PB.setHeight(rs.getInt("p_height"));
                PB.setWeight(rs.getInt("p_weight"));
                PB.setPhone(rs.getString("p_phone"));
                PB.setEmail(rs.getString("p_email"));

                DoctorBean DB = new DoctorBean();
                DB.setID(rs.getInt("d_id"));
                DB.setFirstName(rs.getString("d_f_name"));
                DB.setLastName(rs.getString("d_l_name"));
                DB.setAge(rs.getInt("d_age"));
                DB.setSpecialization(rs.getString("d_specialization"));
                DB.setEmail(rs.getString("d_email"));

                AB = new AppointmentBean();
                AB.setPatient(PB);
                AB.setDoctor(DB);
                AB.setID(rs.getInt("id"));
                AB.setAppointmentStatus(rs.getInt("status"));
                AB.setAppointmentTime(rs.getString("time"));
                AB.setAppointmentDescription(rs.getString("description"));

        }
        finally {
                if(rs != null)
                        rs.close();
                if(st != null)
                        st.close();
        }

        return AB;
}

public void changeAppointmentStatus(AppointmentBean AB)  throws SQLException {
        PreparedStatement pst = null;

        try {
                pst = connection.prepareStatement("UPDATE appointment SET status = ? WHERE id = ?");
                pst.setInt(1, AB.getAppointmentStatus());
                pst.setInt(2, AB.getID());
                pst.executeUpdate();
        }
        finally {
                if(pst != null)
                        pst.close();
        }
}

public void updatePassword(String email, String password) throws SQLException  {
        PreparedStatement pst = null;

        try {
                pst = connection.prepareStatement("UPDATE doctor_login SET password = ? WHERE email = ?");
                pst.setString(1, password);
                pst.setString(2, email);
                pst.executeUpdate();
        }
        finally {
                if(pst != null)
                        pst.close();
        }
}

public String getPassword(String email) throws SQLException {
        Statement st = null;
        ResultSet rs = null;

        try {
                st = connection.createStatement();
                String query = "SELECT password FROM doctor_login WHERE email = '" + email + "'";
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


public ArrayList<AppointmentBean> getAllAppointments(String search_key)  throws SQLException {
        Statement st = null;
        ResultSet rs = null;
        ArrayList<AppointmentBean> list = new ArrayList<>();

        try {
                st = connection.createStatement();
                String query = "SELECT * FROM appointment";

                String[] params_str = {"time", "status"};
                String[] params_num = {"id", "doctor_id", "patient_id"};

                String search_query = searchQueryConstructor(params_num, params_str, search_key);

                rs = st.executeQuery(query + search_query);
                System.out.println("\n\nQuery : " + query + search_query);
                System.out.println("Search key : " + search_key);

                while(rs.next()) {
                        PatientBean PB = getPatient(rs.getInt("patient_id"));
                        // PB.setID(rs.getInt("patient_id"));

                        DoctorBean DB = getDoctor(rs.getInt("doctor_id"));
                        // DB.setID(rs.getInt("doctor_id"));

                        AppointmentBean AB = new AppointmentBean();
                        AB.setPatient(PB);
                        AB.setDoctor(DB);
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

public ArrayList<AppointmentBean> getAllAppointments()  throws SQLException {
        return getAllAppointments("");
}


}
