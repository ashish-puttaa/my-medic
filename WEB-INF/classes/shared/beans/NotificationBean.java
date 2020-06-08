package shared.beans;

public class NotificationBean {
private int receiver_id;
private String receiver_type;
private int sender_id;
private String sender_type;
private String message;
private int view_status;
private int sent_status;

public void setSenderID(String id) {
        this.sender_id = Integer.parseInt(id.substring(1));
        if(id.charAt(0) == 'D') {
                this.sender_type = "Doctor";
        }
        if(id.charAt(0) == 'P') {
                this.sender_type = "Patient";
        }
}

public void setReceiverID(String id) {
        this.receiver_id = Integer.parseInt(id.substring(1));
        if(id.charAt(0) == 'D') {
                this.receiver_type = "Doctor";
        }
        if(id.charAt(0) == 'P') {
                this.receiver_type = "Patient";
        }
}


public void setMessage(String msg) {
        this.message = msg;
}

public void setViewStatus(int vstatus) {
        this.view_status = vstatus;
}

public void setSentStatus(int sstatus) {
        this.sent_status = sstatus;
}

public String getSenderIDAsString() {
        return Character.toUpperCase(this.sender_type.charAt(0)) + String.valueOf(sender_id);
}

public String getReceiverIDAsString() {
        return Character.toUpperCase(this.receiver_type.charAt(0)) + String.valueOf(receiver_id);
}

public String getMessage() {
        return this.message;
}

public int getViewStatus() {
        return this.view_status;
}

public int getSentStatus() {
        return this.sent_status;
}

public int getReceiverID() {
        return this.receiver_id;
}

public int getSenderID() {
        return this.sender_id;
}

public String getReceiverType() {
        return this.receiver_type;
}

public String getSenderType() {
        return this.sender_type;
}

}
