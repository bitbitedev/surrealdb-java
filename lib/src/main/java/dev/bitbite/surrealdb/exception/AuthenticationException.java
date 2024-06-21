package dev.bitbite.surrealdb.exception;

/**
 * The AuthenticationException class represents an exception that is thrown when there is an issue with authentication.
 */
public class AuthenticationException extends RuntimeException {
    
    /**
     * Constructs a new AuthenticationException with the specified detail message.
     * 
     * @param message the detail message
     */
    public AuthenticationException(String message) {
        super(message);
    }

    /**
     * Constructs a new AuthenticationException with the specified detail message and cause.
     * 
     * @param message the detail message
     * @param cause the cause
     */
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new AuthenticationException with the specified cause.
     * 
     * @param cause the cause
     */
    public AuthenticationException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new AuthenticationException with no detail message.
     */
    public AuthenticationException() {
        super();
    }

}
