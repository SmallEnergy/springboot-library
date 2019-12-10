package cn.smallenergy;

import cn.smallenergy.base.http.RestTemplateUtils;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestRestTemplateUtils {
    Logger logger = LoggerFactory.getLogger(getClass());
   @Test
    public void testGet(){
        HashMap map  = new HashMap<String,String>();
        map.put("siteid","kf_99991");
        map.put("userid","kf_99991_00001");
        String url = "https://nx-v8-g1-trail.ntalker.com/skyeye/enterprises/ux_1000/tracks/nt?nt_id=00fa4bdd-f02b-4ca4-8049-e535b04a2799&nav=all&page=1&per_page=5";
        //RestTemplateUtils.RespBody result = RestTemplateUtils.get(url, map,JSONObject.class);
        //logger.info(result.getRespCode()+"  "+result.getRespValue()+"____"+result.getEntity());
    }

//    @Test
    public void testPut(){
        //有返回
        HashMap map  = new HashMap<String,String>();
        map.put("siteid","kf_99991");
        map.put("userid","kf_99991_00001");
        map.put("export","false");
        map.put("lang","zh-cn");
        String url = "http://localhost:80/kpi/api/v1/dashboard/rpt_panther_consult_analyze/1/data";
        String jsonStr = "{\"time\":{\"starttime\":\"2019-11-26 00:00:00\",\"endtime\":\"2019-11-27 00:00:00\"},\"conditions\":[{\"operation\":\"in\",\"type\":\"String\",\"name\":\"cs_id\",\"values\":[]},{\"operation\":\"in\",\"type\":\"String\",\"name\":\"cs_group_id\",\"values\":[]},{\"operation\":\"in\",\"type\":\"String\",\"name\":\"groupid\",\"values\":[]}]}";
        JSONObject jsonCondition = JSONObject.parseObject(jsonStr);
        RestTemplateUtils.RespBody result = RestTemplateUtils.put(url, jsonCondition.toJSONString(), map,JSONObject.class);
        logger.info(result.getRespCode()+"____"+result.getRespValue()+"____"+result.getEntity().toString());
    }


        @Test
    public void testPutNull(){
        //无返回
        String url = "http://localhost:80/kpi/putTest";
        String jsonStr = "{\"time\":{\"starttime\":\"2019-11-26 00:00:00\",\"endtime\":\"2019-11-27 00:00:00\"},\"conditions\":[{\"operation\":\"in\",\"type\":\"String\",\"name\":\"cs_id\",\"values\":[]},{\"operation\":\"in\",\"type\":\"String\",\"name\":\"cs_group_id\",\"values\":[]},{\"operation\":\"in\",\"type\":\"String\",\"name\":\"groupid\",\"values\":[]}]}";
        JSONObject jsonCondition = JSONObject.parseObject(jsonStr);
        RestTemplateUtils.RespBody result = RestTemplateUtils.put(url, jsonCondition.toJSONString(), JSONObject.class);
            logger.info(result.getRespCode()+"____"+result.getRespValue()+"____"+result.getEntity());
    }



//    @Test
    public void testPost(){
        String url = "https://bj-v4-t1-gateway.ntalker.com/pigeon/app/kf_99991/pullmessage";
        String jsonStr = "{\"userid\":\"kf_99991_c98f7fb7-3c8b-4772-ba00-55e7e664fbaa\",\"versionid\":1574929310001,\"fromate\":1,\"sessionid\":\"pc1574929307678\",\"from\":\"PC\"}";
        JSONObject jsonCondition = JSONObject.parseObject(jsonStr);

        RestTemplateUtils.RespBody result = RestTemplateUtils.post(url,jsonCondition.toJSONString(),null,String.class);
        logger.info(result.getRespCode()+"  "+result.getRespValue()+"____"+result.getEntity().toString());
    }


}
