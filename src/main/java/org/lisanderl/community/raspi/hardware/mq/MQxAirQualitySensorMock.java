package org.lisanderl.community.raspi.hardware.mq;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class MQxAirQualitySensorMock implements MQxAirQualitySensor {

    @Override
    public AirQuality getAirQuality() {
        return AirQuality.GOOD;
    }

    @Override
    public void readRawValue(int samples) {

    }
}
