import java.io.*;
import java.util.Date;
import java.util.*;
import java.sql.*;
import shared.beans.SessionBean;
import shared.dao.SessionDAO;


public class InvalidateInactiveSessions {
public static void run() {
        try {
                SessionDAO ob = SessionDAO.getInstance();

                ArrayList<SessionBean> sessions_list = ob.getAllSessions();
                Iterator<SessionBean> iter = sessions_list.iterator();

                if(!iter.hasNext()) {
                        System.out.println("No sessions.");
                        return;
                }

                while(iter.hasNext()) {
                        SessionBean SB = iter.next();

                        long created_date = SB.getCreatedDate();
                        Date currentTime = new Date(System.currentTimeMillis());
                        Date halfHourAgo = new Date(System.currentTimeMillis() - 30*60*1000);
                        Date created = new Date(created_date);

                        System.out.println("Created : " + created.toString());
                        if (created.before(halfHourAgo)) {
                                System.out.println("More than half hour old, invalidated!");
                                ob.removeSession(SB.getID());
                                // System.out.println("Ago : " + halfHourAgo.toString());
                        }
                        else {
                                System.out.println("Session of User : " + SB.getUserIDAsString() + " has ended.");
                                // System.out.println("Not yet old.");
                                // System.out.println("Ago : " + halfHourAgo.toString());
                        }
                }
        }
        catch(SQLException e) {
                e.printStackTrace();
        }
        catch(ClassNotFoundException e) {
                e.printStackTrace();
        }
}

public static void main(String[] args) throws InterruptedException {
        // InvalidateInactiveSessions ob = new InvalidateInactiveSessions();

        // Thread t = new Thread(ob);
        // t.start();

        while(true) {
                run();
                Thread.sleep(5000);
        }
}
}
