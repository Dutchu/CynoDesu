package edu.weeia.cynodesu.api.v1.model;

import lombok.Data;
import edu.weeia.cynodesu.domain.ReceivedFile;
import edu.weeia.cynodesu.domain.DogCompetitionScore;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


@Data
public class CompetitionScoreForm {
    //Dog Details
    String icon;
    Long dogId;
    String dogName;
    Long userId;
    String userName;
    LocalDateTime createdAt;
    Long breedingFacilityId;
    //Get BreedingFacility Name
    String breedingFacilityName;
    String content;
    List<ReceivedFile> docs;
    Set<DogCompetitionScore> scores;
    //Score
    int newScore;
    int newRank;
}
