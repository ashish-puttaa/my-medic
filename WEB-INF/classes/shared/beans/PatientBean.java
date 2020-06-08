package shared.beans;

public class PatientBean {
private int id;
private String f_name;
private String l_name;
private String email;
private int age;
private String blood_group;
private int height;
private int weight;
private String phone;

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

public void setBloodGroup(String bgroup) {
        this.blood_group = bgroup;
}

public void setHeight(int height) {
        this.height = height;
}

public void setWeight(int weight) {
        this.weight = weight;
}

public void setPhone(String phone) {
        this.phone = phone;
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

public String getBloodGroup() {
        return this.blood_group;
}

public int getHeight() {
        return this.height;
}

public int getWeight() {
        return this.weight;
}

public String getPhone() {
        return this.phone;
}

public String getName() {
        return this.f_name + " " + this.l_name;
}
}
