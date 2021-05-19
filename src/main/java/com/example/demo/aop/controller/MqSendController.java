package com.example.demo.aop.controller;

import com.example.demo.aop.config.ErrorAnnotation;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: mqSendController
 * @Description:
 * @Author: lixl
 * @Date: 2021/4/27 16:55
 */
@RestController
@RequestMapping("/rocketmq")
public class MqSendController {


    @Autowired
    private RocketMQTemplate rocketMQTemplate;


    @RequestMapping("/send/aop")
    @ErrorAnnotation(value = "默认值.......................")
    public String aop(String info,Integer count) {
        System.out.println("方法调用 throw:"+info+"---"+count+"----");
        if(1==1) throw new IndexOutOfBoundsException();
        return "success aop";
    }

    @RequestMapping("/send/aop2")
    public String aop2(Exception e) {
        System.out.println("方法调用 throw II:"+e);
        return "success aop";
    }

    @RequestMapping("/send/message/{tag}")
    public String sendMessage(@PathVariable("tag") String tag) {
        //顺序消费通过hashKey来确定他们在哪个queue
        for(int i=0;i<10;i++){
            rocketMQTemplate.syncSendOrderly("topic-test:" + tag,"order:"+String.valueOf(i),"order");
        }

        //顺序消费通过hashKey来确定他们在哪个queue
        for(int i=0;i<10;i++){
            rocketMQTemplate.syncSendOrderly("topic-test:" + tag,"orderOne:"+String.valueOf(i),"orderOne");
        }
        // 正常发送消费
        for(int i=0;i<10;i++){
            rocketMQTemplate.convertAndSend("topic-test:" + tag, "order-normal:"+String.valueOf(i));
        }

        return "success";
    }
}
