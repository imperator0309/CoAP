package ProjectDemo.Message;

public class CommandMessage {
    private int sensor_id;
    private Command command;

    public CommandMessage() {

    }

    public CommandMessage(int sensor_id, Command command) {
        this.sensor_id = sensor_id;
        this.command = command;
    }

    public int getSensor_id() {
        return sensor_id;
    }

    public void setSensor_id(int sensor_id) {
        this.sensor_id = sensor_id;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public enum Command {
        SUSPEND(0),
        RESUME(1);

        public final int value;
        private Command(final int value) {
            this.value = value;
        }
    }
}
