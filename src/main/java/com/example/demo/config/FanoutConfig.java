package com.example.demo.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

//Fanout 类型 发布订阅模式
@Component
public class FanoutConfig {

    /**
     * 定义死信队列相关信息
     */
    public final static String deadQueueName = "dead_queue";
    public final static String deadRoutingKey = "dead_routing_key";
    public final static String deadExchangeName = "dead_exchange";
    /**
     * 死信队列 交换机标识符
     */
    public static final String DEAD_LETTER_QUEUE_KEY = "x-dead-letter-exchange";
    /**
     * 死信队列交换机绑定键标识符
     */
    public static final String DEAD_LETTER_ROUTING_KEY = "x-dead-letter-routing-key";

    // 邮件队列
    private String FANOUT_EMAIL_QUEUE = "fanout_email_queue";

    // 短信队列
    private String FANOUT_SMS_QUEUE = "fanout_sms_queue";
    // fanout 交换机
    private String EXCHANGE_NAME = "fanoutExchange";

    // 1.定义邮件队列
    @Bean
    public Queue fanOutEamilQueue() {
        // 将普通队列绑定到死信队列交换机上
        Map<String, Object> args = new HashMap<>(2);
        args.put(DEAD_LETTER_QUEUE_KEY, deadExchangeName);
        args.put(DEAD_LETTER_ROUTING_KEY, deadRoutingKey);
        Queue queue = new Queue(FANOUT_EMAIL_QUEUE, true, false, false, args);
        return queue;
    }

    // 2.定义短信队列
    @Bean
    public Queue fanOutSmsQueue() {
        return new Queue(FANOUT_SMS_QUEUE);
    }

    // 2.定义交换机
    @Bean
    FanoutExchange fanoutExchange() {
        return new FanoutExchange(EXCHANGE_NAME);
    }

    // 3.队列与交换机绑定邮件队列
    @Bean
    Binding bindingExchangeEamil(Queue fanOutEamilQueue, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(fanOutEamilQueue).to(fanoutExchange);
    }

    // 4.队列与交换机绑定短信队列
    @Bean
    Binding bindingExchangeSms(Queue fanOutSmsQueue, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(fanOutSmsQueue).to(fanoutExchange);
    }

    /**
     * 创建配置死信邮件队列
     *
     * @return
     */
    @Bean
    public Queue deadQueue() {
        Queue queue = new Queue(deadQueueName, true);
        return queue;
    }
    /*
     * 创建死信交换机
     */
    @Bean
    public DirectExchange deadExchange() {
        return new DirectExchange(deadExchangeName);
    }
    /*
     * 死信队列与死信交换机绑定
     */
    @Bean
    public Binding bindingDeadExchange(Queue deadQueue, DirectExchange deadExchange) {
        return BindingBuilder.bind(deadQueue).to(deadExchange).with(deadRoutingKey);
    }

}