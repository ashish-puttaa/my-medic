package shared.utilities;

import java.util.*;
import shared.utilities.BeanComparator;
import shared.beans.AppointmentBean;

public class ListSorter<T> {
private static ListSorter instance = null;

private ListSorter() {
}

public static ListSorter getInstance() {
        if(instance == null) {
                instance = new ListSorter<>();
        }
        return instance;
}

public ArrayList<T> sort(ArrayList<T> list, String[] property) {
        BeanComparator c = new BeanComparator(property);
        Collections.sort(list, c);

        return list;
}
}
