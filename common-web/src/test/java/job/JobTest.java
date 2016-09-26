package job;

import com.merle.http.HttpClientUtils;
import com.merle.io.JsonUtils;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 少康 on 2016/6/2.
 */
public class JobTest {

    @Test
    public void addJob(){
//        String jobName = "jobName" + new Date().getTime();
//        String jobGroup = "jobGroup" + new Date().getTime();
        String jobName = "jobName1464846155968";
        String jobGroup = "jobGroup1464846155968";
        String jobDesc = "jobDesc";
        Map jobDataMap = new HashMap();
        jobDataMap.put("url", "http://www.baidu.com");
        jobDataMap.put("method", "post");
        String jobData = JsonUtils.toJson(jobDataMap);
        Calendar calendar = Calendar.getInstance();
        long startTime = calendar.getTimeInMillis();
        calendar.add(Calendar.DAY_OF_MONTH, 3);
        long endTime = calendar.getTimeInMillis();
        String cronExpression = "1/10 * * * * ?";
        Map map = new HashMap();
        map.put("job_name", jobName);
        map.put("job_group", jobGroup);
        map.put("job_desc", jobDesc);
        map.put("job_data", jobData);
        map.put("cron_expression", cronExpression);
        map.put("start_time", startTime);
        map.put("end_time", endTime);
        String result = HttpClientUtils.postString("http://123.59.95.71:8080/common-web/api/job.json", map);
        System.out.println(result);
    }

    @Test
    public void list(){
        String url = "http://localhost:8089/common-web/api/job/list.json?status=planed";
        String result = HttpClientUtils.getString(url);
        System.out.println(result);
    }

    @Test
    public void pause(){
        String url = "http://localhost:8089/common-web/api/job/pause.json?job_name=jobName1464848384337&job_group=jobGroup1464848384337";
        String result = HttpClientUtils.getString(url);
        System.out.println(result);
    }

    @Test
    public void resume(){
        String url = "http://localhost:8089/common-web/api/job/resume.json?job_name=jobName1464846155968&job_group=jobGroup1464846155968";
        String result = HttpClientUtils.getString(url);
        System.out.println(result);
    }

    @Test
    public void delete(){
        String url = "http://localhost:8089/common-web/api/job/delete.json?job_name=jobName1464853494740&job_group=jobGroup1464853494740";
        String result = HttpClientUtils.getString(url);
        System.out.println(result);
    }

    @Test
    public void detail(){
        String url = "http://localhost:8089/common-web/api/job/detail.json?job_name=jobName1464848384337&job_group=jobGroup1464848384337";
        String result = HttpClientUtils.getString(url);
        System.out.println(result);
    }
}
