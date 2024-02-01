package edu.weeia.cynodesu.api.v1.model;

import lombok.Data;

@Data
public class CreateDogDTO {
    private String name;
    private Long ownerId;
}
