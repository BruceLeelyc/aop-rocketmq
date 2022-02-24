package com.example.demo.aop.config;

import com.example.demo.aop.pojo.BaseCondition;
import com.example.demo.aop.pojo.UserDto;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description:
 * @author: lixl
 * @date: 2022.02.23
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ParamsAnnotation {

  String value() default "default";

  Class<? extends UserDto> user() default UserDto.class;

  Class<? extends BaseCondition> condition() default BaseCondition.class;

  enum AnnotationBean{
    USER_SELF(new UserDto())
    ;

    private final Object implementation;

    AnnotationBean(Object implementation) {
      this.implementation = implementation;
    }

    @Deprecated
    public AnnotationBean get() {
      return this;
    }
  }
}
