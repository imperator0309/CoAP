package ProjectDemo.SensorSimulator;

import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        TemperatureSensor sensor = new TemperatureSensor(1, 5000);
        sensor.connect("coap//localhost:5683");

        TemperatureSensor sensor1 = new TemperatureSensor(2, 5000);
        sensor1.connect("coap//localhost:5683");

        TemperatureSensor sensor2 = new TemperatureSensor(3, 5000);
        sensor2.connect("coap//localhost:5683");
        try {
            sensor.start();
            TimeUnit.MILLISECONDS.sleep(1500);
            sensor1.start();
            TimeUnit.MILLISECONDS.sleep(1500);
            sensor2.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
