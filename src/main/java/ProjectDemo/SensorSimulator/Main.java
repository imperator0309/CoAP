package ProjectDemo.SensorSimulator;

public class Main {
    public static void main(String[] args) {
        TemperatureSensor sensor = new TemperatureSensor(1, 5000);
        sensor.connect("coap//localhost:5683");
        sensor.start();
    }
}
