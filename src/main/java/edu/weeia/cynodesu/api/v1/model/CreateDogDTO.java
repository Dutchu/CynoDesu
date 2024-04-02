package edu.weeia.cynodesu.api.v1.model;

import lombok.Data;

@Data
//TODO: Think of what should user be able to pass to create a dog
public class CreateDogDTO {
    private Long ownerId;
    private String name;
    private String content;
}
