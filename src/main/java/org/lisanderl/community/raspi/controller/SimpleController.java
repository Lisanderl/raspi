package org.lisanderl.community.raspi.controller;

import org.lisanderl.community.raspi.hardware.Pad;
import org.lisanderl.community.raspi.hardware.RGBLed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
;
import java.awt.*;


@RestController("/")
public class SimpleController {

    @Autowired
    private RGBLed led;
    @Autowired
    private Pad pad;

    @GetMapping("hello")
    String hello() {
        return "{value: 'Hello'}";
    }

    @GetMapping("rgb/blue")
    void getBlueColor() {
        led.showColor(Color.BLUE);

    }

    @GetMapping("rgb/red")
    void getRedColor() {
        led.showColor(Color.RED);

    }

    @GetMapping("rgb/yellow")
    void getYellowColor() {
        led.showColor(Color.YELLOW);
    }

    @GetMapping("pad")
    void movePad(@RequestParam(name = "x") int coordX, @RequestParam(name = "y") int coordY) {
        pad.horizontalMoveTo(coordX);
        pad.verticalMoveTo(coordY);
    }
}
