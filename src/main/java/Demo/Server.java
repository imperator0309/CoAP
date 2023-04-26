package Demo;

import Protocol.CoAP;
import Protocol.CoapResource;
import Protocol.CoapServer;
import Protocol.Exchange;

public class Server {
    public static void main(String[] args) {
        CoapServer server = new CoapServer();
        server.add(new HelloResource());
        server.start();
    }

    public static class HelloResource extends CoapResource {
        public HelloResource() {
            super("Hello");
            setObservable(true);
        }

        @Override
        public void handleGET(Exchange exchange) {
            exchange.respond(CoAP.ResponseCode.CONTENT, "Hello".getBytes());
        }

        @Override
        public void handlePOST(Exchange exchange) {
            setData(exchange.getRequest().getPayload());
            exchange.respond(CoAP.ResponseCode.CREATE, getData());
            change();
        }
    }
}
