package com.example.demo.routing;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zhangchuan
 * @create 2019/6/5 15:03
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RoutingRabbitMqTest {

    @Autowired
    private RoutingProducer routingProducer;

    @Test
    public void test() throws Exception {
        routingProducer.send("routingExchange", "email");
        routingProducer.send("routingExchange", "sms");
    }
}
