package org.lisanderl.community.raspi.hardware.dht.exception;

public class SensorStatusException extends IllegalStateException {

    public SensorStatusException() {
        super("Positive lvl at sensor pin");
    }

    public SensorStatusException(String msg) {
        super(msg);
    }
}
