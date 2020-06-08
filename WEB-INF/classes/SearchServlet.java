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
        String tag_open = "<p style=\"display:inline; background-color:lightgreen; color: red; font-weight: bold; margin: 0 0;\">";
        String tag_close = "</p>";

        Iterator<String[]> iter = list.iterator();

        ArrayList<String[]> search_list = new ArrayList<>();

        ArrayList<String> num_headings = new ArrayList<>();
        num_headings.add("id");
        num_headings.add("patient id");
        num_headings.add("doctor id");
        num_headings.add("age");
        num_headings.add("patient age");
        num_headings.add("height");
        num_headings.add("weight");

        String[] heading_arr = iter.next();
        search_list.add(heading_arr);

        while(iter.hasNext()) {
                String[] str_arr = iter.next();
                boolean flag = false;
                for (int i=0; i<str_arr.length; i++) {
                        if(num_headings.contains(heading_arr[i].toLowerCase()) && key.matches("[0-9]+")) {
                                if(str_arr[i].equals(key)) {
                                        str_arr[i] = tag_open + str_arr[i] + tag_close;
                                        flag = true;
                                }
                        }
                        else {
                                if(str_arr[i].toLowerCase().contains(key.toLowerCase())) {

                                        int start_index = str_arr[i].toLowerCase().indexOf(key.toLowerCase());
                                        int last_index = start_index + key.length();

                                        String match_str = str_arr[i].substring(start_index, last_index);
                                        match_str = tag_open + match_str + tag_close;
                                        String first_part = str_arr[i].substring(0, start_index);
                                        String last_part = str_arr[i].substring(last_index);

                                        str_arr[i] = first_part + match_str + last_part;
                                        flag = true;
                                }
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

        if(list == null || list.size() == 1) {
                String error_msg = "No results avaliable";
                request.setAttribute("error_msg", error_msg);
        }

        RequestDispatcher requestDispatcher = request.getRequestDispatcher(redirect_url);
        requestDispatcher.forward(request, response);

}

}
