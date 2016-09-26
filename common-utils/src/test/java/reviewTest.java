import com.merle.io.JsonUtils;

import static java.lang.System.*;
import java.util.*;

/**
 * Created by merle on 2016/6/27.
 */
public class reviewTest {

    public static void main(String[] args){
//        String source = "abcd";
//        splitString(source);
//        System.out.println("===========================");
        int[] nums = new int[]{1,8,4,5,9,7,2,10,44,55,67};

        nth_largest(nums, 7);
    }

    public static void splitString(String source){
        combineString(source, "");
    }

    public static void combineString(String num, String prefix){
        System.out.println(prefix + "(" + num + ")");
        if(num.length() > 0){
            for(int i = 1 ; i < num.length() ; i ++){
                String newPrefix = prefix + "(" + num.substring(0, i) + ")";
                combineString(num.substring(i, num.length()), newPrefix);
            }
        }
    }

    public static void nth_largest (int[] nums, int n){
        List ints = intArrayAsList(nums);
        Collections.sort(ints);
        JsonUtils.toJson(ints);
    }

    static List intArrayAsList(final int[] a){
        if(a == null)
            throw new NullPointerException();

        return new AbstractList(){

            @Override
            public Object get(int index)
            {
                return new Integer(a[index]);
            }

            @Override
            public int size()
            {
                return a.length;
            }

            //排序所用到的方法
            public Object set(int index, Object o){
                int oldVal = a[index];
                a[index] = ((Integer)o).intValue();
                return new Integer(oldVal);
            }

        };
    }




}
