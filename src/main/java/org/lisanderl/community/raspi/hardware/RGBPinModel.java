package org.lisanderl.community.raspi.hardware;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import static org.apache.logging.log4j.LogManager.getLogger;

@Data
@Component
@PropertySource("classpath:application.properties")
public class RGBPinModel {
    private final Pin redPin;
    private final Pin bluePin;
    private final Pin greenPin;

    public RGBPinModel(@Value("${raspi.led.red}") int red,
                       @Value("${raspi.led.blue}") int blue,
                       @Value("${raspi.led.green}") int green) {
        this.redPin = RaspiPin.getPinByAddress(red);
        this.bluePin = RaspiPin.getPinByAddress(blue);
        this.greenPin = RaspiPin.getPinByAddress(green);
    }
}

