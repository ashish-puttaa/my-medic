package shared.beans;
import java.util.*;

public class SessionBean {
private String id;
private long created_date;
private int user_id;
private String user_type;

public void setID(String id) {
        this.id = id;
}

public void setCreatedDate(long dateinms) {
        this.created_date = dateinms;
}

public void setUserID(String id) {
        this.user_id = Integer.parseInt(id.substring(1));
        if(id.charAt(0) == 'D') {
                this.user_type = "Doctor";
        }
        if(id.charAt(0) == 'P') {
                this.user_type = "Patient";
        }
}

public void setUserID(int id) {
        this.user_id = id;
}

public void setUserType(String type) {
        this.user_type = type;
}

public String getID() {
        return this.id;
}

public long getCreatedDate() {
        return this.created_date;
}

public int getUserID() {
        return this.user_id;
}

public String getUserType() {
        return this.user_type;
}

public String getUserIDAsString() {
        return this.user_type.charAt(0) + String.valueOf(user_id);
}
}
