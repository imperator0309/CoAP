package ProjectDemo;

import Protocol.CoAP;
import Protocol.CoapResource;
import Protocol.CoapServer;
import Protocol.Exchange;
import com.fasterxml.jackson.databind.ObjectMapper;


public class Main {
    public static void main(String[] args) {
//        CoapServer server = new CoapServer();
//        server.add(new Temperature());
//        server.add(new SensorCommand());
//        server.start();

        CommandMessage message = new CommandMessage(1, CommandMessage.Command.SUSPEND);
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(message);
            System.out.println(json);

            CommandMessage message1 = mapper.convertValue(json, CommandMessage.class);
            System.out.println("Sensor_ID>>" + message1.getSensor_id());
            System.out.println("Command>>" + message1.getCommand());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class Temperature extends CoapResource {
        public Temperature() {
            super("temperature");
            setObservable(true);
        }

        @Override
        public void handleGET(Exchange exchange) {
            if (getData() != null) {
                exchange.respond(CoAP.ResponseCode.CONTENT, getData());
            } else {
                exchange.respond(CoAP.ResponseCode.NOT_FOUND, null);
            }
        }

        @Override
        public void handlePOST(Exchange exchange) {
            setData(exchange.getRequest().getPayload());
            exchange.respond(CoAP.ResponseCode.CREATE, getData());
            if (getData() != null) {
                System.out.println(new String(getData()));
            }
            change();
        }
    }

    public static class SensorCommand extends CoapResource {
        public SensorCommand() {
            super("command");
            setObservable(true);
        }

        @Override
        public void handleGET(Exchange exchange) {
            if (getData() != null) {
                exchange.respond(CoAP.ResponseCode.CONTENT, getData());
            } else {
                exchange.respond(CoAP.ResponseCode.NOT_FOUND, null);
            }
        }

        @Override
        public void handlePOST(Exchange exchange) {
            setData(exchange.getRequest().getPayload());
            exchange.respond(CoAP.ResponseCode.CREATE, getData());
            if (getData() != null) {
                System.out.println(new String(getData()));
            }
            change();
        }
    }
}
