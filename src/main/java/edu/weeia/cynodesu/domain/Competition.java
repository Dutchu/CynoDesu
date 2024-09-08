package edu.weeia.cynodesu.domain;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
public class Competition extends BaseAuditingEntity implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String location;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(
            mappedBy = "competition",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<DogCompetitionScore> scores = new HashSet<>();

    public Competition(String name, String location) {
        this.name = name;
        this.location = location;
    }

    //https://vladmihalcea.com/the-best-way-to-map-a-many-to-many-association-with-extra-columns-when-using-jpa-and-hibernate/
    public void addDog(Dog dog) {
        DogCompetitionScoreId id = new DogCompetitionScoreId(dog.getId(), this.id);
        DogCompetitionScore score = new DogCompetitionScore(this, dog);
        score.setId(id);
        scores.add(score);
        dog.getScores().add(score);
    }

    @Transactional
    public void removeDog(Dog dog) {
        for (Iterator<DogCompetitionScore> iterator = scores.iterator(); iterator.hasNext();) {
            DogCompetitionScore score = iterator.next();

            if (score.getCompetition().equals(this) && score.getDog().equals(dog)) {
                iterator.remove();
                score.getDog().getScores().remove(score);
                score.setCompetition(null);
                score.setDog(null);
            }
        }
    }
}
