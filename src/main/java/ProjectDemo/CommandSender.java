package ProjectDemo;

import ProjectDemo.Message.CommandMessage;
import Protocol.CoapClient;
import Protocol.Response;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Scanner;

public class CommandSender {
    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            CoapClient sender = new CoapClient("coap//localhost:5683/command");
            Scanner sc = new Scanner(System.in);

            String cmd;
            int id;

            do {
                System.out.print("Sensor ID>>");
                id = Integer.parseInt(sc.nextLine());
                System.out.print("Command>>");
                cmd = sc.nextLine();

                if (!cmd.equals("QUIT")) {
                    CommandMessage command = new CommandMessage(id, CommandMessage.Command.valueOf(cmd));
                    String json = mapper.writeValueAsString(command);
                    Response response = sender.post(json.getBytes());
                    if (response != null) {
                        System.out.println("Command Status>>" + response.getCode());
                    }
                }
            } while (!cmd.equals("QUIT"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
