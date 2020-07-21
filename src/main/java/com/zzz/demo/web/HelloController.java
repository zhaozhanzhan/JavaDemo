package com.zzz.demo.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class HelloController {

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello(String name) {
//        return new HashMap<String, String>(){{
//            put("name", "Hello World:" + name);
//        }};
        String msg = "Hello World：" + name;
        System.out.println(msg);
        return msg;
    }
}
