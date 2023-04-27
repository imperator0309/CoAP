package ProjectDemo.Server.GUI;

import ProjectDemo.Message.SensorMessage;
import Protocol.CoAP;
import Protocol.CoapResource;
import Protocol.CoapServer;
import Protocol.Exchange;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {
    public static void main(String[] args) {
        CoapServer server = new CoapServer();
        server.add(new TemperatureResource());
        server.add(new CommandResource());
        server.start();
    }

    public static class TemperatureResource extends CoapResource {
        public TemperatureResource() {
            super("temperature");
            setObservable(true);
        }

        @Override
        public void handleGET(Exchange exchange) {
            exchange.respond(CoAP.ResponseCode.CONTENT, getData());
        }

        @Override
        public void handlePOST(Exchange exchange) {
            byte[] payload = exchange.getRequest().getPayload();

            if (payload != null) {
                try {
                    setData(payload);
                    ObjectMapper mapper = new ObjectMapper();
                    String json = new String(payload);
                    SensorMessage message = mapper.readValue(json, SensorMessage.class);

                    double delay = (System.currentTimeMillis() - message.getLast_time_modified());

                    System.out.println(json + " | delay:" + delay+ "ms | throughput:" +
                            payload.length * 8 / (delay / 1000  * 1024) + "Kbps");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            exchange.respond(CoAP.ResponseCode.CREATE, payload);
            change();
        }
    }

    public static class CommandResource extends CoapResource {
        public CommandResource() {
            super("command");
            setObservable(true);
        }

        @Override
        public void handleGET(Exchange exchange) {
            exchange.respond(CoAP.ResponseCode.CONTENT, getData());
        }

        @Override
        public void handlePOST(Exchange exchange) {
            byte[] payload = exchange.getRequest().getPayload();

            if (payload != null) {
                setData(payload);
                System.out.println(new String(payload));
            }

            exchange.respond(CoAP.ResponseCode.CREATE, payload);
            change();
        }
    }
}
