package edu.weeia.cynodesu.api.v1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class ListDogDTO {
    List<GetDogDTO> dogs;
}
