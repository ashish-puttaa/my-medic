package shared.beans;

public class DoctorBean {
private int id;
private String f_name;
private String l_name;
private String email;
private int age;
private String specialization;

// Setters
public void setID(int id) {
        this.id = id;
}

public void setFirstName(String fname) {
        this.f_name = fname;
}

public void setLastName(String lname) {
        this.l_name = lname;
}

public void setEmail(String email) {
        this.email = email;
}

public void setAge(int age) {
        this.age = age;
}

public void setSpecialization(String spec) {
        this.specialization = spec;
}


// Getters
public int getID() {
        return this.id;
}

public String getFirstName() {
        return this.f_name;
}

public String getLastName() {
        return this.l_name;
}

public String getEmail() {
        return this.email;
}

public int getAge() {
        return this.age;
}

public String getSpecialization() {
        return this.specialization;
}

public String getName() {
        return this.f_name + " " + this.l_name;
}

}
