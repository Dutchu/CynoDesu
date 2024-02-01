package edu.weeia.cynodesu.api.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Set;

@Data
public class OwnerDTO {
    private String firstName;
    private String lastName;

    @JsonProperty("owner_url")
    private String ownerURL;

    private Set<GetDogDTO> dogs;
}
