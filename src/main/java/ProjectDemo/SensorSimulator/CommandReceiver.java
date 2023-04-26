package ProjectDemo.SensorSimulator;

import Protocol.CoapClient;
import Protocol.ObserveHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CommandReceiver extends Thread {
    private DataGenerator dataGenerator;
    private CoapClient client;

    private final ObjectMapper mapper = new ObjectMapper();

    public CommandReceiver(CoapClient client, DataGenerator dataGenerator) {
        this.client = client;
        this.dataGenerator = dataGenerator;
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
        public CommandListener(CoapClient client) {
            super(client);
        }

        @Override
        public synchronized void observation() {
            super.observation();
        }
    }
}
