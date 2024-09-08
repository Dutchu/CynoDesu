package edu.weeia.cynodesu.domain;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Breed extends BaseEntity {
    private String name;
}
