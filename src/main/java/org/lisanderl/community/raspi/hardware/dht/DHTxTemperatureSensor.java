package org.lisanderl.community.raspi.hardware.dht;

import com.pi4j.io.gpio.GpioPinDigitalMultipurpose;
import com.pi4j.io.gpio.PinState;
import com.pi4j.wiringpi.Gpio;
import org.lisanderl.community.raspi.hardware.dht.exception.SensorStatusException;

import java.util.Objects;

import static com.pi4j.io.gpio.PinMode.DIGITAL_INPUT;
import static com.pi4j.io.gpio.PinMode.DIGITAL_OUTPUT;
import static com.pi4j.io.gpio.PinState.HIGH;
import static com.pi4j.io.gpio.PinState.LOW;
import static java.util.Arrays.copyOfRange;

public abstract class DHTxTemperatureSensor implements DHTxSensor {
    protected static final int MAX_LOW_HIGH_SENSOR_DELAY = 90;
    private final GpioPinDigitalMultipurpose pin;
    protected SensorsData lastSensorData;
    private int firstReadImpulseMillisecondsDelay = 100;
    private int secondReadImpulseMillisecondsDelay = 20;
    private int sensorReadStateInitDelay = 15;
    private int tryReadOneBitAttempts = 4;
    /**
     * Magic value, should be adjusted. At raspi3 and normal CPU load
     * 12000 means ~ 120-140 microseconds
     */
    private int readOneBitMaxDelay = 12000;


    protected DHTxTemperatureSensor(GpioPinDigitalMultipurpose pin) {
        this.pin = pin;
        lastSensorData = new SensorsData(0.0, 0.0);
    }

    protected DHTxTemperatureSensor(GpioPinDigitalMultipurpose pin, int firstReadImpulseMillisecondsDelay,
                                    int secondReadImpulseMillisecondsDelay, int sensorReadStateInitDelay,
                                    int tryReadOneBitAttempts, int readOneBitMaxDelay) {
        this(pin);
        this.firstReadImpulseMillisecondsDelay = firstReadImpulseMillisecondsDelay;
        this.secondReadImpulseMillisecondsDelay = secondReadImpulseMillisecondsDelay;
        this.sensorReadStateInitDelay = sensorReadStateInitDelay;
        this.tryReadOneBitAttempts = tryReadOneBitAttempts;
        this.readOneBitMaxDelay = readOneBitMaxDelay;
    }

    public static int convertBinaryArrToInt(int[] arr) {
        int multyplier = 0;
        int result = 0;
        for (int i = arr.length - 1; i >= 0; i--) {
            result += (arr[i] << multyplier++);
        }
        return result;
    }

    public static boolean isCorrectData(int[] arr) {
        if (Objects.isNull(arr) || arr.length < 40) {
            return false;
        }
        int checkSum = convertBinaryArrToInt(copyOfRange(arr, 32, 40));
        int integralRH = convertBinaryArrToInt(copyOfRange(arr, 0, 8));
        int decimalRH = convertBinaryArrToInt(copyOfRange(arr, 8, 16));
        int integralT = convertBinaryArrToInt(copyOfRange(arr, 16, 24));
        int decimalT = convertBinaryArrToInt(copyOfRange(arr, 24, 32));

        return checkSum == ((integralRH + decimalRH + integralT + decimalT) & 0xFF);
    }

    public double getTemperature() {

        return lastSensorData.getTemperature();
    }

    public double getHumidity() {

        return lastSensorData.getHumidity();
    }


    /**
     * @return array of 82 sensor impulses
     * First two it's default sensor response
     * of low and high signal for 80 millis
     */
    protected synchronized int[] readSensorImpulses() {
        int[] rawData = new int[82];
        int readRequestCount = 0;

        while (!readRequest()) {
            if (++readRequestCount == 5)
                throw new SensorStatusException("Can't complete read request");
        }

        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        for (int i = 0; i < rawData.length; i += 2) {
            rawData[i] = captureSignal(LOW, tryReadOneBitAttempts);
            rawData[i + 1] = captureSignal(HIGH, tryReadOneBitAttempts);
        }
        Thread.currentThread().setPriority(Thread.NORM_PRIORITY);

        return rawData;
    }

    private boolean readRequest() {
        pin.setMode(DIGITAL_OUTPUT);
        pin.setState(HIGH);
        Gpio.delay(firstReadImpulseMillisecondsDelay);

        pin.setState(LOW);
        Gpio.delay(secondReadImpulseMillisecondsDelay);

        pin.setMode(DIGITAL_INPUT);
        Gpio.delayMicroseconds(sensorReadStateInitDelay);

        return pin.isLow() || !pin.isHigh(); // just try to check signal one more time )
    }

    private int captureSignal(PinState state, int attempt) {
        PinState reversedState = state.isHigh() ? LOW : HIGH;
        int result = -1;
        long startMicroSeconds = Gpio.micros();
        while (pin.isState(state) || !pin.isState(reversedState)) {

            if (++result > readOneBitMaxDelay) {

                return 0;
            }
        }

        return (--attempt <= 0 || result > 0)
                ? (int) (Gpio.micros() - startMicroSeconds)
                : captureSignal(state, attempt);
    }
}
