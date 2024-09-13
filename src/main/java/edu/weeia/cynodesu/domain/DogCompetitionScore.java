package edu.weeia.cynodesu.domain;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class DogCompetitionScore {

    @EmbeddedId
    private DogCompetitionScoreId id;

    @ManyToOne
    @MapsId("dogId")
    private Dog dog;

    @ManyToOne
    @MapsId("competitionId")
    private Competition competition;

    private int score;

    private int rank;

    public DogCompetitionScore(Competition competition, Dog dog) {
        this.dog = dog;
        this.competition = competition;
        this.id = new DogCompetitionScoreId(dog.getId(), competition.getId()); // Properly initialize ID here
    }

    private String note;
}
