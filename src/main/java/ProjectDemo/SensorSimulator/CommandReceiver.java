package ProjectDemo.SensorSimulator;

import CoAPException.CoapClientException;
import ProjectDemo.Message.CommandMessage;
import Protocol.CoapClient;
import Protocol.ObserveHandler;
import Protocol.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CommandReceiver extends Thread {
    private int sensor_id;
    private DataGenerator dataGenerator;
    private CoapClient client;

    private static ObjectMapper mapper = new ObjectMapper();

    public CommandReceiver(int sensor_id, DataGenerator dataGenerator, CoapClient client) {
        this.sensor_id = sensor_id;
        this.dataGenerator = dataGenerator;
        this.client = client;
    }

    @Override
    public void run() {
        client.setObserveHandler(new CommandListener(client, this));
        try {
            client.observe();
        } catch (CoapClientException ex) {
            ex.printStackTrace();
        }
    }

    private void changeSensorState(CommandMessage message) {
        switch (message.getCommand()) {
            case SUSPEND -> {
                if (this.dataGenerator.isRunning()) {
                    try {
                        this.dataGenerator.wait();
                        this.dataGenerator.setRunning(false);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            case RESUME -> {
                if (!this.dataGenerator.isRunning()) {
                    if (this.dataGenerator.isAlive()) {
                        this.dataGenerator.setRunning(true);
                        notifyAll();
                    } else {
                        this.dataGenerator.setRunning(true);
                        this.dataGenerator.start();
                    }
                }
            }
        }
    }

    public int getSensor_id() {
        return sensor_id;
    }

    public void setSensor_id(int sensor_id) {
        this.sensor_id = sensor_id;
    }

    public CoapClient getClient() {
        return client;
    }

    public void setClient(CoapClient client) {
        this.client = client;
    }

    public DataGenerator getDataGenerator() {
        return dataGenerator;
    }

    public void setDataGenerator(DataGenerator dataGenerator) {
        this.dataGenerator = dataGenerator;
    }

    public static class CommandListener extends ObserveHandler {
        private CommandReceiver receiver;
        public CommandListener(CoapClient client, CommandReceiver receiver) {
            super(client);
            this.receiver = receiver;
        }

        @Override
        public synchronized void observation() {
            super.observation();
            Response response = getResponse();
            if (response != null) {
                if (response.getPayload() != null) {
                    String json = new String(response.getPayload());
                    try {
                        CommandMessage command = mapper.readValue(json, CommandMessage.class);
                        if (command.getSensor_id() == this.receiver.getSensor_id()) {

                        }
                    } catch (JsonProcessingException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }
}
