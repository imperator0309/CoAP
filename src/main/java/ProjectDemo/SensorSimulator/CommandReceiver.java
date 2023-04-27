package ProjectDemo.SensorSimulator;

import CoAPException.CoapClientException;
import ProjectDemo.Message.CommandMessage;
import Protocol.CoapClient;
import Protocol.ObserveHandler;
import Protocol.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CommandReceiver extends Thread {
    private final TemperatureSensor sensor;
    private CoapClient client;

    private static ObjectMapper mapper = new ObjectMapper();

    public CommandReceiver(TemperatureSensor sensor, CoapClient client) {
        this.sensor = sensor;
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
                if (sensor.getDataGenerator().isRunning()) {
                    sensor.getDataGenerator().setRunning(false);
                }
            }
            case RESUME -> {
                if (!sensor.getDataGenerator().isRunning()) {
                    sensor.getDataGenerator().setRunning(true);
                }
            }
        }
    }

    public TemperatureSensor getSensor() {
        return sensor;
    }

    public CoapClient getClient() {
        return client;
    }

    public void setClient(CoapClient client) {
        this.client = client;
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
                        if (command.getSensor_id() == receiver.getSensor().getSensor_id()) {
                            this.receiver.changeSensorState(command);
                        }
                    } catch (JsonProcessingException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }
}
