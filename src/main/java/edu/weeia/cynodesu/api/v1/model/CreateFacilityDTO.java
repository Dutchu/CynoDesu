package edu.weeia.cynodesu.api.v1.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public record CreateFacilityDTO(

        @NotBlank(message = "Name is required and cannot be empty")
        @Size(min = 3, message = "Name must be at least 3 characters long")
        String name,

        String phoneNumber,
        String email,
        String address,
//        @Pattern(regexp = "^[a-zA-Z0-9]{10}$", message = "Registration number must be exactly 10 alphanumeric characters.")
        String registrationNumber,
        String licenseInfo,

        @Size(max = 500, message = "Description is at maximum 500 characters long")
        String notes,
        MultipartFile image

) {
}
