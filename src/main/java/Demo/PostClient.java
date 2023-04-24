package Demo;

import Protocol.CoAP;
import Protocol.CoapClient;
import Protocol.Response;

import java.util.concurrent.TimeUnit;

public class PostClient {
    public static void main(String[] args) {
        try {
            CoapClient client = new CoapClient("coap//localhost:5683/Hello");
            while (true) {
                Response response = client.post("Hello World".getBytes());
                if (response != null)
                    if (response.getCode() == CoAP.ResponseCode.CREATE)
                        System.out.println("Server>>" + new String(response.getPayload()));

                TimeUnit.MILLISECONDS.sleep(3000);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
