package cn.smallenergy.base.exception;

/**
 * 业务逻辑异常类、
 *
 * @author zhaoxuezhi
 * Created on 2018/9/20-15:22.
 */
public class LogicException extends RuntimeException {
    /**
     * 异常信息
     */
    private String errorMsg;
    /**
     * 错误码
     */
    private String code;

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LogicException(String errorMsg) {
        super(errorMsg);
        this.code = errorMsg.substring(0, 5);
        this.errorMsg = errorMsg.substring(6);
    }

    public LogicException(String code, String errorMsg) {
        super(errorMsg);
        this.code = code;
        this.errorMsg = errorMsg;
    }

}
