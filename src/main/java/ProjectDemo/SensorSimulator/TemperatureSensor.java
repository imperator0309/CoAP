package ProjectDemo.SensorSimulator;

import CoAPException.CoapClientException;
import Protocol.CoapClient;

public class TemperatureSensor extends Thread {
    private int sensor_id;
    private long timeInterval;
    private DataGenerator dataGenerator;
    private CommandReceiver commandReceiver;
    private String serverAddress;

    public TemperatureSensor(int sensor_id, long timeInterval) {
        this.sensor_id = sensor_id;
        this.timeInterval = timeInterval;
    }

    @Override
    public void run() {
        dataGenerator.setRunning(true);
        dataGenerator.start();
        commandReceiver.start();
    }

    public void connect(String serverAddress) {
        String tempResource = serverAddress + "/temperature";
        String cmdResource = serverAddress + "/command";

        try {
            CoapClient sender = new CoapClient(tempResource);
            CoapClient receiver = new CoapClient(cmdResource);

            this.dataGenerator = new DataGenerator(sensor_id, sender, timeInterval);
            this.commandReceiver = new CommandReceiver(this, receiver);
        } catch (CoapClientException ex) {
            ex.printStackTrace();
        }
    }

    public int getSensor_id() {
        return sensor_id;
    }

    public void setSensor_id(int sensor_id) {
        this.sensor_id = sensor_id;
    }

    public long getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(long timeInterval) {
        this.timeInterval = timeInterval;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public DataGenerator getDataGenerator() {
        return dataGenerator;
    }

    public void setDataGenerator(DataGenerator dataGenerator) {
        this.dataGenerator = dataGenerator;
    }

    public CommandReceiver getCommandReceiver() {
        return commandReceiver;
    }

    public void setCommandReceiver(CommandReceiver commandReceiver) {
        this.commandReceiver = commandReceiver;
    }
}
