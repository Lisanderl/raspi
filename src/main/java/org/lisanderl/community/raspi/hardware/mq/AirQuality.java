package org.lisanderl.community.raspi.hardware.mq;

import lombok.Getter;

import static java.util.Arrays.stream;

@Getter
public enum AirQuality {
    PERFECT(900),
    GOOD(1500),
    OK(2000),
    NORMAL(2500),
    BAD(3000),
    FAIRLY_BAD(4000),
    UNKNOWN(0);

    private int analogValue;

    AirQuality(int analogValue) {
        this.analogValue = analogValue;
    }

    public static AirQuality getAirQualityByAnalogValue(int analogValue) {

        return stream(AirQuality.values()).reduce((v1, v2) ->
                v1.getAnalogValue() >= analogValue
                        ? v1 : v2
        ).orElse(UNKNOWN);
    }

    @Override
    public String toString() {
        return "Air Quality is " + this.name();
    }
}
