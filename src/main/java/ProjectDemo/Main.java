package ProjectDemo;

import ProjectDemo.Message.CommandMessage;
import ProjectDemo.Message.SensorMessage;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {
    public static void main(String[] args) {
        CommandMessage message = new CommandMessage(1, CommandMessage.Command.SUSPEND);
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(message);
            System.out.println(json);

            CommandMessage message1 = mapper.readValue(json, CommandMessage.class);
            System.out.println("sensor_id:" + message1.getSensor_id());
            System.out.println("Command:" + message1.getCommand());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
