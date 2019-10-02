package org.lisanderl.community.raspi.hardware;

import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
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

    private void init(){
        var gpio = GpioFactory.getInstance();

        gpio.provisionDigitalOutputPin(pinModel.getRedPin())
                .setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        gpio.provisionDigitalOutputPin(pinModel.getGreenPin())
                .setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        gpio.provisionDigitalOutputPin(pinModel.getBluePin())
                .setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);

        SoftPwm.softPwmCreate(pinModel.getRedPin().getAddress(), 0, 50);
        SoftPwm.softPwmCreate(pinModel.getGreenPin().getAddress(), 0, 50);
        SoftPwm.softPwmCreate(pinModel.getBluePin().getAddress(), 0, 50);

        this.showColor(Color.BLACK);
    }

    public void showColor(Color color){
    var colorsArray = color.getRGBColorComponents(new float[3]);
    SoftPwm.softPwmWrite(pinModel.getRedPin().getAddress(), (int) colorsArray[0]);
    SoftPwm.softPwmWrite(pinModel.getGreenPin().getAddress(), (int) colorsArray[1]);
    SoftPwm.softPwmWrite(pinModel.getBluePin().getAddress(), (int) colorsArray[2]);
    }

}
