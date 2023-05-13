package Protocol;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class CoapResource implements Resource {
    private String name;
    private Resource parent;
    private final HashMap<String, Resource> children = new HashMap<>();
    private boolean observable;
    private final HashSet<Endpoint> observers = new HashSet<>();
    private byte[] data;

    protected CoapResource(String name) {
        this.name = name;
    }

    public void handleRequest(Exchange exchange) {
        CoAP.Code method = exchange.getRequest().code;
        switch (method) {
            case GET -> {
                Option.OptionDefinition definition = exchange.getRequest().getOption().getDefinition();
                if (definition == Option.OptionDefinition.OBSERVE) {
                    if (observable) {
                        if (exchange.getRequest().getType() == CoAP.Type.CON) {
                            observers.add(exchange.getEndpoint());
                            Response response = new Response(exchange.getRequest(), CoAP.Type.ACK);
                            exchange.setResponse(response);
                            exchange.respond(CoAP.ResponseCode.CONTENT, data);

                        } else if (exchange.request().getType() == CoAP.Type.RST) {
                            observers.remove(exchange.getEndpoint());
                        }
                    } else {
                        Response response = new Response(exchange.getRequest(), CoAP.Type.RST);
                        exchange.setResponse(response);
                        exchange.respond(CoAP.ResponseCode.METHOD_NOT_ALLOWED, null);
                    }
                } else {
                    handleGET(exchange);
                }
            }
            case POST ->
                    handlePOST(exchange);
        }
    }

    public void handleGET(Exchange exchange) {
        exchange.respond(CoAP.ResponseCode.METHOD_NOT_ALLOWED, null);
    }

    public void handlePOST(Exchange exchange) {
        exchange.respond(CoAP.ResponseCode.METHOD_NOT_ALLOWED, null);
    }

    /**
     * Call when an observable resource changed state
     */
    public void change() {
        for (Endpoint observer : observers) {
            Response response = new Response(CoAP.Type.NON, CoAP.ResponseCode.CONTENT,
                    new Random().nextInt(100), null, null, data);
            Exchange exchange = new Exchange(response, observer);
            exchange.respond(CoAP.ResponseCode.CREATE, data);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Resource getChild(String name) {
        return children.get(name);
    }

    @Override
    public Resource getParent() {
        return parent;
    }

    @Override
    public void setParent(Resource parent) {
        this.parent = parent;
    }

    @Override
    public boolean isObservable() {
        return observable;
    }

    @Override
    public void setObservable(boolean observable) {
        this.observable = observable;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public void add(Resource child) {
        if (child == null || child.getName() == null) {
            System.err.println("Child name must not be null");
        } else {
            Resource parent = child.getParent();
            if (parent == this && children.get(child.getName()) == child) {
                return;
            }
            if (parent != null) {
                parent.delete(child.getName());
            }
            Resource previous = children.get(child.getName());
            if (previous != null && previous != child) {
                delete(previous.getName());
            }
            children.put(child.getName(), child);
            child.setParent(this);
        }
    }

    @Override
    public void delete(String childName) {
        Resource child = children.remove(childName);
        if (child != null) {
            child.setParent(null);
        }
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (! (obj instanceof Resource))
            return false;

        return name.equals(((Resource) obj).getName());
    }
}
