package com.example.demo.deadQueue;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * ClassName DeadMessageListener
 * Description 死信队列监听器
 *
 * @Author wudy
 * @Date 2019/6/4 11:17
 * @Version 1.0
 **/
@Slf4j
@Component
public class DeadMessageListener {

    @RabbitListener(queues = "X-Queue-dead")
    public void process(Message message, @Headers Map<String, Object> headers, Channel channel) throws Exception  {
        System.out.println("================end time:" + System.currentTimeMillis());
        log.info("接收到死信消息：" +message);

        // // 手动ack
        Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        // 手动签收
        channel.basicAck(deliveryTag, false);

    }

}
