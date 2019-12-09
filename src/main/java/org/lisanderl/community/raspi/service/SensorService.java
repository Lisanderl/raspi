package org.lisanderl.community.raspi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.lisanderl.community.raspi.hardware.dht.DHTxSensor;
import org.lisanderl.community.raspi.hardware.mq.MQxAirQualitySensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@PropertySource("classpath:application.properties")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SensorService {
    private final DHTxSensor temperatureSensor;
    private final MQxAirQualitySensor airQualitySensor;

    @Scheduled(fixedRateString = "${rasp.sensor.job.rate}")
    public void checkSensors() {
        log.info(
                temperatureSensor.readSensorData(true));
        airQualitySensor.getAirQuality();
    }
}
