package com.example.demo.aop.config;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface ErrorAnnotation {
    String value() default "default";
}
