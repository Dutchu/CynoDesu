package edu.weeia.cynodesu.api.v1.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserSignUpDto(
        @Email @NotBlank @Size(min = 5, max = 254) String email,
        @NotBlank @Size(min = 2, max = 30) String firstName,
        @Size(max = 30) String lastName,
        @NotNull @Size(min = 5, max = 20) String username,
        @NotNull @Size(min = 8, max = 30) String pwdPlainText) {
    public static UserSignUpDto validatedFromForm(UserSignUpForm data) {
        return new UserSignUpDto(
                data.getEmail(),
                data.getFirstName(),
                data.getLastName(),
                data.getUsername(),
                data.getPwdPlainText()
        );
    }
}

