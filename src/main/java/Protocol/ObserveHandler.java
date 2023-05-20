package Protocol;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Random;

import CoAPException.*;
public class ObserveHandler extends Thread{
    private CoapClient client;
    private String resourcePath;
    private Response response;
    private DatagramSocket datagramSocket;
    private boolean observing;
    private boolean cancelled;

    public ObserveHandler(CoapClient client) {
        try {
            this.client = client;
            resourcePath = client.getResourcePath();
            datagramSocket = new DatagramSocket();
        } catch (SocketException e) {
            System.err.println("Cannot Create Observation Handler");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            boolean ack;

            do {
                ack = register();
            } while (!ack);

            while (observing) {
                observation();
            }
        } catch (CoapClientException e) {
            System.out.println(e.getMessage());
            observing = false;
        }
    }

    public boolean register() throws CoapClientException {
        try {
            Option option = new Option(0, 6, resourcePath.getBytes());
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
            int n = reader.read(buffer, 0, buffer.length);

            if (n == -1)
                return false;

            Response response = new Response(buffer);

            if (response.getType() == CoAP.Type.ACK && response.getCode() == CoAP.ResponseCode.CONTENT) {
                datagramSocket.setSoTimeout(0);
                return true;
            } else if (response.getType() == CoAP.Type.RST &&
                    response.getCode() == CoAP.ResponseCode.METHOD_NOT_ALLOWED) {
                datagramSocket.setSoTimeout(0);
                throw new CoapClientException("Resource cannot be observed");
            } else {
                return false;
            }
        } catch (SocketTimeoutException ex) {
            return false;
        } catch (MessageFormatException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public synchronized void observation() {
        try {
            byte[] buffer = new byte[256];
            DatagramPacket packet = new DatagramPacket(buffer, 0, buffer.length);
            datagramSocket.receive(packet);
            ByteArrayInputStream reader = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());

            if (cancelled) {
                try {
                    Option option = new Option(0, 6, client.getResourcePath().getBytes());
                    Request request = new Request(CoAP.Type.RST, CoAP.Code.GET, new Random().nextInt(100),
                            null, option, null);
                    DatagramPacket cancelPacket = new DatagramPacket(request.toByteArray(), 0,
                            request.toByteArray().length, client.getServer().getAddress(), client.getServer().getPort());
                    datagramSocket.send(cancelPacket);
                } catch (MessageFormatException ex) {
                    ex.printStackTrace();
                }
            } else {
                buffer = new byte[packet.getLength()];
                int n = reader.read(buffer, 0, buffer.length);
                if (n != -1)
                    response = new Response(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MessageFormatException ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
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

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
