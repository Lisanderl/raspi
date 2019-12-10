package org.lisanderl.community.raspi.hardware.mq;

import java.io.IOException;

public interface MQxAirQualitySensor {
    void readRawValue(int samples) throws IOException;

    AirQuality getAirQuality();
}
