package user;

import com.merle.basic.Config;
import com.merle.http.HttpClientUtils;
import com.merle.io.JsonUtils;
import org.apache.commons.collections.MapUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.net.www.http.HttpClient;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 少康 on 2016/5/27.
 */
public class UserTest {

    Logger logger = LoggerFactory.getLogger(UserTest.class);

    @Test
    public void save(){
        String url = "http://123.59.95.71:8080/common-web/api/user.json";
        Map map = new HashMap();
        map.put("name", "miao");
        map.put("sex", 0);
        map.put("password", 123456);
        map.put("email", "xxx@xx.com");
        String result = HttpClientUtils.postString(url, map);
        System.out.println(result);
    }

    @Test
    public void test(){
        logger.info("info");
        logger.debug("debug");
        logger.warn("warn");
        logger.error("error");
        logger.info(Config.getLocalProperty("rabbitmq.host"));
        String userInfo = "{\"uuid\":\"CfZ5mxDpbZCYKFfNusCffQ\",\"comp\":null}";

        Map userMap = JsonUtils.toBean(userInfo, Map.class);
        Map compMap = MapUtils.getMap(userMap, "comp");
        System.out.println(JsonUtils.toJson(compMap));
//        {"uuid":"CfZ5mxDpbZCYKFfNusCffQ","comp":null}
    }


}
