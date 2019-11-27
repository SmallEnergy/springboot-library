package cn.smallenergy.anno;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD , ElementType.TYPE})
public @interface OperateLog {
    String value() default  "first one";
}
