package ProjectDemo.Server.GUI;

import Protocol.CoAP;
import Protocol.CoapResource;
import Protocol.CoapServer;
import Protocol.Exchange;

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
                setData(payload);
                System.out.println(new String(payload));
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
