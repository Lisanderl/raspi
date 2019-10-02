package org.lisanderl.community.raspi.hardware;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class RGBPinModel {
    private final Pin red;
    private final Pin blue;
    private final Pin green;

    public RGBPinModel(@Value("${raspi.led.red}") String red,
                       @Value("${raspi.led.blue}") String blue,
                       @Value("${raspi.led.green}") String green) {
        this.red = RaspiPin.getPinByName(red);
        this.blue = RaspiPin.getPinByName(blue);
        this.green = RaspiPin.getPinByName(green);
    }
}

