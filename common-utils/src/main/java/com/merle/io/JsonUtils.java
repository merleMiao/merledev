package com.merle.io;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class JsonUtils implements Serializable {

    private static final long serialVersionUID = 1L;

    private static Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    @Deprecated
    public static String toJson(Object o) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        SimpleFilterProvider sample = new SimpleFilterProvider();
        sample.setFailOnUnknownId(false);
        mapper.setFilters(sample);
        Writer writer = null;
        String data = null;
        try {
            writer = new StringWriter();
            data = mapper.writeValueAsString(o);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null)
                    writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    @Deprecated
    public static <T> T toBean(String str, Class<T> clazz) {
        T object = null;
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        SimpleFilterProvider sample = new SimpleFilterProvider();
        sample.setFailOnUnknownId(false);
        mapper.setFilters(sample);
        try {
            object = mapper.readValue(str, clazz);
        } catch (Exception e) {
            logger.error("[{}], [{}]", str, clazz);
            e.printStackTrace();
        }
        return object;
    }

    public static String toJson(Object o, ObjectMapper mapper) {
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        SimpleFilterProvider sample = new SimpleFilterProvider();
        sample.setFailOnUnknownId(false);
        mapper.setFilters(sample);
        Writer writer = null;
        String data = null;
        try {
            writer = new StringWriter();
            data = mapper.writeValueAsString(o);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null)
                    writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    public static <T> T toBean(String str, Class<T> clazz, ObjectMapper mapper) {
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        SimpleFilterProvider sample = new SimpleFilterProvider();
        sample.setFailOnUnknownId(false);
        mapper.setFilters(sample);
        T object = null;
        try {
            object = mapper.readValue(str, clazz);
        } catch (Exception e) {
            logger.error("[{}], [{}]", str, clazz);
            e.printStackTrace();
        }
        return object;
    }


    public static void main(String[] args) throws Exception {
    }


    public static Map converKey(Map map) {
        Map result = new HashMap();
        if (map != null && map.size() > 0) {
            Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> e = iterator.next();
                String key = e.getKey();
                Object val = e.getValue();
                result.put(keyToHump(key), val);
            }
        }
        return result;
    }

    private static String keyToHump(String key) {
        if (StringUtils.isNotBlank(key)) {
            String keys[] = StringUtils.split(key, "_");
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < keys.length; i++) {
                if (StringUtils.isNotBlank(keys[i]) && i > 0) {
                    String first = String.valueOf(keys[i].charAt(0)).toUpperCase();
                    sb.append(first);
                    sb.append(StringUtils.substring(keys[i], 1));
                } else {
                    sb.append(StringUtils.defaultString(keys[i]));
                }
            }
            key = sb.toString();
        }
        return key;
    }
}




