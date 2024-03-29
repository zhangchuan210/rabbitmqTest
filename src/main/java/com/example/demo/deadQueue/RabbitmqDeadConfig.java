package com.example.demo.deadQueue;

import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName RabbitmqConfig
 * Description TODO
 * rabbitmq 配置中心
 * 注意事项：
 * 1.本项目共有两个交换机（exchange），并且是直连（direct）交换机，如有需要，可以自己替换成topic交换机
 * 2。也可以只有一个交换机，将死信队列x-queue-dead绑定到这个唯一的交换机，但要注意路由（routingkey）
 * 3。当其中的durable参数设置成true时，要注意重启项目时，项目中的配置会不会和rabbitmq server已经持久化的配置冲突，不然会报错，解决方案有两种：
 *  a:登陆rabbitmq的后台，删除对应的配置
 *  b:重命名配置名称
 * 4.为了模拟过期死信路由，可以不设置x-queue-dead的监听事件以达到模拟超时转发死信的过程，即注释TtlMessageListener的@service注解，这样就不会生效
 * 5。为了模拟拒绝并且不投回原消息队列情况下的私信路由，可以启用x-queue-dead的监听事件，连接工厂要设置rabbitmq的应答模式为手动确认（AcknowledgeMode.MANUAL），并且测试参数为reject
 * @Author wudy
 * @Date 2019/6/4 10:10
 * @Version 1.0
 **/
@Configuration
public class RabbitmqDeadConfig {
    @Autowired
    private ConnectionFactory connectionFactory;

    /**
     * @Author wudy
     * @Description 过期交换机，直连模式，也可以设置成topic模式，需要设置好路由规则（routingKey）
     * @Date 13:24 2019/6/4
     * @Param []
     * @return org.springframework.amqp.core.DirectExchange
     **/
    @Bean
    DirectExchange ttlExchange() {
        return new DirectExchange("X-Exchange-ttl",true,false);
    }

    /**
     * @Author wudy
     * @Description 死信交换机，直连模式，可以为topic模式
     * @Date 13:26 2019/6/4
     * @Param []
     * @return org.springframework.amqp.core.DirectExchange
     **/
    @Bean
    DirectExchange deadLetterExchange() {
        return new DirectExchange("X-Exchange-dead",true,false);
    }

    /**
     * @Author wudy
     * @Description 死信队列
     * @Date 13:27 2019/6/4
     * @Param []
     * @return org.springframework.amqp.core.Queue
     **/
    @Bean
    Queue deadQueue() {
        return new Queue("X-Queue-dead");
    }

    /**
     * @Author wudy
     * @Description 绑定过期队列到过期交换机
     * @Date 13:27 2019/6/4
     * @Param [ttlExchange, ttlQueue]
     * @return org.springframework.amqp.core.Binding
     **/
    @Bean
    Binding ttlQueueBinding(DirectExchange ttlExchange, Queue ttlQueue) {
        return BindingBuilder.bind(ttlQueue).to(ttlExchange).with("ttl");
    }

    /**
     * @Author wudy
     * @Description 绑定死信队列到死信交换机
     * @Date 13:28 2019/6/4
     * @Param [deadLetterExchange, deadQueue]
     * @return org.springframework.amqp.core.Binding
     **/
    @Bean
    Binding deadQueueBinding(DirectExchange deadLetterExchange, Queue deadQueue) {
        return BindingBuilder.bind(deadQueue).to(deadLetterExchange).with("dead");
    }

    /**
     * @Author wudy
     * @Description 过期队列
     * @Date 13:29 2019/6/4
     * @Param []
     * @return org.springframework.amqp.core.Queue
     **/
    @Bean
    Queue ttlQueue() {
        Map<String,Object> args = new HashMap<>();
        //以下参数配置具体查看官方文档或者到rabbit管理后台添加queue中查看
        //配置死信队列的交换机
        args.put("x-dead-letter-exchange","X-Exchange-dead");
        //配置死信队列的路由routingKey
        args.put("x-dead-letter-routing-key","dead");
        //设置过期时间
        args.put("x-message-ttl",8000);
        return new Queue("X-Queue-ttl",true,false,false,args);
    }

    /**
     * @Author wudy
     * @Description 配置rabbitTemplate
     * @Date 13:32 2019/6/4
     * @Param []
     * @return org.springframework.amqp.rabbit.core.RabbitTemplate
     **/
    @Bean
    RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        //配置消息转化器，如果需要在消息通道channel中传输可序列化对象，需要此配置
//        rabbitTemplate.setMessageConverter(new HessianMessageConverter());
        return rabbitTemplate;
    }

    /**
     * @Author wudy
     * @Description 配置监听器工厂
     * @Date 13:34 2019/6/4
     * @Param []
     * @return org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory
     **/
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        //开启手动 ack，测试一个消息被Consumer拒收了，并且reject方法的参数里requeue是false。也就是说不会被再次放在队列里，被其他消费者使用
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        //配置消息转化器，如果需要在消息通道channel中传输可序列化对象，需要此配置
//        factory.setMessageConverter(new HessianMessageConverter());
        return factory;
    }
}
