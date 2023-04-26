package ProjectDemo.Message;

public class SensorMessage {
    private int sensor_id;
    private double temperature;
    private long last_time_modified;

    public SensorMessage() {

    }

    public SensorMessage(int sensor_id, double temperature, long last_time_modified) {
        this.sensor_id = sensor_id;
        this.temperature = temperature;
        this.last_time_modified = last_time_modified;
    }

    public int getSensor_id() {
        return sensor_id;
    }

    public void setSensor_id(int sensor_id) {
        this.sensor_id = sensor_id;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public long getLast_time_modified() {
        return last_time_modified;
    }

    public void setLast_time_modified(long last_time_modified) {
        this.last_time_modified = last_time_modified;
    }
}
