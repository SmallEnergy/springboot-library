package cn.smallenergy;

import cn.smallenergy.anno.OperateLog;
import cn.smallenergy.base.exception.ErrorMessage;
import cn.smallenergy.base.exception.LogicException;
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

    @GetMapping("getuser")
    public User getUser() {
        User user = new User();
        user.setUsername("NiuBist");
        user.setId("123");
        return user;
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
