package edu.weeia.cynodesu.services;

import edu.weeia.cynodesu.api.v1.model.CompetitionCreateForm;
import edu.weeia.cynodesu.api.v1.model.CompetitionPreviewDto;
import edu.weeia.cynodesu.api.v1.model.CompetitionScoreForm;
import edu.weeia.cynodesu.domain.Competition;
import edu.weeia.cynodesu.domain.DogCompetitionScoreId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;

public interface CompetitionService {
    Page<CompetitionPreviewDto> getUsersCompetitions(String username, Pageable pageable);
    Long save(CompetitionCreateForm form);
    Page<CompetitionPreviewDto> getAllCompetitions(Pageable pageable);
    Long registerDogForCompetition(Long competitionId, Long dogId);
    CompetitionScoreForm getCompetitionScoreFormInformation(Long competitionId, Long dogId);
    Competition findCompetition(Long id);
    Set<DogCompetitionScoreId> scoreDog(Long competitionId, Long dogId, CompetitionScoreForm scoreForm);
}
