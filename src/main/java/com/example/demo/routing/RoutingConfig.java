package com.example.demo.routing;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

//Routing模式
@Component
public class RoutingConfig {

    // 邮件队列
    private String ROUTING_EMAIL_QUEUE = "routing_email_queue";

    // 短信队列
    private String ROUTING_SMS_QUEUE = "routing_sms_queue";

    // routing 交换机
    private String ROUTING_EXCHANGE_NAME = "routingExchange";

    // 1.定义邮件队列
    @Bean
    public Queue eamilQueue() {
        Queue queue = new Queue(ROUTING_EMAIL_QUEUE, true, false, false);
        return queue;
    }

    // 2.定义短信队列
    @Bean
    public Queue smsQueue() {
        return new Queue(ROUTING_SMS_QUEUE);
    }

    // 2.定义交换机
    @Bean
    DirectExchange directExchange() {
        return new DirectExchange(ROUTING_EXCHANGE_NAME);
    }

    // 3.队列与交换机绑定邮件队列
    @Bean
    Binding exchangeEamil(Queue eamilQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(eamilQueue).to(directExchange).with("email");
    }

    // 4.队列与交换机绑定短信队列
    @Bean
    Binding exchangeSms(Queue smsQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(smsQueue).to(directExchange).with("sms");
    }

}