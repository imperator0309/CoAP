package Protocol;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Random;

import CoAPException.*;
public class ObserveHandler extends Thread{
    private CoapClient client;
    private String URI;
    private Response response;
    private DatagramSocket datagramSocket;
    private boolean observing;

    public ObserveHandler(CoapClient client) {
        try {
            this.client = client;
            URI = client.getURI();
            datagramSocket = new DatagramSocket();
        } catch (SocketException e) {
            System.err.println("Cannot Create Observation Handler");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            boolean ack = onLoad();
            while (!ack) {
                ack = onLoad();
            }

            while (observing) {
                observation();
            }
        } catch (CoapClientException e) {
            observing = false;
        }
    }

    public boolean onLoad() throws CoapClientException {
        try {
            Option option = new Option(0, 6, URI.getBytes());
            Request request = new Request(CoAP.Type.CON, CoAP.Code.GET,
                    new Random().nextInt(100), null, option, null);
            byte[] buffer = request.toByteArray();
            DatagramPacket packet = new DatagramPacket(buffer, 0, buffer.length,
                    client.getServer().getAddress(), client.getServer().getPort());

            datagramSocket.send(packet);

            buffer = new byte[256];
            DatagramPacket ackPkt = new DatagramPacket(buffer, 0, buffer.length);
            datagramSocket.setSoTimeout(3000);
            datagramSocket.receive(ackPkt);

            buffer = new byte[ackPkt.getLength()];
            ByteArrayInputStream reader = new ByteArrayInputStream(ackPkt.getData(), 0, ackPkt.getLength());
            reader.read(buffer, 0, buffer.length);

            Response response = new Response(buffer);
            if (response == null)
                return false;

            if (response.getType() == CoAP.Type.ACK) {
                datagramSocket.setSoTimeout(0);
                return true;
            } else if (response.getType() == CoAP.Type.RST &&
                    response.getCode() == CoAP.ResponseCode.METHOD_NOT_ALLOWED) {
                throw new CoapClientException("Resource cannot be observed");
            } else {
                return false;
            }
        } catch (MessageFormatException e) {
            e.printStackTrace();
            return false;
        } catch (SocketException ex) {
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void observation() {
        try {
            byte[] buffer = new byte[256];
            DatagramPacket packet = new DatagramPacket(buffer, 0, buffer.length);
            datagramSocket.receive(packet);
            ByteArrayInputStream reader = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());

            buffer = new byte[packet.getLength()];
            reader.read(buffer, 0, buffer.length);
            response = new Response(buffer);
            System.out.println(new String(response.getPayload()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getURI() {
        return URI;
    }

    public void setURI(String URI) {
        this.URI = URI;
    }

    public DatagramSocket getDatagramSocket() {
        return datagramSocket;
    }

    public void setDatagramSocket(DatagramSocket datagramSocket) {
        this.datagramSocket = datagramSocket;
    }

    public Response getResponse() {
        return response;
    }

    public boolean isObserving() {
        return observing;
    }

    public void setObserving(boolean observing) {
        this.observing = observing;
    }
}
