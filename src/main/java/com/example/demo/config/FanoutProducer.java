package com.example.demo.config;

import java.util.UUID;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

@Component
public class FanoutProducer {
    @Autowired
    private AmqpTemplate amqpTemplate;

    public void send(String queueName) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email", "xx@163.com");
        jsonObject.put("timestamp", 0);  // 时间戳为0，导致后续正常消费抛异常
        String jsonString = jsonObject.toJSONString();
        System.out.println("jsonString:" + jsonString);
        // 设置消息唯一id 保证每次重试消息id唯一
        Message message = MessageBuilder.withBody(jsonString.getBytes())
                .setContentType(MessageProperties.CONTENT_TYPE_JSON).setContentEncoding("utf-8")
                .setMessageId(UUID.randomUUID() + "").build(); //消息id设置在请求头里面 用UUID做全局ID
        amqpTemplate.convertAndSend(queueName, message);
    }
}