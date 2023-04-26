package ProjectDemo;

import Protocol.CoapClient;

public class Sensor {
    private DataGenerator dataGenerator;
    private CommandReceiver commandReceiver;
    private int sensor_id;

    public static void main(String[] args) {
        try {
            CoapClient client1 = new CoapClient("coap//localhost:5683/temperature");
            CoapClient client2 = new CoapClient("coap//localhost:5683/command");

            Sensor sensor = new Sensor(1, client1, client2, 5000);
            sensor.run();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public Sensor(int id, CoapClient sender, CoapClient receiver, long interval) {
        this.sensor_id = id;
        this.dataGenerator = new DataGenerator(this.sensor_id, sender, interval);
        this.commandReceiver = new CommandReceiver(receiver, this.dataGenerator);
    }

    public void run() {
        dataGenerator.start();
        commandReceiver.start();
    }

    public int getSensor_id() {
        return sensor_id;
    }

    public void setSensor_id(int sensor_id) {
        this.sensor_id = sensor_id;
    }

    public CommandReceiver getCommandReceiver() {
        return commandReceiver;
    }

    public void setCommandReceiver(CommandReceiver commandReceiver) {
        this.commandReceiver = commandReceiver;
    }

    public DataGenerator getDataGenerator() {
        return dataGenerator;
    }

    public void setDataGenerator(DataGenerator dataGenerator) {
        this.dataGenerator = dataGenerator;
    }
}
