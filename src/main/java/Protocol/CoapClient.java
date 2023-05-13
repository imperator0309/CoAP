package Protocol;

import CoAPException.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

public class CoapClient {
    private String url;
    private String host;
    private int port;
    private Endpoint server;
    private String resourcePath;
    private ObserveHandler observeHandler;

    public CoapClient(String url) throws CoapClientException {
        this.url = url;
        try {
            host = getHostAddress(url);

            decodeLink();

            if (host == null || port == -1 || resourcePath == null)
                throw new CoapClientException("Invalid URL");

            try {
                InetAddress serverAddress = InetAddress.getByName(host);
                server = new Endpoint(serverAddress, this.port);
            } catch (UnknownHostException ex) {
                throw new CoapClientException("Unknown Server Address");
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new CoapClientException("Invalid URL");
        }
    }

    public Response get() {
        try {
            Token token = new Token(new Random().nextInt(100));
            Option option = new Option(0, 11, resourcePath.getBytes());
            Request request = new Request(CoAP.Type.NON, CoAP.Code.GET, new Random().nextInt(100),
                    token, option, null);

            Exchange exchange = new Exchange(request, server);
            return exchange.request();

        } catch (MessageFormatException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Response post(byte[] payload) {
        try {
            Token token = new Token(new Random().nextInt(100));
            Option option = new Option(0, 11, resourcePath.getBytes());
            Request request = new Request(CoAP.Type.NON, CoAP.Code.POST, new Random().nextInt(100),
                    token, option, payload);
            Exchange exchange = new Exchange(request, server);
            return exchange.request();
        } catch (MessageFormatException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * start observing an observable resource
     */
    public synchronized void observe() throws CoapClientException{
        if (observeHandler == null) {
            throw new CoapClientException("Observing Handler Cannot be Null");
        } else {
            observeHandler.setObserving(true);
            observeHandler.setCancelled(false);
            observeHandler.start();
        }
    }

    /**
     * cancel observing a resource
     */
    public void cancelObserving() {
        observeHandler.setCancelled(true);
    }

    private void decodeLink() {
        host = getHostAddress(url);

        if (host != null) {
            int postIndex = url.indexOf(host) + host.length();

            String tmp = url.substring(postIndex);
            port = getPort(tmp);

            if (port != -1) {
                resourcePath = tmp.substring(CoAP.PORT_SCHEME_SEPARATOR.length() + (port + "").length());
            } else {
                resourcePath = null;
            }
        } else {
            port = -1;
            resourcePath = null;
        }
    }

    private String getHostAddress(String url) {

        if (url.length() < CoAP.PROTOCOL_SCHEME.length() + CoAP.PROTOCOL_SCHEME_SEPARATOR.length())
            return null;

        String protocolCheck = url.substring(0, CoAP.PROTOCOL_SCHEME.length() + CoAP.PROTOCOL_SCHEME_SEPARATOR.length());

        if (!protocolCheck.equals(CoAP.PROTOCOL_SCHEME + CoAP.PROTOCOL_SCHEME_SEPARATOR))
            return null;

        String tmp = url.substring(protocolCheck.length());

        int portIndex = tmp.indexOf(CoAP.PORT_SCHEME_SEPARATOR);
        if (portIndex == -1)
            return null;

        return tmp.substring(0, portIndex);
    }

    private int getPort(String url) {
        if (url.charAt(0) != ':')
            return -1;

        int port;
        try {
            String tmp = url.substring(1, url.indexOf(CoAP.SCHEME_SEPARATOR));
            port = Integer.parseInt(tmp);
        } catch (NumberFormatException ex) {
            return -1;
        } catch (IndexOutOfBoundsException e) {
            return -1;
        }
        return port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Endpoint getServer() {
        return server;
    }

    public void setServer(Endpoint server) {
        this.server = server;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public ObserveHandler getObserveHandler() {
        return observeHandler;
    }

    public void setObserveHandler(ObserveHandler observeHandler) {
        this.observeHandler = observeHandler;
    }
}
