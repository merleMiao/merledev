package com.merle.scheme;


import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

public class ResultScheme {

    public final static int ACT_RESULT_SUCCESS = 0;

    public static String transfer(String keyword) {
        keyword = StringUtils.replace(keyword, "\\", "\\\\");
        keyword = StringUtils.replace(keyword, "%", "\\%");
        keyword = StringUtils.replace(keyword, "_", "\\_");
        return keyword;
    }

    public static void main(String[] args) {

    }

    public static int[] splitToArray(String ids, String split) {
        String[] id = StringUtils.split(ids, split);
        int[] arr = new int[0];
        if (id != null && id.length > 0) {
            for (String i : id) {
                arr = ArrayUtils.add(arr, Integer.parseInt(i));
            }
        }
        return arr;
    }
}
