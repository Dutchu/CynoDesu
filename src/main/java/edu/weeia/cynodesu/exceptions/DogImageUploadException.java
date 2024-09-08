package edu.weeia.cynodesu.exceptions;

public class DogImageUploadException extends RuntimeException {

    // Default constructor
    public DogImageUploadException() {
        super("Dog image upload failed.");
    }

    // Constructor that accepts a custom error message
    public DogImageUploadException(String message) {
        super(message);
    }

    // Constructor that accepts a custom error message and a cause
    public DogImageUploadException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructor that accepts a cause
    public DogImageUploadException(Throwable cause) {
        super(cause);
    }
}
