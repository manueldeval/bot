package org.deman.bot.aiml;

/**
 * Created by deman on 12/01/17.
 */
public class AimlParserException extends Exception {

    public AimlParserException() {
    }

    public AimlParserException(String message) {
        super(message);
    }

    public AimlParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public AimlParserException(Throwable cause) {
        super(cause);
    }
}
