import java.io.*;
import java.util.*;
import java.lang.reflect.*;
import java.text.Collator;

public class BeanComparator<T> implements Comparator<T>
{
private String[] property;

public BeanComparator(String[] property) {
        this.property = property;
}

public int compare(T o1, T o2) {
        String method_name = "get" + this.property[0];
        try {
                Method o1_getter = o1.getClass().getMethod(method_name);
                Object o1_value = o1_getter.invoke(o1);

                Method o2_getter = o2.getClass().getMethod(method_name);
                Object o2_value = o2_getter.invoke(o2);

                if(property.length == 2) {
                        method_name = "get" + this.property[1];

                        o1_getter = o1_value.getClass().getMethod(method_name);
                        o1_value = o1_getter.invoke(o1_value);

                        o2_getter = o2_value.getClass().getMethod(method_name);
                        o2_value = o2_getter.invoke(o2_value);
                }

                if(o1_value.getClass().getName().equals("java.lang.Integer")) {
                        return (int) o1_value - (int) o2_value;
                }

                // System.out.println("Type : " + o1_value.getClass().getName());

                Collator collator = Collator.getInstance(Locale.getDefault());
                int result = collator.compare(o1_value.toString(), o2_value.toString());
                return result;
        }
        catch(NoSuchMethodException e) {
                e.printStackTrace();
        }
        catch(IllegalAccessException e) {
                e.printStackTrace();
        }
        catch(InvocationTargetException e) {
                e.printStackTrace();
        }


        // System.out.println("\n\n\nValue 1 : " + o1_value.toString());
        // System.out.println("Value 2 : " + o2_value.toString());


        // System.out.println("Result : " + result);
        return -1;

}

}
