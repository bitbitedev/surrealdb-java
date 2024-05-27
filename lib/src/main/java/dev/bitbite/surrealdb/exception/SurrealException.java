package dev.bitbite.surrealdb.exception;

public class SurrealException extends RuntimeException {
    
    public SurrealException(String message) {
        super(message);
    }

    public SurrealException(String message, Throwable cause) {
        super(message, cause);
    }

    public SurrealException(Throwable cause) {
        super(cause);
    }

    public SurrealException() {
        super();
    }

}
