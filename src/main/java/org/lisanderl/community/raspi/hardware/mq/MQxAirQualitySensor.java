package org.lisanderl.community.raspi.hardware.mq;

public interface MQxAirQualitySensor {
    void readRawValue(int samples);

    AirQuality getAirQuality();
}
