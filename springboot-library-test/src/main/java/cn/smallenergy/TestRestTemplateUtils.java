package cn.smallenergy;

import cn.smallenergy.base.http.RestTemplateUtils;
import cn.smallenergy.base.log.Log;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestRestTemplateUtils {

//    @Test
    public void testGet(){
        HashMap map  = new HashMap<String,String>();
        map.put("siteid","kf_99991");
        map.put("userid","kf_99991_00001");
        String url = "http://localhost:80/kpi/api/v1/dashboard/dbd_performance_anaylsis/layout";
        RestTemplateUtils.RespBody result = RestTemplateUtils.get(url, map,JSONObject.class);
        Log.getInstance().info(result.getRespCode()+"  "+result.getRespValue()+"____"+result.getEntity());
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
        Log.getInstance().info(result.getRespCode()+"____"+result.getRespValue()+"____"+result.getEntity().toString());
    }


        @Test
    public void testPutNull(){
        //无返回
        String url = "http://localhost:80/kpi/putTest";
        String jsonStr = "{\"time\":{\"starttime\":\"2019-11-26 00:00:00\",\"endtime\":\"2019-11-27 00:00:00\"},\"conditions\":[{\"operation\":\"in\",\"type\":\"String\",\"name\":\"cs_id\",\"values\":[]},{\"operation\":\"in\",\"type\":\"String\",\"name\":\"cs_group_id\",\"values\":[]},{\"operation\":\"in\",\"type\":\"String\",\"name\":\"groupid\",\"values\":[]}]}";
        JSONObject jsonCondition = JSONObject.parseObject(jsonStr);
        RestTemplateUtils.RespBody result = RestTemplateUtils.put(url, jsonCondition.toJSONString(), JSONObject.class);
        Log.getInstance().info(result.getRespCode()+"____"+result.getRespValue()+"____"+result.getEntity());
    }



//    @Test
    public void testPost(){
        String url = "https://bj-v4-t1-gateway.ntalker.com/pigeon/app/kf_99991/pullmessage";
        String jsonStr = "{\"userid\":\"kf_99991_c98f7fb7-3c8b-4772-ba00-55e7e664fbaa\",\"versionid\":1574929310001,\"fromate\":1,\"sessionid\":\"pc1574929307678\",\"from\":\"PC\"}";
        JSONObject jsonCondition = JSONObject.parseObject(jsonStr);

        RestTemplateUtils.RespBody result = RestTemplateUtils.post(url,jsonCondition.toJSONString(),null,String.class);
        Log.getInstance().info(result.getRespCode()+"  "+result.getRespValue()+"____"+result.getEntity().toString());
    }


}
