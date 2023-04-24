package Protocol;

public abstract class Message {
    private int version = CoAP.VERSION;
    private CoAP.Type type;
    private int TKL;

    private Token token = null;
    private int messageID;
    private Option option;
    private byte[] payload;

    protected Message() {

    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getMessageID() {
        return messageID;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    public CoAP.Type getType() {
        return type;
    }

    public void setType(CoAP.Type type) {
        this.type = type;
    }

    public int getTKL() {
        return TKL;
    }

    public void setTKL(int TKL) {
        this.TKL = TKL;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public Option getOption() {
        return option;
    }

    public void setOption(Option option) {
        this.option = option;
    }

    public byte[] getPayload() {
        return payload;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }
}
