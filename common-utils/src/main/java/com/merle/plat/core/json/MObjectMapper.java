package com.merle.plat.core.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

//import com.fasterxml.jackson.map.ObjectMapper;

public class MObjectMapper extends ObjectMapper {
    public MObjectMapper() {
//        disable(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS);
//        configure(SerializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIE
//        AnnotationIntrospector primary = new MAnnotationIntrospector();
//        AnnotationIntrospector pair = new AnnotationIntrospector.Pair(primary, primary);
//        this.setAnnotationIntrospector(pair);
//        this.getSerializationConfig().withAnnotationIntrospector(primary);
//        this.setPropertyNamingStrategy(new NameStrategy());
//        this.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        this.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, true);
        SimpleFilterProvider sample = new SimpleFilterProvider();
        sample.setFailOnUnknownId(false);
        this.setFilters(sample);
//        this.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);//下划线
//        this.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    public static void getFields(Class clazz, Set<String> set) {
        List<Field> fields = Arrays.asList(clazz.getDeclaredFields());
        if (fields != null) {
            for (Field field : fields) {
                set.add(field.getName());
                JsonProperty property = field.getAnnotation(JsonProperty.class);
                if (property != null) {
                    set.add(property.value());
                }
            }
        }
        Method[] methods = clazz.getDeclaredMethods();
        if (methods != null) {
            for (Method method : methods) {
                String name = method.getName();
                if (StringUtils.startsWithIgnoreCase(name, "get")) {
                    name = StringUtils.substring(name, "get".length());
                    String pre = StringUtils.substring(name, 0, 1);
                    String last = StringUtils.substring(name, 1);
                    name = StringUtils.lowerCase(pre) + last;
                    set.add(name);
                }
                if (StringUtils.startsWithIgnoreCase(name, "is")) {
                    name = StringUtils.substring(name, "is".length());
                    String pre = StringUtils.substring(name, 0, 1);
                    String last = StringUtils.substring(name, 1);
                    name = StringUtils.lowerCase(pre) + last;
                    set.add(name);
                }
                JsonProperty property = method.getAnnotation(JsonProperty.class);
                if (property != null) {
                    set.add(property.value());
                }
            }
        }
        while (clazz.getSuperclass() != null && clazz.getSuperclass() != Object.class) {
            clazz = clazz.getSuperclass();
            getFields(clazz, set);
        }
    }
}
