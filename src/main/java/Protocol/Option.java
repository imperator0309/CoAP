package Protocol;

import CoAPException.*;

public class Option {
    private int optionDelta;
    private int optionLength;
    private OptionDefinition definition;
    byte[] value = null;


    public Option(int preOptionNumber ,int delta, byte[] value) throws MessageFormatException {
        this.optionDelta = delta;
        int valueLength = 0;
        if (value != null) {
            valueLength = value.length;
        }
        this.optionLength = valueLength;
        this.value = value;
        switch (delta + preOptionNumber) {
            case 6 -> {
                setDefinition(OptionDefinition.OBSERVE);
            }
            case 11 -> {
                setDefinition(OptionDefinition.URI_PATH);
            }
            default -> {
                throw new MessageFormatException("Unknown Option Definition");
            }
        }
    }

    public int getOptionDelta() {
        return optionDelta;
    }

    public void setOptionDelta(int optionDelta) {
        this.optionDelta = optionDelta;
    }

    public int getOptionLength() {
        return optionLength;
    }

    public void setOptionLength(int optionLength) {
        this.optionLength = optionLength;
    }

    public OptionDefinition getDefinition() {
        return definition;
    }

    public void setDefinition(OptionDefinition definition) {
        this.definition = definition;
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

    public enum OptionDefinition {
        URI_PATH(6),
        OBSERVE(10);

        public final int value;

        private OptionDefinition(final int value) {
            this.value = value;
        }

        public OptionDefinition valueOf(int value) throws MessageFormatException {
            switch (value) {
                case 6: return OBSERVE;
                case 11: return URI_PATH;
                default: throw new MessageFormatException("Unknown Option Definition");
            }
        }
    }

    public static void main(String[] args) {
    }
}
