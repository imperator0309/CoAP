package ProjectDemo.Server.Mechanic;

import ProjectDemo.Server.Database.Database;
import Protocol.CoAP;
import Protocol.CoapResource;
import Protocol.CoapServer;
import Protocol.Exchange;

public class Server extends Thread {
    private Database database;
    CoapServer server;

    @Override
    public void run() {
        init();
        server.start();
    }

    private void init() {
        database = Database.getDatabase();
        server = new CoapServer();
        server.add(new TemperatureResource(this));
        server.add(new CommandResource());
    }

    static class TemperatureResource extends CoapResource {
        private final Server server;
        public TemperatureResource(Server server) {
            super("temperature");
            setObservable(true);

            this.server = server;
        }

        @Override
        public void handleGET(Exchange exchange) {
            double temp = server.database.getSensorData();
            String result = temp + "";
            exchange.respond(CoAP.ResponseCode.CONTENT, result.getBytes());
        }

        @Override
        public void handlePOST(Exchange exchange) {
            byte[] payload = exchange.getRequest().getPayload();
            String json = new String(payload);
            server.database.updateSensorValue(json);

            String tmp = server.database.getSensorData() + "";
            setData(tmp.getBytes());

            exchange.respond(CoAP.ResponseCode.CREATE, getData());
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
            super.handleGET(exchange);
        }

        @Override
        public void handlePOST(Exchange exchange) {
            setData(exchange.getRequest().getPayload());
            exchange.respond(CoAP.ResponseCode.CREATE, getData());
            change();
        }
    }
}
