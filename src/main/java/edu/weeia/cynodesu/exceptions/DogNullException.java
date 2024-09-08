package edu.weeia.cynodesu.exceptions;

public class DogNullException extends RuntimeException {

    // Default constructor
    public DogNullException() {
        super("Dog reference was null.");
    }

    // Constructor that accepts a custom error message
    public DogNullException(String message) {
        super(message);
    }

    // Constructor that accepts a custom error message and a cause
    public DogNullException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructor that accepts a cause
    public DogNullException(Throwable cause) {
        super(cause);
    }
}


