package org.lisanderl.community.raspi.hardware.mq;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("prod")
public class MQ135AirQualitySensor implements MQxAirQualitySensor {
    @Override
    public AirQuality getAirQuality() {
        return null;
    }
}
