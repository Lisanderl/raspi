package org.lisanderl.community.raspi.hardware.dht;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SensorsData {
    private double temperature;
    private double humidity;
}
