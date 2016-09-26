package com.merle.exception;

import com.merle.scheme.ExpScheme;
import com.merle.util.Debug;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class MyExceptionResolver extends AbstractHandlerExceptionResolver {

    public int result = ExpScheme.UNKNOWN;

    public static Logger logger = LoggerFactory.getLogger(MyExceptionResolver.class);

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) {
        ModelAndView modelAndView = new ModelAndView("comm/err");
        logger.error("comm_err:" + e.getMessage(), e);
        modelAndView.addObject("result", result == 0 ? 9 : result);
        if (e instanceof MyException) {
            MyException ex = (MyException) e;
            modelAndView.addObject("msg", ex.getMsg());
            modelAndView.addObject("result", ex.getResult());
        } else {
//            modelAndView.addObject("msg", "系统错误,请稍候重试");
//            modelAndView.addObject("ex", parser(e));
        }
        Debug.report(e);
        return modelAndView;
    }

    private static Map parser(Exception ex) {
        Map map = new LinkedHashMap();
        if (ex != null) {
            map.put("msg", ex.getMessage());
            List<Map> lists = new ArrayList<Map>();
            StackTraceElement[] list = ex.getStackTrace();
            if (list != null) {
                for (StackTraceElement e : list) {
                    Map m = new LinkedHashMap();
                    m.put("clazz_name", e.getClassName());
                    m.put("file_name", e.getFileName());
                    m.put("line_num", e.getLineNumber());
                    m.put("method", e.getMethodName());
                    lists.add(m);
                }
            }
        }
        return map;
    }
}
