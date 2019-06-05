package com.example.demo.topic;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author zhangchuan
 * @create 2019/6/5 15:02
 */

@Component
@RabbitListener(queues = "topic.message")
public class TopicReceiver {

    @RabbitHandler
    public void process(String hello) {
        System.out.println("topic.message Receiver  : " + hello);
    }

}
