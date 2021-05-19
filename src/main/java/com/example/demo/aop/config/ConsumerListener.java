package com.example.demo.aop.config;

import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @ClassName: Consumer
 * @Description:
 * @Author: lixl
 * @Date: 2021/4/27 16:53
 */
@Component
@RocketMQMessageListener(topic = "topic-test", selectorExpression = "nice", consumerGroup = "testgroup",consumeMode = ConsumeMode.ORDERLY)
public class ConsumerListener implements RocketMQListener<String> {

    private static final Logger logger = LoggerFactory.getLogger(ConsumerListener.class);

    @Override
    public void onMessage(String s) {
        logger.info("消费消息:{}", s);
    }
}
