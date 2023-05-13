package Protocol;

import CoAPException.*;
import java.io.ByteArrayOutputStream;

public class Request extends Message {
    CoAP.Code code;

    public Request() {

    }

    public Request(CoAP.Type type, CoAP.Code code, int mid, Token token, Option option, byte[] payload) {
        int tkl;
        if (token == null || token.getTokenBytes() == null) {
            tkl = 0;
        } else {
            tkl = token.getTokenBytes().length;
        }
        setType(type);
        setTKL(tkl);
        setCode(code);
        setMessageID(mid);
        setToken(token);
        setOption(option);
        setPayload(payload);
    }

    public Request(byte[] data) {
        try {
            int firstByte= data[0];
            int version = (firstByte >> 6) & 0b00000011;
            int typeCode = (firstByte >> 4) & 0b00000011;
            int tkl = (firstByte & 0b00001111);

            int code = data[1];

            int mid = 0;
            for (int i = 2; i < 4; i++) {
                int tmp = data[i];
                mid = (mid << 8) + (tmp & 0xFF);
            }

            int optionInfo = data[4 + tkl];
            int delta = (optionInfo >> 4) & 0b00001111;
            int length = optionInfo & 0b00001111;

            int index = 5 + tkl;
            byte[] optionValue = new byte[length];
            for (int i = 0; i < length; i++) {
                optionValue[i] = data[index];
                index++;
            }

            index++; //skip end header marking byte

            CoAP.Type type = CoAP.Type.valueOf(typeCode);
            CoAP.Code method = CoAP.Code.valueOf(code);
            Option option = new Option(0, delta, optionValue);
            Token token = new Token();

            setVersion(version);
            setType(type);
            setTKL(tkl);
            setCode(method);
            setMessageID(mid);
            setOption(option);

            if (data.length - index > 0) {
                byte[] payload = new byte[data.length - index];
                for (int i = 0; i < payload.length; i++) {
                    payload[i] = data[index];
                    index++;
                }
                setPayload(payload);
            }

            if (tkl > 0) {
                byte[] tokenData = new byte[tkl];
                for (int i = 0; i < tkl; i++) {
                    tokenData[i] = data[4 + i];
                }
                token.setTokenBytes(tokenData);
            }

            setToken(token);
        } catch (MessageFormatException exception) {
            System.err.println("Failed to read request");
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public byte[] toByteArray() throws MessageFormatException{
        int type;
        switch (this.getType()) {
            case CON ->
                    type = 0;
            case NON ->
                    type = 1;
            case ACK ->
                    type = 2;
            case RST ->
                    type = 3;
            default ->
                    throw new MessageFormatException("Unknown Message Type");
        }

        byte firstByte = (byte) ((CoAP.VERSION << 6) | (type << 4) | (this.getTKL() & 0b00001111));
        byte secondByte = (byte) (code.value & 0xFF);
        byte firstID = (byte) ((getMessageID() >> 8) & 0b11111111);
        byte lastID = (byte) (getMessageID() & 0b11111111);
        byte optionFirstByte = (byte) ((getOption().getOptionDelta() << 4) |
                (getOption().getOptionLength() & 0b00001111));

        /*
        4 default header bytes,
        TKL bytes for token,
        option length + 1 bytes for option (1 bytes for delta & value length; length bytes for value)
        1 last bytes for marker 0xFF
        */
        byte[] tmp = new byte[4 + this.getTKL() + this.getOption().getOptionLength() + 1 + 1];
        tmp[0] = firstByte;
        tmp[1] = secondByte;
        tmp[2] = firstID;
        tmp[3] = lastID;

        int current = 4;

        if (getTKL()> 0) {
            for (int i = 0; i < getTKL(); i++) {
                tmp[current] = getToken().getTokenBytes()[i];
                current++;
            }
        }

        tmp[current] = optionFirstByte;
        current++;
        for (int i = 0; i < getOption().getOptionLength(); i++) {
            tmp[current] = getOption().getValue()[i];
            current++;
        }
        tmp[current] = CoAP.END_HEADER_BYTE;

        ByteArrayOutputStream writer = new ByteArrayOutputStream();
        writer.writeBytes(tmp);
        if (getPayload() != null && getPayload().length > 0) {
            writer.writeBytes(getPayload());
        }

        return writer.toByteArray();
    }

    public CoAP.Code getCode() {
        return code;
    }

    public void setCode(CoAP.Code code) {
        this.code = code;
    }
}
