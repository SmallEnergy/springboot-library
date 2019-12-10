package cn.smallenergy;

import cn.smallenergy.anno.OperateLog;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestLog {
    Logger logger = LoggerFactory.getLogger(getClass());
    @Resource TestController testController;

    @Test
    @OperateLog("bbb")
    public void testLog() {
        User user = testController.getUser();
        System.out.print("pppppp" + user.toString());
        //Log.getInstance(this).info("Info 日志222...");
        //logger.trace("Trace 日志1...");
        //logger.debug("Debug 日志1...");
        logger.info("Info 日志1...");
        //logger.warn("Warn 日志1...");
        //logger.error("Error 日志1...");
    }

    @Test
    public void testJson() {
        testController.logicException();

        //Log.getInstance(this).info("Info 日志222...");
        //logger.trace("Trace 日志1...");
        //logger.debug("Debug 日志1...");
        logger.info("Info 日志testJson...");
        //logger.warn("Warn 日志1...");
        //logger.error("Error 日志1...");
    }

}
