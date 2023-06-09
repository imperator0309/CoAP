package ProjectDemo.SensorSimulator;

import ProjectDemo.Message.SensorMessage;
import Protocol.CoapClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class DataGenerator extends Thread {
    private int sensor_id;
    private CoapClient client;
    private final long time_interval;
    private boolean running;
    private final ObjectMapper mapper = new ObjectMapper();
    private final Random generator = new Random();

    public DataGenerator(int sensor_id, CoapClient client, long time_interval) {
        this.sensor_id = sensor_id;
        this.client = client;
        this.time_interval = time_interval;
        running = false;
    }

    @Override
    public  void run() {
        while (true) {
            try {
                generateData();
                TimeUnit.MILLISECONDS.sleep(time_interval);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void generateData() {
        try {
            if (isRunning()) {
                SensorMessage message = new SensorMessage(this.sensor_id, generator.nextInt(30) + 10,
                        System.nanoTime());
                String json = mapper.writeValueAsString(message);
                client.post(json.getBytes());
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public int getSensor_id() {
        return sensor_id;
    }

    public void setSensor_id(int sensor_id) {
        this.sensor_id = sensor_id;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public CoapClient getClient() {
        return client;
    }

    public void setClient(CoapClient client) {
        this.client = client;
    }
}
