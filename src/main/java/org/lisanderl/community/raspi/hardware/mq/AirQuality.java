package org.lisanderl.community.raspi.hardware.mq;

import lombok.Getter;

@Getter
public enum AirQuality {
    GOOD(100),
    OK(200),
    NORMAL(300),
    BAD(400),
    FAIRLY_BAD(500),
    UNKNOWN(0);

    private int analogValue;

    AirQuality(int analogValue) {
        this.analogValue = analogValue;
    }

    public static AirQuality getAirQualityByAnalogValue(int analogValue) {
        switch (analogValue) {
            case 100:
                return GOOD;
            case 200:
                return OK;
            case 300:
                return NORMAL;
            case 400:
                return BAD;
            case 500:
                return FAIRLY_BAD;
            default:
                return UNKNOWN;
        }
    }
}
