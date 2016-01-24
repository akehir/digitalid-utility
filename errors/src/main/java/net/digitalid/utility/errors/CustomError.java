package net.digitalid.utility.errors;

/**
 * This class is the parent of all custom errors.
 * 
 * @see ShouldNeverHappenError
 */
public abstract class CustomError extends Error {
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new custom error with the given nullable message and nullable cause.
     */
    protected CustomError(String message, Throwable cause) {
        super(message == null ? "An error occurred." : message, cause);
    }
    
}
