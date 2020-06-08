import java.io.IOException;
import java.sql.*;
import java.util.Timer;
import java.util.TimerTask;
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
import java.util.concurrent.CopyOnWriteArrayList;

@ServerEndpoint("/push/{client-id}")
public class PushSocket {

static List<Object[]> sessions_list = Collections.synchronizedList(new CopyOnWriteArrayList<Object[]>());

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
                                ArrayList<NotificationBean> notification_list = nob.getAllUnsentNotifications();
                                Iterator<NotificationBean> notification_iter = notification_list.iterator();

                                // while(flag) {
                                while(notification_iter.hasNext()) {
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
                                                        System.out.println("\nMessage to " + target_user_id + " : " + message);
                                                }

                                                nob.changeNotificationsToSent(target_user_id);
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
        // session.getBasicRemote().sendText("User " + message + " connected");
        session.getBasicRemote().sendText("");
        System.out.println("User " + message + " connected");
}

@OnClose
public void close(Session session) {
        Iterator<Object[]> iter = sessions_list.iterator();
        while(iter.hasNext()) {
                Object[] ob = iter.next();
                if(ob[0] == session) {
                        sessions_list.remove(ob);
                        System.out.println("Removed User from Socket Session");
                }
        }

}
}
