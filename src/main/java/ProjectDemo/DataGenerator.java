package ProjectDemo;

import Protocol.CoapClient;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class DataGenerator extends Thread {
    private CoapClient client;
    private int sensor_id;
    private long interval;
    private boolean running;

    private static final Random generator = new Random();
    private static final ObjectMapper mapper = new ObjectMapper();
    public DataGenerator(int id, CoapClient client, long interval) {
        this.sensor_id = id;
        this.client = client;
        this.interval = interval;
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            try {
                sendData();
                TimeUnit.MILLISECONDS.sleep(interval);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    private synchronized void sendData() {
        SensorMessage message = new SensorMessage();
        message.setSensor_id(this.sensor_id);
        message.setTemperate(generator.nextInt(30) + 10);
        message.setTime_send(System.currentTimeMillis());
        try {
            String jsonMessage = mapper.writeValueAsString(message);
            client.post(jsonMessage.getBytes());
        } catch (JacksonException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public int getSensor_id() {
        return sensor_id;
    }

    public void setSensor_id(int id) {
        this.sensor_id = id;
    }

    public CoapClient getClient() {
        return client;
    }

    public void setClient(CoapClient client) {
        this.client = client;
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
