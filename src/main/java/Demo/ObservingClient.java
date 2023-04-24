package Demo;

import Protocol.CoapClient;

public class ObservingClient {
    public static void main(String[] args) {
        try {
            CoapClient client = new CoapClient("coap//localhost:5683/Hello");
            client.observe();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
