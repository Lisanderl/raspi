package org.lisanderl.community.raspi.hardware;

import com.pi4j.component.servo.ServoDriver;
import com.pi4j.component.servo.impl.PCA9685GpioServoProvider;
import com.pi4j.gpio.extension.pca.PCA9685GpioProvider;
import com.pi4j.gpio.extension.pca.PCA9685Pin;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory;
import lombok.extern.log4j.Log4j2;
import lombok.var;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.MessageFormat;

@Log4j2
@Component
@Profile("prod")
public class Pad {
    private static final org.apache.logging.log4j.Logger loger = org.apache.logging.log4j.LogManager.getLogger(Pad.class);
    private static final int PROVIDER_ADR = 0x40;
    private static final int HORIZONTAL_MIN = 600;
    private static final int HORIZONTAL_MAX = 2400;
    private static final int MULTIPLIER = 10;
    private static final int DEFAULT = 1500;

    private final PCA9685GpioProvider PCA9685;
    private final GpioController gpioController;
    private final ServoDriver verticalDrive;
    private final ServoDriver horizontalDrive;

    public Pad() throws IOException {
        var frequency = new BigDecimal("50.00");
        var frequencyCorrectionFactor = new BigDecimal("1.0586");

        I2CBus bus = null;
        try {
            bus = I2CFactory.getInstance(I2CBus.BUS_1);
        } catch (I2CFactory.UnsupportedBusNumberException e) {
            loger.error("Error during connection to I2C " + e);
        }

        gpioController = GpioFactory.getInstance();
        PCA9685 = new PCA9685GpioProvider(bus, PROVIDER_ADR, frequency, frequencyCorrectionFactor);
        var servoProvider = new PCA9685GpioServoProvider(PCA9685);
        var pwmPin0 = gpioController.provisionPwmOutputPin(PCA9685, PCA9685Pin.PWM_00, "Pulse 00");
        pwmPin0.setShutdownOptions(true);
        var pwmPin1 = gpioController.provisionPwmOutputPin(PCA9685, PCA9685Pin.PWM_01, "Pulse 00");
        pwmPin1.setShutdownOptions(true);
        horizontalDrive = servoProvider.getServoDriver(pwmPin0.getPin());
        verticalDrive = servoProvider.getServoDriver(pwmPin1.getPin());
    }

    /***
     *
     * @param y should be not more or less than constants
     * @return true of success
     */
    public boolean verticalMoveTo(int y){

        y = HORIZONTAL_MIN + (y * MULTIPLIER) - 1;

        if(y < HORIZONTAL_MIN || y > HORIZONTAL_MAX) {
            loger.error(MessageFormat.format("Vertical y should be in range from {0} to {1}",
                    HORIZONTAL_MIN, HORIZONTAL_MAX));
            return false;
        }
        verticalDrive.setServoPulseWidth(y);

        return verticalDrive.getServoPulseWidth() == y;
    }

    /***
     *
     * @param x should be not more or less than constants
     * @return true of success
     */
    public boolean horizontalMoveTo(int x){

        x = HORIZONTAL_MIN + (x * MULTIPLIER) - 1;

        if(x < HORIZONTAL_MIN || x > HORIZONTAL_MAX) {
            loger.error(MessageFormat.format("Horizontal x should be in range from {0} to {1}",
                    HORIZONTAL_MIN, HORIZONTAL_MAX));
            return false;
        }
        horizontalDrive.setServoPulseWidth(x);

        return horizontalDrive.getServoPulseWidth() == x;
    }
}
