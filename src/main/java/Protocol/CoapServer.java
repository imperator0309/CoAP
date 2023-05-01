package Protocol;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class CoapServer extends Thread {
    private final RootResource root = RootResource.createRoot();
    private int port;

    DatagramSocket serverSocket;

    public CoapServer() {
        port = CoAP.DEFAULT_PORT;
    }

    public CoapServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            serverSocket = new DatagramSocket(port);

            do {
                byte[] buffer = new byte[256];
                DatagramPacket inPkt = new DatagramPacket(buffer, buffer.length);
                serverSocket.receive(inPkt);

                Endpoint endpoint = new Endpoint(inPkt.getAddress(), inPkt.getPort());

                buffer = new byte[inPkt.getLength()];
                ByteArrayInputStream reader = new ByteArrayInputStream(inPkt.getData(), 0, inPkt.getLength());
                reader.read(buffer, 0, buffer.length);

                Request request = new Request(buffer);

                HandleClient handleClient = new HandleClient(this.root, request, endpoint);
                handleClient.start();

            } while (true);
        } catch (SocketException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public CoapResource getResource(String url) {
        try {
            if (url.charAt(0) != '/')
                return null;
            String[] name = url.substring(1).split("/");
            Resource resource = getResource(this.root, name, 0);
            return (CoapResource) resource;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Resource getResource(Resource resource ,String[] name, int index) {
        try {
            if (resource == null)
                return null;

            if (index == name.length)
                return resource;

            return getResource(resource.getChild(name[index]), name, index + 1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void add(CoapResource resource) {
        root.add(resource);
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public RootResource getRoot() {
        return root;
    }

    private static class HandleClient extends Thread {
        private Request request;
        private Endpoint endpoint;
        private RootResource root;
        private HandleClient(RootResource root, Request request, Endpoint endpoint) {
            this.root = root;
            this.request = request;
            this.endpoint = endpoint;
        }

        @Override
        public void run() {
            Exchange exchange = new Exchange(endpoint);

            if (request == null) {
                Response response = new Response();
                response.setType(CoAP.Type.NON);
                response.setTKL(0);
                response.setMessageID(0);
                response.setToken(null);
                response.setOption(null);

                exchange.setResponse(response);
                exchange.respond(CoAP.ResponseCode.BAD_REQUEST, null);
            } else if (request.getOption().getValue() == null) {
                Response response = new Response(request, CoAP.Type.NON);
                exchange.setResponse(response);
                exchange.respond(CoAP.ResponseCode.BAD_OPTION, null);
            } else {
                exchange.setRequest(request);
                String uri = new String(request.getOption().getValue(), 0,
                        request.getOption().getOptionLength());

                CoapResource resource = getResource(uri);

                if (resource == null) {
                    Response response = new Response(request, CoAP.Type.NON);
                    exchange.setResponse(response);
                    exchange.respond(CoAP.ResponseCode.NOT_FOUND, null);
                } else {
                    exchange.setResponse(new Response(request, CoAP.Type.NON));
                    resource.handleRequest(exchange);
                }
            }
        }

        public CoapResource getResource(String url) {
            try {
                if (url.charAt(0) != '/')
                    return null;
                String[] name = url.substring(1).split("/");
                Resource resource = getResource(this.root, name, 0);
                return (CoapResource) resource;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        private Resource getResource(Resource resource ,String[] name, int index) {
            try {
                if (resource == null)
                    return null;

                if (index == name.length)
                    return resource;

                return getResource(resource.getChild(name[index]), name, index + 1);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}