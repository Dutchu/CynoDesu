package edu.weeia.cynodesu.exceptions;

public class FacilityImageUploadException extends RuntimeException {

    // Default constructor
    public FacilityImageUploadException() {
        super("Facility image upload failed.");
    }

    // Constructor that accepts a custom error message
    public FacilityImageUploadException(String message) {
        super(message);
    }

    // Constructor that accepts a custom error message and a cause
    public FacilityImageUploadException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructor that accepts a cause
    public FacilityImageUploadException(Throwable cause) {
        super(cause);
    }
}
