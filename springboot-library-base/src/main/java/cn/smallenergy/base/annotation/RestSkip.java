package cn.smallenergy.base.annotation;

import java.lang.annotation.*;

/**
 * @author futao
 * Created on 2019-05-22.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RestSkip {
}
