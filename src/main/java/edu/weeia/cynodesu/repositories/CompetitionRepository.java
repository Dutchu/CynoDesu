package edu.weeia.cynodesu.repositories;

import edu.weeia.cynodesu.api.v1.model.CompetitionPreviewDto;
import edu.weeia.cynodesu.api.v1.model.CompetitionPreviewProjection;
import edu.weeia.cynodesu.domain.Competition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CompetitionRepository extends JpaRepository<Competition, Long> {

    @Query("""
            SELECT DISTINCT c FROM Competition c
                   JOIN DogCompetitionScore dcs ON dcs.id.competitionId = c.id
                   JOIN Dog d ON dcs.id.dogId = d.id
                   JOIN BreedingFacility bf ON d.breedingFacility.id = bf.id
                   WHERE bf.id IN :facilityIds
            """)
    Page<CompetitionPreviewDto> findAllByBreedingFacilityIds(@Param("facilityIds") Set<Long> breedingFacilityIds, Pageable pageable);

    @Query("SELECT c FROM Competition c")
    Page<CompetitionPreviewProjection> findAllPreviewPages(Pageable pageable);

}
