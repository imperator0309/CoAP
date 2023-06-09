package Protocol;

import CoAPException.*;
import java.io.ByteArrayOutputStream;

public class Response extends Message {
    CoAP.ResponseCode code;

    public Response() {

    }

    public Response(Request request, CoAP.Type type) {
        setVersion(request.getVersion());
        setType(type);
        setTKL(request.getTKL());
        setMessageID(request.getMessageID() + 1);
        setToken(request.getToken());
        setOption(null);
    }

    public Response(CoAP.Type type, CoAP.ResponseCode code, int mid, Token token, Option option, byte[] payload) {
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

    public Response(byte[] data) throws MessageFormatException {
        try {
            int firstByte= data[0];
            int version = (firstByte >> 6) & 0b00000011;
            int typeCode = (firstByte >> 4) & 0b00000011;
            int tkl = (firstByte & 0b00001111);

            int codeByte = data[1];

            int mid = 0;
            for (int i = 2; i < 4; i++) {
                int tmp = data[i];
                mid = (mid << 8) + (tmp & 0xFF);
            }

            CoAP.Type type = CoAP.Type.valueOf(typeCode);
            CoAP.ResponseCode code = CoAP.ResponseCode.valueOf(codeByte);
            Token token = new Token();

            int index = 4;

            if (tkl > 0) {
                byte[] tokenData = new byte[tkl];
                for (int i = 0; i < tkl; i++) {
                    tokenData[i] = data[index + i];
                    index++;
                }
                token.setTokenBytes(tokenData);
            }

            setVersion(version);
            setType(type);
            setTKL(tkl);
            setCode(code);
            setMessageID(mid);
            setToken(token);

            if (index < data.length) { //has option
                if (data[index] != CoAP.PAYLOAD_MAKER) {
                    int optionInfo = data[index];
                    int delta = (optionInfo >> 4) & 0b00001111;
                    int length = optionInfo & 0b00001111;

                    index++; //next to option value first byte;

                    byte[] optionValue = null;

                    if (length > 0) {
                        optionValue = new byte[length];

                        for (int i = 0; i < length; i++) {
                            optionValue[i] = data[index];
                            index++;
                        }
                    }

                    Option option = new Option(0, delta, optionValue);
                    setOption(option);
                }

                if (data.length - index > 0) {
                    if (data.length - index == 1) {
                        throw new MessageFormatException("null payload with payload marker");
                    } else {
                        if (data[index] != CoAP.PAYLOAD_MAKER) {
                            throw new MessageFormatException("payload without payload marker");
                        } else {
                            index++; //skip end header marking byte

                            byte[] payload = new byte[data.length - index];
                            for (int i = 0; i < payload.length; i++) {
                                payload[i] = data[index];
                                index++;
                            }
                            setPayload(payload);
                        }
                    }
                }
            }
        } catch (MessageFormatException e) {
            System.err.println("Failed to Create Response");
        } catch (IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
    }

    public byte[] toByteArray() throws MessageFormatException {
        ByteArrayOutputStream writer = new ByteArrayOutputStream();

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

        byte[] defaultHeader = new byte[4];
        defaultHeader[0] = (byte) ((CoAP.VERSION << 6) | (type << 4) | (this.getTKL() & 0b00001111));
        defaultHeader[1] = (byte) (code.value & 0xFF);
        defaultHeader[2] = (byte) ((getMessageID() >> 8) & 0b11111111);
        defaultHeader[3] = (byte) (getMessageID() & 0b11111111);

        writer.writeBytes(defaultHeader);

        if (this.getTKL() > 0) {
            byte[] token = this.getToken().getTokenBytes();
            writer.writeBytes(token);
        }

        if (getOption() != null && getOption().getValue() != null) {
            byte optionInfo = (byte) ((getOption().getOptionDelta() << 4) & 0b11110000 |
                    (getOption().getOptionLength() & 0b00001111));

            byte[] option = new byte[getOption().getOptionLength() + 1];
            option[0] = optionInfo;
            for (int i = 0; i < getOption().getOptionLength(); i++) {
                option[i + 1] = getOption().getValue()[i];
            }
            writer.writeBytes(option);
        }

        if (getPayload() != null && getPayload().length > 0) { //has payload
            byte[] end = new byte[1];
            end[0] = CoAP.PAYLOAD_MAKER;

            writer.writeBytes(end);
            writer.writeBytes(getPayload());
        }

        return writer.toByteArray();
    }

    public CoAP.ResponseCode getCode() {
        return code;
    }

    public void setCode(CoAP.ResponseCode code) {
        this.code = code;
    }
}
