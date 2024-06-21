package dev.bitbite.surrealdb.exception;

/**
 * The SurrealException class represents an exception specific to the SurrealDB library.
 * It extends the RuntimeException class, making it an unchecked exception.
 */
public class SurrealException extends RuntimeException {
    
    /**
     * Constructs a new SurrealException with the specified detail message.
     * 
     * @param message the detail message (which is saved for later retrieval by the getMessage() method)
     */
    public SurrealException(String message) {
        super(message);
    }

    /**
     * Constructs a new SurrealException with the specified detail message and cause.
     * 
     * @param message the detail message (which is saved for later retrieval by the getMessage() method)
     * @param cause the cause (which is saved for later retrieval by the getCause() method)
     */
    public SurrealException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new SurrealException with the specified cause.
     * 
     * @param cause the cause (which is saved for later retrieval by the getCause() method)
     */
    public SurrealException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new SurrealException with no detail message.
     */
    public SurrealException() {
        super();
    }

}
