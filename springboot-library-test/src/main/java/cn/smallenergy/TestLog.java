package cn.smallenergy;

import cn.smallenergy.base.log.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestLog {
    //Logger logger = LoggerFactory.getLogger(getClass());
    @Test
    public void testLog() {
        Log.getInstance(this).info("Info 日志222...");
        //logger.trace("Trace 日志1...");
        //logger.debug("Debug 日志1...");
        //logger.info("Info 日志1...");
        //logger.warn("Warn 日志1...");
        //logger.error("Error 日志1...");
    }
}
