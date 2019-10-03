package org.lisanderl.community.raspi.hardware;

import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.SoftPwm;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.awt.*;

import static org.apache.logging.log4j.LogManager.getLogger;

@Component
@Profile("prod")
public class RGBLed {
    private static final org.apache.logging.log4j.Logger loger = getLogger(RGBLed.class);
    private final RGBPinModel pinModel;

    @Autowired
    public RGBLed(RGBPinModel pinModel) {
        this.pinModel = pinModel;
        loger.info("RGB led model has successfully created " + pinModel);
        this.init();
    }

    private void init() {
        var gpio = GpioFactory.getInstance();

        var gpio1 = gpio.provisionDigitalOutputPin(pinModel.getRedPin());
        gpio1.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        gpio1.low();
        var gpio2 = gpio.provisionDigitalOutputPin(pinModel.getGreenPin());
        gpio2.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        gpio2.low();
        var gpio3 = gpio.provisionDigitalOutputPin(pinModel.getBluePin());
        gpio3.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        gpio3.low();

    }

    public void showColor(Color color) {
        SoftPwm.softPwmCreate(pinModel.getRedPin().getAddress(), 0, 50);
        SoftPwm.softPwmCreate(pinModel.getGreenPin().getAddress(), 0, 50);
        SoftPwm.softPwmCreate(pinModel.getBluePin().getAddress(), 0, 50);
        var colorsArray = color.getRGBColorComponents(null);
        SoftPwm.softPwmWrite(pinModel.getRedPin().getAddress(), (int) (colorsArray[0] * 50.0f));
        SoftPwm.softPwmWrite(pinModel.getGreenPin().getAddress(), (int) (colorsArray[1] * 50.0f));
        SoftPwm.softPwmWrite(pinModel.getBluePin().getAddress(), (int) (colorsArray[2] * 50.0f));
    }

}
