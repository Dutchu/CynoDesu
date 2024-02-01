package edu.weeia.cynodesu.api.v1.model;

import lombok.Data;

@Data
public class GetDogDTO {
    private Long id;
    private String name;
    private OwnerDTO owner;
}
