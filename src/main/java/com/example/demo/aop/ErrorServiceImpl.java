package com.example.demo.aop;

import com.example.demo.aop.config.ErrorAnnotation;
import org.springframework.stereotype.Service;

/**
 * @ClassName: ErrorServiceImpl
 * @Description:
 * @Author: lixl
 * @Date: 2021/4/29 17:32
 */
@Service
public class ErrorServiceImpl implements ErrorService {
    @Override
    public String message(String info, Integer count) {
        return "point aop error"+info+count;
    }

    @Override
    @ErrorAnnotation("message2")
    public String message2(String info, Integer count) {
        return "point aop2 error"+info+count;
    }
}
