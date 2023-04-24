package Protocol;

import java.util.HashMap;
import java.util.Map;

import CoAPException.MessageFormatException;
public final class CoAP {
    public static final int VERSION = 1;
    public static final int DEFAULT_PORT = 5683;
    public static final int VERSION_BITS_LENGTH = 2;
    public static final int TYPE_BITS_LENGTH = 2;
    public static final int TKL_BITS_LENGTH = 4;
    public static final int CODE_BITS_LENGTH = 8;
    public static final int CODE_CLASS_BITS_LENGTH = 3;
    public static final int CODE_DETAIL_BITS_LENGTH = 5;
    public static final int MESSAGE_ID_BITS_LENGTH = 16;
    public static final int OPTION_DELTA_BITS_LENGTH = 4;
    public static final int OPTION_LENGTH_BITS_LENGTH = 4;
    public static final String PROTOCAL_SCHEME = "coap";
    public static final String PROTOCOL_SCHEME_SEPARATOR = "//";
    public static final String SCHEME_SEPARATOR = "/";

    public static final byte END_HEADER_BYTE = (byte) (0xFF);

    private static final Map<String, ResponseCode> responseCodeMap = new HashMap<>();
    private CoAP() {

    }

    public static int getCodeClass(int code) {
        return (code & 0b11100000) >> 5;
    }

    public static int getCodeDetail(int code) {
        return code & 0b00011111;
    }
    public enum Type {
        CON(0),
        NON(1),
        ACK(2),
        RST(3);

        private int value;

        private Type(int value) {
            this.value = value;
        }

        public static Type valueOf(int value) throws MessageFormatException {
            switch (value) {
                case 0 -> {
                    return CON;
                }
                case 1 -> {
                    return NON;
                }
                case 2 -> {
                    return ACK;
                }
                case 3 -> {
                    return RST;
                }
                default -> {
                    throw new MessageFormatException("Unknown message type");
                }
            }
        }
    }

    public enum Code {
        GET(1),
        POST(2),
        PUT(3),
        DELETE(4);

        public final int value;

        private Code(final int value) {
            this.value = value;
        }

        public static Code valueOf(final int value) throws MessageFormatException {
            switch (value) {
                case 1 -> {
                    return GET;
                }
                case 2 -> {
                    return POST;
                }
                case 3 -> {
                    return PUT;
                }
                case 4 -> {
                    return DELETE;
                }
                default -> {
                    throw new MessageFormatException("Unknown Protocol.CoAP request code");
                }
            }
        }
    }

    public enum CodeClass {
        REQUEST(0),
        SUCCESS(2),
        ERROR(4);

        public final int value;

        private CodeClass(final int value) {
            this.value = value;
        }
    }

    public enum ResponseCode {
        //success code
        UNKNOWN_SUCCESS_CODE(CodeClass.SUCCESS, 0),
        CREATE(CodeClass.SUCCESS, 1),
        DELETE(CodeClass.SUCCESS, 2),
        CHANGED(CodeClass.SUCCESS, 4),
        CONTENT(CodeClass.SUCCESS, 5),

        //error code
        BAD_REQUEST(CodeClass.ERROR, 0),
        BAD_OPTION(CodeClass.ERROR, 2),
        NOT_FOUND(CodeClass.ERROR, 4),
        METHOD_NOT_ALLOWED(CodeClass.ERROR, 5);


        public final int value;
        public final int codeClass;
        public final int codeDetail;

        private ResponseCode(CodeClass codeClass, final int codeDetail) {
            this.codeClass = codeClass.value;
            this.codeDetail = codeDetail;
            this.value = codeClass.value << 5 | codeDetail;
        }

        public static ResponseCode valueOf(final int value)  throws MessageFormatException {
            int codeClass = (value & 0b11100000) >> 5;
            int codeDetail = (value & 0b00011111);
            switch (codeClass) {
                case 2 -> {
                    return valueOfSuccessCode(codeDetail);
                }
                case 4 -> {
                    return valueOfErrorCode(codeDetail);
                }
                default -> {
                    throw new MessageFormatException("Not a Coap response code");
                }
            }
        }

        private static ResponseCode valueOfSuccessCode(final int codeDetail) {
            switch (codeDetail) {
                case 1 -> {
                    return CREATE;
                }
                case 2 -> {
                    return DELETE;
                }
                case 4 -> {
                    return CHANGED;
                }
                case 5 -> {
                    return CONTENT;
                }
                default -> {
                    return UNKNOWN_SUCCESS_CODE;
                }
            }
        }

        private static ResponseCode valueOfErrorCode(final int codeDetail) {
            switch (codeDetail) {
                case 2 -> {
                    return BAD_OPTION;
                }
                case 4 -> {
                    return NOT_FOUND;
                }
                case 5 -> {
                    return METHOD_NOT_ALLOWED;
                }
                default -> {
                    return BAD_REQUEST;
                }
            }
        }
    }
}

