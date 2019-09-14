package com.example.demo.fanout;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zhangchuan
 * @create 2019/6/5 15:03
 */

/**
 *  RabbitMQ 死信队列
 *  https://www.cnblogs.com/toov5/p/10288260.html
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class FanoutRabbitMqTest {

    @Autowired
    private FanoutProducer fanoutProducer;

    @Test
    public void hello() throws Exception {
        fanoutProducer.send("fanoutExchange");
    }
}
