package org.lisanderl.community.raspi.controller;

import org.lisanderl.community.raspi.hardware.RGBLed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;

@RestController("/")
public class SimpleController {

    @Autowired
    private RGBLed led;

    @GetMapping("hello")
    String hello() {
        return "{value: 'Hello'}";
    }

    @GetMapping("rgb/{color}")
    String getColor(@PathVariable("color") String color) {
        led.showColor(Color.getColor(color));

        return "Set color to " + color;
    }
}
