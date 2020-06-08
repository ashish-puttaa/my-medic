package shared.beans;

public class AppointmentBean {
private int id;
private PatientBean PB;
private DoctorBean DB;
private int appointment_status = 0;
private String appointment_time;
private String appointment_description;

// Setters
public void setID(int id) {
        this.id = id;
}

public void setPatient(PatientBean P) {
        this.PB = P;
}

public void setDoctor(DoctorBean D) {
        this.DB = D;
}

public void setAppointmentStatus(int status) {
        this.appointment_status = status;
}

public void setAppointmentTime(String time) {
        this.appointment_time = time;
}

public void setAppointmentDescription(String desc) {
        this.appointment_description = desc;
}

// Getters
public int getID() {
        return this.id;
}

public PatientBean getPatient() {
        return this.PB;
}

public DoctorBean getDoctor() {
        return this.DB;
}

public int getAppointmentStatus() {
        return this.appointment_status;
}

public String getAppointmentTime() {
        return this.appointment_time;
}

public String getAppointmentDescription() {
        return this.appointment_description;
}

public int getStatus() {
        return this.appointment_status;
}

public String getTime() {
        return this.appointment_time;
}

public String getDescription() {
        return this.appointment_description;
}

}
