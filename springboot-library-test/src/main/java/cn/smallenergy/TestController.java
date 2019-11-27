package cn.smallenergy;

import cn.smallenergy.anno.OperateLog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    @GetMapping("/get")
    @OperateLog(value = "bbb")
    public String getTest(){
        System.out.println("this is test method");
        return "aaa";
    }
}
