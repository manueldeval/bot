package org.deman.bot.tags;

/**
 * Created by deman on 11/01/17.
 */
public class MalformedAimlException extends Exception {
    public MalformedAimlException() {
    }

    public MalformedAimlException(String message) {
        super(message);
    }

    public MalformedAimlException(String message, Throwable cause) {
        super(message, cause);
    }

    public MalformedAimlException(Throwable cause) {
        super(cause);
    }
}
