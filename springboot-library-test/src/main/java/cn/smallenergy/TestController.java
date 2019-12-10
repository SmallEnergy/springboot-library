package cn.smallenergy;

import cn.smallenergy.anno.OperateLog;
import cn.smallenergy.base.exception.ErrorMessage;
import cn.smallenergy.base.exception.LogicException;
import cn.smallenergy.base.http.RestTemplateUtils;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/test")
public class TestController {
    @GetMapping("/get")
    @OperateLog(value = "bbb")
    public String getTest(){
        System.out.println("this is test method");
        return "aaa";
    }

    @GetMapping("getuser")
    public User getUser() {
        User user = new User();
        user.setUsername("NiuBist");
        user.setId("123");
        return user;
    }

    @GetMapping("gettrail")
    public RestTemplateUtils.RespBody getTrail() {
        HashMap map  = new HashMap<String,String>();
        map.put("siteid","kf_99991");
        map.put("userid","kf_99991_00001");
        String url = "https://nx-v8-g1-trail.ntalker.com/skyeye/enterprises/ux_1000/tracks/nt?nt_id=00fa4bdd-f02b-4ca4-8049-e535b04a2799&nav=all&page=1&per_page=5";
        RestTemplateUtils.RespBody result = RestTemplateUtils.get(url, map,JSONObject.class);
        return result;
    }

    /**
     * 业务逻辑异常
     */
    @GetMapping(path = "logicException")
    public void logicException() {
        throw new LogicException(ErrorMessage.LOGIC_EXCEPTION);
    }

    /**
     * 系统异常
     */
    @GetMapping(path = "systemException")
    public void systemException() {
        throw new NullPointerException("空指针了，哥门!!!");
    }
}
