package ProjectDemo;

import java.io.Serializable;

public class CommandMessage {
    private int sensor_id;
    private Command command;

    public CommandMessage() {

    }

    public CommandMessage(int sensor_id, Command command) {
        this.sensor_id = sensor_id;
        this.command = command;
    }

    public CommandMessage(int sensor_id, String cmd) throws UnknownCommandException {
        try {
            this.sensor_id = sensor_id;
            Command command = Command.getValue(cmd);
            this.command = command;
        } catch (UnknownCommandException e) {
            throw new  UnknownCommandException("Unknown Command");
        }
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

        public static Command getValue(String value) throws UnknownCommandException {
            switch (value) {
                case "SUSPEND" -> {
                    return SUSPEND;
                }
                case "RESUME" -> {
                    return RESUME;
                }
                default -> {
                    throw new UnknownCommandException("Unknown Command");
                }
            }
        }
    }
}
