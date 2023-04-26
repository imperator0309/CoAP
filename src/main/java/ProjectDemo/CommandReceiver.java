package ProjectDemo;

import CoAPException.CoapClientException;
import Protocol.CoapClient;
import Protocol.ObserveHandler;
import Protocol.Response;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CommandReceiver extends Thread{
    private CoapClient client;
    private DataGenerator dataGenerator;
    public static final ObjectMapper mapper = new ObjectMapper();

    public CommandReceiver(CoapClient client, DataGenerator dataGenerator) {
        this.client = client;
        this.dataGenerator = dataGenerator;
    }

    @Override
    public void run() {
        this.client.setObserveHandler(new CommandListener(client));
        try {
            client.observe();
        } catch (CoapClientException ex) {
            ex.printStackTrace();
        }
    }

    public CoapClient getClient() {
        return client;
    }

    public void setClient(CoapClient client) {
        this.client = client;
    }

    private class CommandListener extends ObserveHandler {
        private CommandListener(CoapClient client) {
            super(client);
        }

        @Override
        public synchronized void observation() {
            super.observation();
            Response response = getResponse();
            if (response != null) {
                if (response.getPayload() != null) {
                    try {
                        String command = new String(response.getPayload());
                        CommandMessage commandMessage = mapper.convertValue(command, CommandMessage.class);
                        if (commandMessage.getSensor_id() == dataGenerator.getSensor_id()) {
                            switch (commandMessage.getCommand()) {
                                case SUSPEND -> {
                                    if (dataGenerator.isRunning()) {
                                        dataGenerator.wait();
                                        dataGenerator.setRunning(false);
                                    }
                                }
                                case RESUME -> {
                                    if (!dataGenerator.isRunning()) {
                                        dataGenerator.setRunning(true);
                                        if (dataGenerator.isAlive()) {
                                            notifyAll();
                                        } else {
                                            dataGenerator.start();
                                        }
                                    }
                                }
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
