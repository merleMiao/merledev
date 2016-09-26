package com.merle.util;

import com.merle.io.JsonUtils;
import com.merle.io.RequestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;


public class Debug {

    public static final String breakx = "\t\r\n<br/>";

    public static Logger logger = LoggerFactory.getLogger(Debug.class);

    public static void report(Throwable ex) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (request != null) {
            String method = request.getMethod();
            String params = RequestUtils.getParams(request);
            StringBuffer sb = new StringBuffer();
            StackTraceElement[] list = ex.getStackTrace();
            if (list != null && list.length > 0) {
                for (StackTraceElement e : list) {
                    String className = e.getClassName();
                    boolean self = StringUtils.startsWith(className, "com.fishbone")
                            || StringUtils.startsWith(className, "com.yugu")
                            || StringUtils.startsWith(className, "com.lenxeon");
                    sb.append("\r\n").append("<div style=" + (self ? "color:red !important" : "") + ">");
                    sb.append(className).append(":").append(e.getLineNumber()).append("</div>");
                }
            }
            String ip = RequestUtils.getRemoteHost(request);
            String app = StringUtils.removeEnd(request.getContextPath(), "/");
            String href = "";
            if (method.equalsIgnoreCase("get")) {
                href = request.getRemoteHost() + request.getRequestURI() + "?" + params;
                href = "<a href='" + href + "'>view</a>";
            }
            String content = "msg:" + ex.getMessage()
                    + breakx + "ip:" + ip
                    + breakx + "href:" + href
                    + breakx + getProp(ip)
                    + breakx + "url:" + request.getRequestURI()
                    + breakx + "method:" + method
                    + breakx + "data:" + params
                    + breakx + "params:" + JsonUtils.toJson(request.getParameterMap())
                    + breakx + "stack:" + sb.toString();
//            YuGuMailUtils.getInstance().asyncSendMail("", "error@servermail.yugusoft.com", "", "来自:" + app + " 异常处理中心:" + ex.getMessage(), content);
            logger.error(ex.getMessage(), content);
        } else {
            logger.error(ex.getMessage(), ex);
        }
    }

    public static void report(String title) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (request != null) {
            String method = request.getMethod();
            String params = RequestUtils.getParams(request);
            StringBuffer sb = new StringBuffer();
            String app = StringUtils.removeEnd(request.getContextPath(), "/");
            String ip = RequestUtils.getRemoteHost(request);
            String href = "";
            if (method.equalsIgnoreCase("get")) {
                href = request.getRemoteHost() + request.getRequestURI() + "?" + params;
                href = "<a href='" + href + "'>view</a>";
            }
            String content = "msg:" + title
                    + breakx + "ip:" + ip
                    + breakx + "href:" + href
                    + breakx + getProp(ip)
                    + breakx + "url:" + request.getRequestURI()
                    + breakx + "method:" + method
                    + breakx + "data:" + params
                    + breakx + "params:" + JsonUtils.toJson(request.getParameterMap())
                    + breakx + "stack:" + sb.toString();
//            YuGuMailUtils.getInstance().asyncSendMail("", "yugusoft_001@163.com", "", "来自:" + app + " 异常处理中心:" + title, content);
            logger.error("report(String title)", content);
        } else {
            logger.error("report(String title)", title);
            logger.info("test1111111111111111");
        }
    }

    public static String getProp(String ip) {
        StringBuffer sb = new StringBuffer();
        if (StringUtils.equals(ip, "127.0.0.1")) {
            Properties prop = System.getProperties();
            Iterator<Map.Entry<Object, Object>> iterator = prop.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Object, Object> e = iterator.next();
                if (StringUtils.startsWith((String) e.getKey(), "user") || StringUtils.startsWith((String) e.getKey(), "os")) {
                    sb.append(breakx).append(e.getKey()).append(":").append(e.getValue());
                }
            }
        }
        return sb.toString();
    }
}
