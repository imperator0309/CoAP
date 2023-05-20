package Protocol;

import CoAPException.MessageFormatException;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

public class CoapServer extends Thread {
    private final RootResource root = RootResource.createRoot();
    private int port;
    private final int BUFFER_SIZE = 4096;

    DatagramChannel channel;
    Selector selector;
    ByteBuffer buffer;

    public CoapServer() {
        try {
            port = CoAP.DEFAULT_PORT;

            selector = Selector.open();
            channel = DatagramChannel.open();
            InetSocketAddress address = new InetSocketAddress(port);
            channel.socket().bind(address);
            channel.configureBlocking(false);

            channel.register(selector, SelectionKey.OP_READ);

            buffer = ByteBuffer.allocate(BUFFER_SIZE);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public CoapServer(int port) {
        try {
            this.port = port;

            selector = Selector.open();
            channel = DatagramChannel.open();
            InetSocketAddress address = new InetSocketAddress(port);
            channel.socket().bind(address);
            channel.configureBlocking(false);

            channel.register(selector, SelectionKey.OP_READ);

            buffer = ByteBuffer.allocate(BUFFER_SIZE);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        do {
            handleClient();
        } while (true);
    }

    private void handleClient() {
        try {
            this.selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

            while (iterator.hasNext()) {
                try {
                    SelectionKey key = iterator.next();
                    iterator.remove();

                    if (!key.isValid()) {
                        continue;
                    }

                    if (key.isReadable()) {
                        buffer.clear();
                        SocketAddress clientSocket = channel.receive(buffer);
                        String tmp = clientSocket.toString();
                        String clientAddress = tmp.substring(1, tmp.indexOf(":"));
                        int clientPort = Integer.parseInt(tmp.substring(tmp.indexOf(":") + 1));

                        Endpoint endpoint = new Endpoint(InetAddress.getByName(clientAddress), clientPort);
                        byte[] data = Arrays.copyOfRange(buffer.array(), 0, buffer.position());

                        Request request;
                        try {
                            request = new Request(data);
                        } catch (Exception e) {
                            request = null;
                        }
                        Exchange exchange = new Exchange(endpoint, channel);

                        if (request != null) {
                            if (request.getOption().getValue() == null) {
                                Response response = new Response(request, CoAP.Type.NON);

                                response.setOption(null);

                                exchange.setResponse(response);
                                exchange.respond(CoAP.ResponseCode.BAD_OPTION, null);
                            } else {
                                exchange.setRequest(request);
                                String uri = new String(request.getOption().getValue(), 0,
                                        request.getOption().getOptionLength());

                                CoapResource resource = getResource(uri);

                                Response response = new Response(request, CoAP.Type.ACK);
                                if (resource == null) {
                                    response.setOption(null);
                                    exchange.setResponse(response);
                                    exchange.respond(CoAP.ResponseCode.NOT_FOUND, null);
                                } else {
                                    Option option = new Option(0, 0, null);
                                    response.setOption(option);
                                    exchange.setResponse(response);
                                    resource.handleRequest(exchange);
                                }
                            }
                        } else {
                            Response response = new Response(CoAP.Type.NON, CoAP.ResponseCode.BAD_REQUEST,
                                    new Random().nextInt(100), null, null, null);
                            exchange.setResponse(response);
                            exchange.respond(CoAP.ResponseCode.BAD_REQUEST, null);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (MessageFormatException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void destroy() {
        try {
            channel.close();
        } catch (IOException ex) {
            System.err.println("cannot close server channel");
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
        resource.setServer(this);
        root.add(resource);
    }

    public DatagramChannel getChannel() {
        return channel;
    }

    public void setChannel(DatagramChannel channel) {
        this.channel = channel;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}