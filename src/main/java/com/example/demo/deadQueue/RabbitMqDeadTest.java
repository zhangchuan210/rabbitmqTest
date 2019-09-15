package com.example.demo.deadQueue;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * @author zhangchuan
 * @create 2019/6/5 15:03
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitMqDeadTest {

    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private ConnectionFactory connectionFactory;

    @Test
    public void dead() throws Exception {

        // 1. 队列设置过期时间
        /*String message = "zc test";
        amqpTemplate.convertAndSend("X-Exchange-ttl","ttl", message);
        System.out.println("================begin time:" + System.currentTimeMillis());*/

        // 2. 消息设置过期时间
        try {
            Connection connection = connectionFactory.createConnection();
            Channel channel = connection.createChannel(true);
            AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
            builder.deliveryMode(2);//持久化消息
            builder.expiration("5000");//设置过期时间
            AMQP.BasicProperties properties = builder.build();
            // 发送消息
            channel.basicPublish("X-Exchange-ttl", "ttl", properties, "ttltest".getBytes());
            channel.txCommit();
            System.out.println("================begin time:" + System.currentTimeMillis());

            // 关闭频道和连接
            channel.close();
            connection.close();

            Thread.sleep(10000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
