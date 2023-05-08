package ProjectDemo.SensorSimulator;

import ProjectDemo.Message.SensorMessage;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ArrayList<TemperatureSensor> sensors = new ArrayList<>();

        int n = 30;

        try {
            for (int i = 0; i < n; i++) {
                sensors.add(new TemperatureSensor(i,5000));
                sensors.get(i).connect("coap//localhost:5683");
                sensors.get(i).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
