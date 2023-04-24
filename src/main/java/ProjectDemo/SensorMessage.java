package ProjectDemo;

import java.io.Serializable;

public class SensorMessage implements Serializable {
    public static final long serialVersionUID = 1L;
    private int sensor_id;
    private int temperate;
    private long time_send;

    public int getSensor_id() {
        return sensor_id;
    }

    public void setSensor_id(int sensor_id) {
        this.sensor_id = sensor_id;
    }

    public int getTemperate() {
        return temperate;
    }

    public void setTemperate(int temperate) {
        this.temperate = temperate;
    }

    public long getTime_send() {
        return time_send;
    }

    public void setTime_send(long timeSend) {
        this.time_send = timeSend;
    }
}
