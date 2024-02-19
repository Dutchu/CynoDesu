package edu.weeia.cynodesu.api.v1.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileUpdateDTO {

    @Email
    private String email;

    @NotBlank
    @Size(min = 2, max = 30)
    private String firstName;

    @Size(max = 30)
    private String lastName;

}
