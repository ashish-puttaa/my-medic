import java.io.*;
import java.util.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/Search")
public class SearchServlet extends GenericServlet {

public ArrayList<String[]> search(ArrayList<String[]> list, String key) {
        // System.out.println("Inside method");
        String tag_open = "<p style=\"color: red; font-weight: bold; margin: 0 0;\">";
        String tag_close = "</p>";

        Iterator<String[]> iter = list.iterator();

        ArrayList<String[]> search_list = new ArrayList<>();

        String[] heading = iter.next();
        search_list.add(heading);

        while(iter.hasNext()) {
                String[] str_arr = iter.next();
                boolean flag = false;
                for (int i=0; i<str_arr.length; i++) {
                        if(key.toLowerCase().equals(str_arr[i].toLowerCase())) {
                                // if(i != 0)
                                str_arr[i] = tag_open + str_arr[i] + tag_close;
                                flag = true;
                        }
                }
                if(flag) {
                        search_list.add(str_arr);
                }
        }

        return search_list;
}


public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        // System.out.println("Inside Search");

        ArrayList<String[]> list = (ArrayList<String[]>) request.getAttribute("list");
        String key = request.getParameter("search_key");

        list = search(list, key);

        String redirect_url = (String) request.getAttribute("redirect_url");

        request.setAttribute("list", list);

        RequestDispatcher requestDispatcher = request.getRequestDispatcher(redirect_url);
        requestDispatcher.forward(request, response);

}

}
