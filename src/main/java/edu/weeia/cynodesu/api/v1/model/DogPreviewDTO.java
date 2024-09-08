package edu.weeia.cynodesu.api.v1.model;

import edu.weeia.cynodesu.domain.Breed;
import edu.weeia.cynodesu.domain.DogStatus;
import edu.weeia.cynodesu.domain.Sex;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Data
public class DogPreviewDTO {

    private Long id;

    private String icon;

    private String name;

    private DogStatus status;

    private Long userId;

    private String username;

    private LocalDateTime createdDate;

    private String sex;

    private Breed breed;

    private Integer age;
}