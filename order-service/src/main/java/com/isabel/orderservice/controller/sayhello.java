package com.isabel.orderservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RequestMapping("api")
@RestController
public class sayhello {


    @GetMapping
    public String getMethodName(@RequestParam(required=false)  String param) {
        return "Hello Developer";
    }
    
    
}
