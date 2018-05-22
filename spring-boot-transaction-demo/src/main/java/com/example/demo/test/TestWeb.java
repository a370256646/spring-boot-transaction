package com.example.demo.test;

import com.lc.transaction.processor.SendEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

/**
 * 测试入口
 *
 * @author liucheng
 * @create 2018-05-18 17:41
 **/
@RestController
public class TestWeb {
    @Autowired
    SendEvent sendEvent;

    @RequestMapping("/send/{expects}")
    public void send(@PathVariable("expects") String expects) {
        sendEvent.send("test", null, Collections.singleton(expects));
    }
}
