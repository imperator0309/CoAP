package Protocol;

import CoAPException.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;

public class Exchange {
    private Endpoint endpoint;
    private Response response;
    private Request request;

    public Exchange(Endpoint endpoint) {
        this.endpoint = endpoint;
    }

    public Exchange(Request request, Endpoint endpoint) {
        this.request = request;
        this.endpoint = endpoint;
    }

    public Exchange(Response response, Endpoint endpoint) {
        this.response = response;
        this.endpoint = endpoint;
    }

    public void respond(CoAP.ResponseCode code, byte[] payload) {
        DatagramSocket datagramSocket = null;
        try {
            datagramSocket = new DatagramSocket();
            response.setCode(code);
            response.setPayload(payload);
            byte[] data = response.toByteArray();
            DatagramPacket outPkt = new DatagramPacket(data, 0, data.length,
                    endpoint.getAddress(), endpoint.getPort());

            datagramSocket.send(outPkt);
        } catch (IOException exception) {
            exception.printStackTrace();
        } catch (MessageFormatException e) {
            e.printStackTrace();
        } finally {
            if (datagramSocket != null) {
                datagramSocket.close();
            }
        }
    }

    public void non_request() {
        DatagramSocket datagramSocket = null;
        try {
            datagramSocket = new DatagramSocket();
            byte[] data = request.toByteArray();
            DatagramPacket packet = new DatagramPacket(data, 0, data.length,
                    endpoint.getAddress(), endpoint.getPort());

            datagramSocket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MessageFormatException ex) {
            ex.printStackTrace();
        }
    }

    public Response request() {
        Response response = null;
        DatagramSocket datagramSocket = null;
        try {
            datagramSocket = new DatagramSocket();

            byte[] data = request.toByteArray();
            DatagramPacket outPkt = new DatagramPacket(data, 0, data.length,
                    endpoint.getAddress(), endpoint.getPort());

            datagramSocket.send(outPkt);

            byte[] buffer = new byte[256];
            DatagramPacket inPkt = new DatagramPacket(buffer, 0, buffer.length);
            datagramSocket.receive(inPkt);

            ByteArrayInputStream reader = new ByteArrayInputStream(inPkt.getData(), 0, inPkt.getLength());
            byte[] receiver = new byte[inPkt.getLength()];
            reader.read(receiver, 0, receiver.length);

            response = new Response(receiver);

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MessageFormatException e) {
            e.printStackTrace();
        } finally {
            if (datagramSocket != null) {
                datagramSocket.close();
            }
        }
        return response;
    }

    public Endpoint getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(Endpoint endpoint) {
        this.endpoint = endpoint;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }
}

