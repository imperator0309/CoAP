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
    private String URI;
    private ObserveHandler observeHandler;

    public CoapClient(String url) throws CoapClientException {
        this.url = url;
        try {
            String host = getHostName(url);
            int port = getPort(url);

            if (host == null || port == -1) {
                throw new CoapClientException("Invalid URL");
            }

            this.host = host;
            this.port = port;

            int resourceIndex = url.indexOf(":");
            for (int i = resourceIndex + 1; i < url.length(); i++) {
                if (url.charAt(i) == '/') {
                    resourceIndex = i;
                    break;
                }
            }

            this.URI = url.substring(resourceIndex);

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
            Option option = new Option(0, 11, URI.getBytes());
            Request request = new Request(CoAP.Type.CON, CoAP.Code.GET, new Random().nextInt(100),
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
            Option option = new Option(0, 11, URI.getBytes());
            Request request = new Request(CoAP.Type.CON, CoAP.Code.POST, new Random().nextInt(100),
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
            observeHandler.start();
        }
    }

    /**
     * cancel observing a resource
     */
    public synchronized void cancelObserving() {
        try {
            Option option = new Option(0, 6, URI.getBytes());
            Request request = new Request(CoAP.Type.RST, CoAP.Code.GET, new Random().nextInt(100),
                    null, option, null);
            Exchange exchange = new Exchange(request, server);
            exchange.non_request();

            observeHandler.setObserving(false);
        } catch (MessageFormatException e) {
            e.printStackTrace();
        }
    }

    private String getHostName(String url) {
        int separator = url.indexOf(CoAP.PROTOCOL_SCHEME_SEPARATOR);
        if (separator == -1)
            return null;
        int portIndex = url.indexOf(":");
        if (portIndex == -1)
            return null;
        if (portIndex <= separator + 1)
            return null;
        return url.substring(separator + 2, portIndex);
    }

    private int getPort(String url) {
        int indexPort = url.indexOf(":");
        if (indexPort == -1)
            return -1;
        int uri_index = -1;
        for (int i = indexPort + 1; i < url.length(); i++) {
            if (url.charAt(i) == '/') {
                uri_index = i;
                break;
            }
        }
        if (uri_index == -1)
            return -1;
        String tmp = url.substring(indexPort + 1, uri_index);
        return Integer.parseInt(tmp);
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

    public String getURI() {
        return URI;
    }

    public void setURI(String URI) {
        this.URI = URI;
    }

    public ObserveHandler getObserveHandler() {
        return observeHandler;
    }

    public void setObserveHandler(ObserveHandler observeHandler) {
        this.observeHandler = observeHandler;
    }
}
