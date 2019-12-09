package org.lisanderl.community.raspi.hardware.dht;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component("myBean")
@Profile("dev")
public class DHT11SensorMock implements DHTxSensor {

    @Override
    public SensorsData readSensorData(boolean checkData) {
        return new SensorsData(22.8, 33.5);
    }

    @Override
    public SensorsData getLastCorrectMeasure() {
        return new SensorsData(22.8, 33.5);
    }
}
