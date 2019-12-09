package org.lisanderl.community.raspi.hardware.dht;

public interface DHTxSensor {
    SensorsData readSensorData(boolean checkData);

    SensorsData getLastCorrectMeasure();
}
