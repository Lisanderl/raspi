package org.lisanderl.community.raspi.hardware.dht;

import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.RaspiPin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import static java.util.Arrays.copyOfRange;

@Component
@Profile("prod")
@PropertySource("classpath:application.properties")
public class DHT11Sensor extends DHTxTemperatureSensor {

    @Autowired
    public DHT11Sensor(@Value("${raspi.dht11}") String pinAdr) {
        super(GpioFactory.getInstance().provisionDigitalMultipurposePin(RaspiPin.getPinByAddress(
                Integer.parseInt(pinAdr)), PinMode.DIGITAL_OUTPUT));
    }

    @Override
    public synchronized SensorsData readSensorData(boolean checkData) {
        int[] sensorImpulses = super.readSensorImpulses();
        int[] sensorDataImpulses = copyOfRange(sensorImpulses, 2, 82);
        int[] sensorDataBinary = new int[40];
        int averageLowImpulse = 0;
        for (int i = 0; i < sensorDataImpulses.length; i += 2) {
            averageLowImpulse += sensorDataImpulses[i];
        }
        averageLowImpulse = averageLowImpulse / 40;
        int count = 0;
        for (int i = 1; i < sensorDataImpulses.length; i += 2) {
            sensorDataBinary[count] = sensorDataImpulses[i] > averageLowImpulse ? 1 : 0;
            count++;
        }
        if (checkData) {
            if (isCorrectData(sensorDataBinary)
                    && (sensorDataImpulses[0] > MAX_LOW_HIGH_SENSOR_DELAY && sensorDataImpulses[1] > MAX_LOW_HIGH_SENSOR_DELAY))
                throw new IllegalArgumentException("Wrong sensor check sum");
        }

        int integralRH = convertBinaryArrToInt(copyOfRange(sensorDataBinary, 0, 8));

        int integralT = convertBinaryArrToInt(copyOfRange(sensorDataBinary, 16, 24));
        int decimalT = convertBinaryArrToInt(copyOfRange(sensorDataBinary, 24, 32));
        lastSensorData = new SensorsData(integralT + ((decimalT & 0x0f) * 0.1D), integralRH);

        return lastSensorData;
    }
}
