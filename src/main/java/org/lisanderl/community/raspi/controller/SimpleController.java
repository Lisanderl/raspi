package org.lisanderl.community.raspi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
public class SimpleController {

    @GetMapping("hello")
    String hello(){
        return "{value: 'Hello'}";
    }
}
