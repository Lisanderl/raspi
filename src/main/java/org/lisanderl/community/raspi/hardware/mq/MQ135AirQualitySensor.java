package org.lisanderl.community.raspi.hardware.mq;

import com.pi4j.gpio.extension.ads.ADS1115GpioProvider;
import com.pi4j.gpio.extension.ads.ADS1115Pin;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.wiringpi.Gpio;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.IOException;

@Component
@Profile("prod")
@PropertySource("classpath:application.properties")
public class MQ135AirQualitySensor implements MQxAirQualitySensor {
    private static final int READ_DELAY = 20;
    private final ADS1115GpioProvider analogGpioProvider;
    private final Pin sensorPin;
    @Getter
    private double sensorData;

    public MQ135AirQualitySensor(@Value("${raspi.I2Cbus}") String I2Cbus) throws IOException, I2CFactory.UnsupportedBusNumberException {

        System.out.println("Device is: " +
                I2CFactory.getInstance(Integer.parseInt(I2Cbus)).getDevice(ADS1115GpioProvider.ADS1115_ADDRESS_0x4A));

        System.out.println("Device is: " +
                I2CFactory.getInstance(Integer.parseInt(I2Cbus)).getDevice(ADS1115GpioProvider.ADS1115_ADDRESS_0x4B));

        System.out.println("Device is: " +
                I2CFactory.getInstance(Integer.parseInt(I2Cbus)).getDevice(ADS1115GpioProvider.ADS1115_ADDRESS_0x48));

        System.out.println("Device is: " +
                I2CFactory.getInstance(Integer.parseInt(I2Cbus)).getDevice(ADS1115GpioProvider.ADS1115_ADDRESS_0x49));


        this.analogGpioProvider = new ADS1115GpioProvider(Integer.parseInt(I2Cbus), ADS1115GpioProvider.ADS1115_ADDRESS_0x4B);
        this.sensorPin = ADS1115Pin.INPUT_A0;
        analogGpioProvider.setMode(sensorPin, PinMode.ANALOG_INPUT);
    }

    @Override
    public AirQuality getAirQuality() {

        return AirQuality.getAirQualityByAnalogValue((int) sensorData);
    }

    @Override
    public void readRawValue(int samples) {
        double result = 0.0d;
        for (int i = 0; i < samples; i++) {
            result += analogGpioProvider.getValue(sensorPin);
            Gpio.delay(READ_DELAY);
        }
        sensorData = result / samples;
    }

    @PreDestroy
    private void postConstructor() {
        analogGpioProvider.shutdown();
    }


}
