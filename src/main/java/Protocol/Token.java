package Protocol;

public class Token {
    private byte[] tokenBytes;

    public Token() {
        tokenBytes = null;
    }

    public Token(byte[] tokenBytes) {
        this.tokenBytes = tokenBytes;
    }

    public Token(int value) {
        int tkl = 0;
        byte[] tmp = new byte[8];
        while (value > 0) {
            tmp[tkl] = (byte) (value & 0xFF);
            value = value >> 8;
            tkl++;
        }
        tokenBytes = new byte[tkl];
        for (int i = tkl - 1; i >= 0; i--) {
            tokenBytes[tkl - i - 1] = tmp[i];
        }
    }

    public long getValue() {
        long value = 0;
        for (int i = 0; i < tokenBytes.length; i++) {
            value = (value << 8) + tokenBytes[i];
        }
        return value;
    }

    public byte[] getTokenBytes() {
        return tokenBytes;
    }

    public void setTokenBytes(byte[] tokenBytes) {
        this.tokenBytes = tokenBytes;
    }
}

