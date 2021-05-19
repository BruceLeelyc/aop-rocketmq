package com.example.demo.aop.config;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface LoginfoAnnotation {
    String value() default "default";
}
