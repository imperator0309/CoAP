package Demo;

import Protocol.CoAP;
import Protocol.CoapClient;
import Protocol.ObserveHandler;

public class ObservingClient {
    public static void main(String[] args) {
        try {
            CoapClient client = new CoapClient("coap//localhost:5683/Hello");
            client.setObserveHandler(new Observer(client));
            client.observe();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public static class Observer extends ObserveHandler {
        public Observer(CoapClient client) {
            super(client);
        }

        @Override
        public void observation() {
            super.observation();
            if (getResponse() != null)
                if (getResponse().getPayload() != null)
                    System.out.println(new String(getResponse().getPayload()));
        }
    }
}
