import java.io.IOException;
import java.sql.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.*;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.OnClose;
import javax.websocket.Session;
import javax.websocket.*;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.websocket.server.ServerEndpoint;
import shared.beans.SessionBean;
import shared.dao.SessionDAO;
import shared.dao.NotificationDAO;
import shared.beans.NotificationBean;
import javax.websocket.server.PathParam;

@ServerEndpoint("/push/{client-id}")
public class PushSocket {

static List<Object[]> sessions_list = Collections.synchronizedList(new ArrayList<Object[]>());

@OnMessage
public void onMessage(String message, final Session session) throws IOException, SQLException, ClassNotFoundException {
        // session.getBasicRemote().sendText("Inside onMessage");
        SessionDAO sob = SessionDAO.getInstance();
        NotificationDAO nob = NotificationDAO.getInstance();


        TimerTask timerTask = new TimerTask() {
                // int i = 1;
                @Override
                public void run(){
                        try {
                                // session.getBasicRemote().sendText("Running outside loop");
                                // boolean flag = true;

                                // session.getBasicRemote().sendText("Running inside loop");
                                boolean done_sending = true;

                                while(true) {
                                        // while(notification_iter.hasNext()) {
                                        ArrayList<NotificationBean> notification_list = null;
                                        Iterator<NotificationBean> notification_iter = null;
                                        if(done_sending == true) {
                                                notification_list = nob.getAllUnsentNotifications();

                                                if(notification_list == null) {
                                                        break;
                                                }
                                                else
                                                        notification_iter = notification_list.iterator();

                                        }

                                        done_sending = false;
                                        // session.getBasicRemote().sendText("\nNotifications : " + notification_list.size());
                                        System.out.println("Notifications : " + notification_list.size());

                                        NotificationBean NB = notification_iter.next();

                                        String target_user_id = NB.getReceiverIDAsString();

                                        Session target_user_session = null;

                                        Iterator<Object[]> session_iter = sessions_list.iterator();

                                        // session.getBasicRemote().sendText("Sessions : " + sessions_list.size());
                                        System.out.println("Sessions : " + sessions_list.size());

                                        while(session_iter.hasNext()) {
                                                Object[] obj_arr = session_iter.next();
                                                Session session = (Session) obj_arr[0];
                                                String user_id = (String) obj_arr[1];

                                                // session.getBasicRemote().sendText("\n" + user_id + " = " + target_user_id);
                                                System.out.println(user_id + " = " + target_user_id);
                                                if(user_id.equals(target_user_id)) {
                                                        target_user_session = session;
                                                        break;
                                                }
                                        }

                                        if(target_user_session != null) {
                                                // session.getBasicRemote().sendText("\nNot null");
                                                ArrayList<NotificationBean> user_notifications = nob.getUnsentNotifications(target_user_id);
                                                Iterator<NotificationBean> user_notifications_iter = user_notifications.iterator();

                                                while(user_notifications_iter.hasNext()) {
                                                        NotificationBean user_notification = user_notifications_iter.next();
                                                        String message = user_notification.getMessage();
                                                        target_user_session.getBasicRemote().sendText("Message to " + target_user_id + " : " + message);
                                                }

                                                nob.changeNotificationsToSent(target_user_id);

                                                done_sending = true;
                                        }

                                }
                        }
                        catch (IOException ex) {
                                ex.printStackTrace();
                        }
                        catch (SQLException ex) {
                                ex.printStackTrace();
                        }

                }
        };
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(timerTask, 0,  1000);
}

@OnOpen
public void open(@PathParam("client-id") String message, Session session) throws IOException {
        Object[] arr = new Object[] {session, message};
        // sessions_list.add(session);
        sessions_list.add(arr);
        session.getBasicRemote().sendText("User " + message + " connected");
}

@OnClose
public void close(Session session) {
        System.out.println("Remo");
        for(Object[] ob : sessions_list) {
                if(ob[0] == session) {
                        sessions_list.remove(ob);
                }
        }
}

}
