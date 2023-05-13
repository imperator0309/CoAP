package Protocol;

import java.net.InetAddress;

public class Endpoint {
    private InetAddress address;
    private int port;

    public Endpoint(InetAddress address, int port) {
        this.address = address;
        this.port = port;
    }

    public InetAddress getAddress() {
        return address;
    }

    public void setAddress(InetAddress address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public boolean equals(Object obj) {
        if (! (obj instanceof Endpoint))
            return false;

        return (this.address.getHostName().equals(((Endpoint) obj).getAddress().getHostName())
                && port == ((Endpoint)obj).getPort());
    }

    @Override
    public int hashCode() {
        return (address.getHostName().hashCode()) + port;
    }
}
