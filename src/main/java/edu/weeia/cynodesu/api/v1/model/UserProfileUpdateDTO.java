package edu.weeia.cynodesu.api.v1.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public record UserProfileUpdateDTO (
        @Email @NotBlank @Size(min = 5, max = 254) String email,
        @NotBlank @Size(min = 2, max = 30) String firstName,
        @Size(max = 30) String lastName) {
}
