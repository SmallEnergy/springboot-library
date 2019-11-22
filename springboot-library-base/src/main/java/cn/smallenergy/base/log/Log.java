package cn.smallenergy.base.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class Log {
    private static String logPrefix = "badao log -> ";
    private static Log instance;
    private static Logger logger = null;
    private static Map<Class, Logger> loggerList = new HashMap<>(); //用于缓存logger对象

    /**
     * 定义私有构造方法实现单例
     */
    private Log() {
    }

    /**
     * 功能说明：获取服务实例的静态方法
     * 修改说明：
     *
     * @param obj 传入调用此方法的对象
     * @return instance
     */
    public synchronized static Log getInstance(Object obj) {
        if (instance == null) {
            instance = new Log();
        }
        Log.logger = loggerList.get(obj.getClass());
        if (Log.logger == null) {
            Log.logger = LoggerFactory.getLogger(obj.getClass());
            //Log.logger = Logger.getLogger(obj.getClass());
            loggerList.put(obj.getClass(), Log.logger);
        }
        return instance;
    }

    /**
     * 功能说明：获取服务实例的静态方法
     * 修改说明：
     *
     * @param clazz 传入调用此方法的类型
     * @return instance
     */
    public synchronized static Log getInstance(Class clazz) {
        if (instance == null) {
            instance = new Log();
        }
        Log.logger = loggerList.get(clazz);
        if (Log.logger == null) {
            Log.logger = LoggerFactory.getLogger(clazz);
            loggerList.put(clazz, Log.logger);
        }
        return instance;
    }

    /**
     * 功能说明：获取服务实例的静态方法
     * 修改说明：
     *
     * @return instance
     */
    public synchronized static Log getInstance() {
        if (instance == null) {
            instance = new Log();
        }
        Log.logger = loggerList.get(Log.class);
        if (Log.logger == null) {
            Log.logger = LoggerFactory.getLogger(Log.class);
            loggerList.put(Log.class, Log.logger);
        }
        return instance;
    }

    public void trace(String message) {
        Log.logger.trace(logPrefix + message);
    }

    public void trace(String message, Throwable t) {
        Log.logger.trace(logPrefix + message, t);
    }

    public void debug(String message) {
        Log.logger.debug(logPrefix + message);
    }

    public void debug(String message, Throwable t) {
        Log.logger.debug(logPrefix + message, t);
    }

    public void info(String message) {
        Log.logger.info(logPrefix + message);
    }

    public void info(String message, Throwable t) {
        Log.logger.info(logPrefix + message, t);
    }

    public void warn(String message) {
        Log.logger.warn(logPrefix + message);
    }

    public void warn(String message, Throwable t) {
        Log.logger.warn(logPrefix + message, t);
    }

    public void error(String message, Object... arguments) {
        Log.logger.error(logPrefix + message, arguments);
    }

    public void error(String message, Throwable t, Object... arguments) {
        Log.logger.error(logPrefix + message, t, arguments);
    }
}
