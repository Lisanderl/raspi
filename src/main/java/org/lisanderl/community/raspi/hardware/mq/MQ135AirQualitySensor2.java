package org.lisanderl.community.raspi.hardware.mq;

import com.pi4j.gpio.extension.ads.ADS1115GpioProvider;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Profile("prod")
@PropertySource("classpath:application.properties")
@Log4j2
public class MQ135AirQualitySensor2 implements MQxAirQualitySensor {
    private static final int READ_DELAY = 20;
    private final I2CDevice i2CDevice;

    @Getter
    private double sensorData;

    public MQ135AirQualitySensor2(@Value("${raspi.I2Cbus}") String i2CbusAddr) throws IOException, I2CFactory.UnsupportedBusNumberException {
        i2CDevice = I2CFactory.getInstance(Integer.parseInt(i2CbusAddr)).getDevice(ADS1115GpioProvider.ADS1115_ADDRESS_0x48);

        log.info("MQ135AirQualitySensor has created");

    }

    @Override
    public AirQuality getAirQuality() {

        return AirQuality.getAirQualityByAnalogValue((int) sensorData);
    }

    @Override
    public void readRawValue(int samples) {
        try {
            log.info("Address: " + i2CDevice.getAddress());
            log.info(i2CDevice.read(0x01));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
