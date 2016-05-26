package org.openbaton.cli.exceptions;

/**
 * Created by tbr on 26.05.16.
 */
public class CommandLineException extends Exception {

    public CommandLineException() {
    }

    public CommandLineException (String message) {
        super(message);
    }

    public CommandLineException (Throwable cause) {
        super(cause);
    }

    public CommandLineException (String message, Throwable cause) {
        super(message, cause);
    }

}
