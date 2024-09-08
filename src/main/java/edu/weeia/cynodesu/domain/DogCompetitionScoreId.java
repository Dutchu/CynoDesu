package edu.weeia.cynodesu.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class DogCompetitionScoreId implements Serializable {
    @Column(name = "dog_id")
    private Long dogId;

    @Column(name = "competition_id")
    private Long competitionId;
}
