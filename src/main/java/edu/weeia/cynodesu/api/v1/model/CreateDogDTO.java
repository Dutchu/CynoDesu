package edu.weeia.cynodesu.api.v1.model;

import edu.weeia.cynodesu.domain.Breed;
import edu.weeia.cynodesu.domain.Sex;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Base64;

//TODO: Think of what should user be able to pass to create a dog
@Data
public class CreateDogDTO {
    @NotBlank(message = "Name is required and cannot be empty")
    @Size(min = 3, message = "Name must be at least 3 characters long")
    String name;

    private Breed breed;

    private LocalDate dob;

    private Sex sex;

    private String registrationNo;

    @Size(max = 500, message = "Description is at maximum 500 characters long")
    String content;

    // Adding a MultipartFile for image upload
    MultipartFile image;
}

