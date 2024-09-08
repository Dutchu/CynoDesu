package edu.weeia.cynodesu.exceptions;

public class DogCreationException extends RuntimeException {

    // Default constructor
    public DogCreationException() {
        super("Dog creation failed due to invalid or missing data.");
    }

    // Constructor that accepts a custom error message
    public DogCreationException(String message) {
        super(message);
    }

    // Constructor that accepts a custom error message and a cause
    public DogCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructor that accepts a cause
    public DogCreationException(Throwable cause) {
        super(cause);
    }
}
