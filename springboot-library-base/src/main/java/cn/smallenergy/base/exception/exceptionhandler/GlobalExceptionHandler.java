package cn.smallenergy.base.exception.exceptionhandler;

import cn.smallenergy.base.exception.ErrorMessage;
import cn.smallenergy.base.exception.LogicException;
import cn.smallenergy.base.exception.RestResult;
import org.omg.CORBA.portable.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 异常统一处理，
 *
 * @author zhaoxuezhi
 * Created on 2018/9/21-15:13.
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 全局异常处理
     *
     * @param e        异常
     * @param request  请求
     * @param response 响应
     * @return 处理后的异常的数据结构
     */
    @ExceptionHandler(value = {Exception.class,ApplicationException.class})
    @ResponseBody
    public Object logicExceptionHandler(HttpServletRequest request, Exception e, HttpServletResponse response) {
        //系统级异常，错误码固定为-1，提示语固定为系统繁忙，请稍后再试
        RestResult result = new RestResult(false, RestResult.SYSTEM_ERROR_CODE, e.getMessage(), ErrorMessage.SYSTEM_EXCEPTION);
        //对系统级异常进行日志记录
        logger.error("系统异常:" + e.getMessage(), e);
        return result;
    }

    /**
     * 如果是业务逻辑异常，返回具体的错误码与提示信息
     *
     * @param e        异常
     * @param request  请求
     * @param response 响应
     * @return 处理后的异常的数据结构
     */
    @ExceptionHandler(LogicException.class)
    public Object logicExceptionHandler(LogicException e, HttpServletRequest request, HttpServletResponse response) {
        RestResult result = new RestResult(false,RestResult.SYSTEM_ERROR_CODE, e.getMessage(), "system.exception");
        result.setCode(e.getCode());
        result.setErrorMessage(e.getErrorMsg());
        return result;
    }
}
