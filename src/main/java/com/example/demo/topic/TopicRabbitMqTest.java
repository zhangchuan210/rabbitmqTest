package com.example.demo.topic;

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
public class TopicRabbitMqTest {

    @Autowired
    private TopicSender topicSender;

    @Test
    public void hello() throws Exception {
        topicSender.send1();
        topicSender.send2();
    }
}
