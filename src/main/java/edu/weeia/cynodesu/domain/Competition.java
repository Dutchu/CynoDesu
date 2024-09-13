package edu.weeia.cynodesu.domain;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Competition implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String name;
    @NotNull
    private String location;
    @NotNull
    private Instant venueDate;

    @CreatedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id", updatable = false, nullable = true)
    private AppUser createdByUser;

    @CreatedDate
    @Column(name = "created_date", updatable = false, nullable = false)
    private Instant createdDate;

    @LastModifiedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_modified_by_user_id")
    private AppUser lastModifiedByUser;

    @LastModifiedDate
    @Column(name = "last_modified_date", insertable = false)
    private Instant lastModifiedDate;

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
        DogCompetitionScore score = new DogCompetitionScore(this, dog);
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
