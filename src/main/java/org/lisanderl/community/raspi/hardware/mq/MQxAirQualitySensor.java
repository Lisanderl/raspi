package org.lisanderl.community.raspi.hardware.mq;

@FunctionalInterface
public interface MQxAirQualitySensor {
    AirQuality getAirQuality();
}
