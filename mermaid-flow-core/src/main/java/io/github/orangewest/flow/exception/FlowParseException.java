package io.github.orangewest.flow.exception;

public class FlowParseException extends RuntimeException {
    private static final long serialVersionUID = 436824513443895614L;

    public FlowParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public FlowParseException(String message) {
        super(message);
    }
}
