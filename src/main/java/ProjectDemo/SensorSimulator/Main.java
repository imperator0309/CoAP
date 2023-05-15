package ProjectDemo.SensorSimulator;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        ArrayList<TemperatureSensor> sensors = new ArrayList<>();

        Scanner sc = new Scanner(System.in);
        System.out.print("Type the initial number of sensors: ");
        int n = sc.nextInt();

        try {
            for (int i = 0; i < n; i++) {
                sensors.add(new TemperatureSensor(i,5000));
                sensors.get(i).connect("coap://localhost:5683");
                sensors.get(i).start();
                TimeUnit.MILLISECONDS.sleep(500);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
