package org.lisanderl.community.raspi.hardware.dht;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SensorsData {
    private double temperature;
    private double humidity;

    @Override
    public String toString() {
        return "Temperature = " + temperature +
                "\\n Humidity = " + humidity;
    }
}
