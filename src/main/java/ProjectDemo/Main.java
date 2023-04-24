package ProjectDemo;

import com.fasterxml.jackson.databind.ObjectMapper;


public class Main {
    public static void main(String[] args) {
        CommandMessage commandMessage = new CommandMessage();
        commandMessage.setSensor_id(14);
        commandMessage.setCommand(CommandMessage.Command.RESUME);

        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(commandMessage);
            System.out.println(json);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
