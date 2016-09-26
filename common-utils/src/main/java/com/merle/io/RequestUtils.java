package com.merle.io;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.merle.exception.MyException;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

public class RequestUtils {

    private String breakx = "\r\n";

    private boolean underline = true;

    private HttpServletRequest request;

    public RequestUtils(HttpServletRequest request) {
        this.request = request;
        String nsrule = request.getParameter("nsrule");
        if (StringUtils.equalsIgnoreCase("hump", nsrule)) {
            this.underline = false;
        }
    }

    public boolean isUnderline() {
        return underline;
    }

    public boolean isMulti() {
        boolean i = false;
        if ((request.getContentType() != null) && (request.getContentType().contains("multipart/form-data")))
            i = true;
        return i;
    }

    public <T> T create(Class<T> t) throws NoSuchFieldException, InvocationTargetException, IllegalAccessException, InstantiationException {
        T o = t.newInstance();
        Method[] method = o.getClass().getMethods();
        Map<String, String> keys = getDeclaredField(o);
        if (method != null) {
            for (Method m : method) {
                if (StringUtils.startsWith(m.getName(), "set") && m.getParameterTypes() != null && m.getParameterTypes().length == 1) {
                    Class<?> type = m.getParameterTypes()[0];
                    String key = StringUtils.replaceOnce(m.getName(), "set", "");
                    String pre = StringUtils.substring(key, 0, 1);
                    key = StringUtils.lowerCase(pre) + StringUtils.substring(key, 1);
                    String val = request.getParameter(key);
                    if (StringUtils.isBlank(val) && StringUtils.isNotBlank(keys.get(key))) {
                        val = request.getParameter(keys.get(key));
                    }
                    if (StringUtils.isNotBlank(val)) {
                        m.setAccessible(true);
                        String className = type.getName();
                        if (type == String.class) {
                            m.invoke(o, val);
                        } else if (type == Integer.class || StringUtils.equals(type.getSimpleName(), "int")) {
                            m.invoke(o, Integer.parseInt(val));
                        } else if (type.isEnum()) {
                            try {
                                Method tm = type.getMethod("valueOf", String.class);
                                m.invoke(o, tm.invoke(type, val));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (type == Date.class) {
                            try {
                                m.invoke(o, new Date(Long.parseLong(val)));
                            } catch (Exception e) {
                                throw new RuntimeException(e.getMessage() + "\tkey=" + key + "\tval=" + val);
                            }
                        } else if (type == Boolean.class || StringUtils.equals(type.getSimpleName(), "boolean")) {
                            m.invoke(o, Boolean.parseBoolean(val));
                        } else if (type == Float.class || StringUtils.equals(type.getSimpleName(), "float")) {
                            m.invoke(o, Float.parseFloat(val));
                        } else if (type == Double.class || StringUtils.equals(type.getSimpleName(), "double")) {
                            m.invoke(o, Double.parseDouble(val));
                        } else if (type == Long.class || StringUtils.equals(type.getSimpleName(), "long")) {
                            m.invoke(o, Long.parseLong(val));
                        }
                    }
                }
            }
        }
        return o;
    }

    Map<String, String> getDeclaredField(Object o) {
        Map<String, String> map = new HashMap<String, String>();
        for (Class<?> clazz = o.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            for (Field f : clazz.getDeclaredFields()) {
                String key = f.getName();
                JsonProperty property = f.getAnnotation(JsonProperty.class);
                if (property != null) {
                    if (StringUtils.isNotBlank(property.value())) {
                        map.put(key, property.value());
                    }
                }
            }
        }
        return map;
    }

    public Map getListParams() {
        Map params = new HashMap();
        int defLimit = 50;
        int start = this.getInt("start", 0);
        int limit = this.getInt("limit", defLimit);
        int end = this.getInt("end", defLimit);
        int pageIndex = this.getInt("page_index");
        int pageSize = this.getInt("page_size", defLimit);

        start = start <= 0 ? 0 : start;
        limit = limit <= 0 ? defLimit : limit;
        limit = limit > defLimit ? defLimit : limit;

        pageIndex = pageIndex <= 1 ? 1 : pageIndex;
        pageSize = pageSize <= 0 ? defLimit : pageSize;
        pageSize = pageSize > defLimit ? defLimit : pageSize;

        if (exists("start")) {
            if (exists("end")) {
                if (end < start) {
                    end = start + defLimit;
                } else if (end - start > defLimit) {
                    end = start + limit;
                }
                limit = end - start;
            } else {
                end = start + limit;
            }
        } else if (exists("pageIndex")) {
            start = (pageIndex - 1) * pageSize;
            limit = pageSize;
            end = start + limit;
        } else {

        }
        params.put("start", start);
        params.put("limit", limit);
        params.put("end", start + limit);
        return params;
    }

    public boolean exists(String key) {
        checkKey(key);
        return request.getParameterMap().containsKey(key);
    }

    public String getApi() {
        String api = request.getRequestURI();
        String query = request.getQueryString();
        String path = request.getContextPath();
        api = api.substring(path.length());
        if (StringUtils.isNotBlank(query)) {
            api = api + "?" + query;
        }
        return api;
    }

    public String getParameter(String key, String def) {
        checkKey(key);
        boolean isGet = StringUtils.equalsIgnoreCase(request.getMethod(), "get");
        String result = null;
        result = request.getParameter(key);
        if (StringUtils.isBlank(result)) {
            result = request.getParameter(toUnderLine(key));
        }
        if (StringUtils.isBlank(result)) {
            result = request.getParameter(toHum(key));
        }
        if (isGet) {
            try {
                result = new String(StringUtils.defaultString(result).getBytes("iso8859-1"), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        result = StringUtils.trim(StringUtils.defaultString(result));
        return StringUtils.isBlank(result) ? def : result;
    }

    public String getParameter(String key) {
        return getParameter(key, "");
    }

    public String[] getParameters(String key) {
        checkKey(key);
        boolean isGet = StringUtils.equalsIgnoreCase(request.getMethod(), "get");
        String[] result = null;
        result = request.getParameterValues(key);
        if (result == null) {
            result = request.getParameterValues(toUnderLine(key));
        }
        if (result == null) {
            result = request.getParameterValues(toHum(key));
        }
        if (isGet) {
            try {
                if (result != null && result.length > 0) {
                    for (int i = 0; i < result.length; i++) {
                        result[i] = new String(StringUtils.defaultString(result[i]).getBytes("iso8859-1"), "utf-8");
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return result == null ? new String[0] : result;
    }

    public Date getDate(String key, String fmt) {
        Date date = null;
        String val = getParameter(key);
        if (StringUtils.isNotBlank(val) && StringUtils.isNumeric(val)) {
            try {
                date = new Date(Long.parseLong(val));
                return date;
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage() + "   key=" + key + "   val=" + val);
            }
        }
        if (StringUtils.isNotBlank(val)) {
            try {
                if (StringUtils.isBlank(fmt)) {
                    fmt = "yyyy-MM-dd HH:mm:ss";
                }
                SimpleDateFormat sdf = new SimpleDateFormat(fmt);
                date = sdf.parse(val);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage() + "\tkey=" + key + "\tval=" + val);
            }
        }
        return date;
    }

    public int getInt(String key, int def) {
        int _result = def;
        String result = getParameter(key);
        if (StringUtils.isNotBlank(result)) {
            _result = Integer.parseInt(result);
        }
        return _result;
    }

    public int getInt(String key) {
        return getInt(key, 0);
    }

    public int[] getIntValues(String key) {
        checkKey(key);
        int[] _result = new int[0];
        String[] result = request.getParameterValues(key);
        if (result != null) {
            for (String v : result) {
                int iv = Integer.parseInt(v);
                _result = ArrayUtils.add(_result, iv);
            }
        }
        return _result;
    }

    public float[] getFloatValues(String key) {
        checkKey(key);
        float[] _result = new float[0];
        String[] result = this.request.getParameterValues(key);
        if (result != null) {
            for (String v : result) {
                float iv = Float.parseFloat(v);
                _result = ArrayUtils.add(_result, iv);
            }
        }
        return _result;
    }

    public List<Map> getSorters() {
        String sorters = request.getParameter("sorters");
        List result = null;
        if (StringUtils.isNotBlank(sorters)) {
            try {
                String temp = java.net.URLDecoder.decode(sorters, "UTF-8");
                result = (List) JsonUtils.toBean(temp, List.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public boolean getBoolean(String key, boolean def) {
        String val = getParameter(key);
        if (StringUtils.isNotBlank(val)) {
            def = Boolean.parseBoolean(val);
        }
        return def;
    }

    public float getFloat(String key, float def) {
        String val = getParameter(key);
        if (StringUtils.isNotBlank(val)) {
            def = Float.parseFloat(val);
        }
        return def;
    }

    public long getLong(String key, long def) {
        String val = getParameter(key);
        if (StringUtils.isNotBlank(val)) {
            def = Long.parseLong(val);
        }
        return def;
    }

    public double getDouble(String key, double def) {
        String val = getParameter(key);
        if (StringUtils.isNotBlank(val)) {
            def = Double.parseDouble(val);
        }
        return def;
    }

    private static int[] split(String ids) {
        int[] _result = new int[0];
        if (StringUtils.isBlank(ids)) {
            return _result;
        }
        String[] sid = StringUtils.split(ids, "|");
        for (String v : sid) {
            int iv = Integer.parseInt(v);
            _result = ArrayUtils.add(_result, iv);
        }
        return _result;
    }

    public static String toUnderLine(String name) {
        if (StringUtils.isBlank(name)) {
            return "";
        }
        char[] cs = StringUtils.defaultIfEmpty(name, "").toCharArray();
        StringBuffer sb = new StringBuffer();
        boolean exp = true;
        int num = cs.length;
        int i = 0;
        for (i = 0; i < cs.length - 1; i++) {
            boolean isMaxCurr = StringUtils.isAllUpperCase(String.valueOf(cs[i]));
            boolean isMaxNext = StringUtils.isAllUpperCase(String.valueOf(cs[i + 1]));
            sb.append(StringUtils.lowerCase(String.valueOf(cs[i])));
            if (!isMaxCurr && isMaxNext) {
                sb.append("_");
            }
        }
        sb.append(StringUtils.lowerCase(String.valueOf(cs[i])));

        return sb.toString();
    }

    public static String toHum(String name) {
        String[] ns = StringUtils.split(name, "_");
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < ns.length; i++) {
            if (i > 0) {
                result.append(upperFirst(ns[i]));
            } else {
                result.append(ns[i]);
            }
        }

        return result.toString();
    }

    public static String upperFirst(String defaultName) {
        if (StringUtils.isBlank(defaultName)) {
            return "";
        }
        char[] arr = defaultName.toCharArray();
        if (arr != null && arr.length > 0) {
            if (Character.isLowerCase(arr[0])) {
                char upper = Character.toUpperCase(arr[0]);
                arr[0] = upper;
            }
        }
        return new StringBuilder().append(arr).toString();
    }

    public static void main(String args[]) {
//        toUnderLine("userID");
//        toUnderLine("userIDx");
//        toUnderLine("userId");
//        toUnderLine("iphone");
//        toUnderLine("UUID");
//
//        toHum("user_id");
//        toHum("user_x");
//        toHum("iphone_name_fax");


//


        Date date = null;
        date = new Date(Long.parseLong("-2880000000"));



        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(-2880000000l);



    }


    public void checkKey(String key) {
        if (!StringUtils.equals(key, StringUtils.lowerCase(key))) {
            String content = key
                    + breakx + JsonUtils.toJson(request.getParameterMap())
                    + breakx + request.getRequestURI()
                    + (StringUtils.isBlank(request.getQueryString()) ? "" : ("?" + request.getQueryString()));
            throw new MyException(content);
        }
    }

    public static String getRemoteHost(HttpServletRequest request) {
        String ip = null;
        try {
            ip = request.getHeader("x-sensky-real-ip");
            if (ip == null || ip.trim().equals("")) {
                ip = request.getHeader("x-real-ip");
                if (ip == null || ip.trim().equals("")) {
                    ip = request.getRemoteHost();
                }
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ip;
    }


    public static String getParams(HttpServletRequest request) {
        RequestUtils req = new RequestUtils(request);
        StringBuffer sb = new StringBuffer();
        if (req.isMulti()) {

        } else {
            if (StringUtils.equalsIgnoreCase(request.getMethod(), "get")) {
                sb.append(request.getQueryString());
            } else {
                Enumeration<String> keys = request.getParameterNames();
                if (keys != null) {
                    while (keys.hasMoreElements()) {
                        String key = keys.nextElement();
                        if (StringUtils.isNotBlank(key)) {
                            String[] vals = request.getParameterValues(key);
                            if (vals != null) {
                                for (String val : vals) {
                                    sb.append("&").append(StringUtils.defaultString(key)).append("=").append(StringUtils.defaultString(val));
                                }
                            } else {
                                sb.append("&").append(StringUtils.defaultString(key));
                            }
                        }
                    }
                }
            }
        }
        return sb.toString();
    }


}
