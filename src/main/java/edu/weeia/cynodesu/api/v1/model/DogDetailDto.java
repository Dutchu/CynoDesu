package edu.weeia.cynodesu.api.v1.model;

import edu.weeia.cynodesu.domain.DogCompetitionScore;
import edu.weeia.cynodesu.domain.ReceivedFile;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public record DogDetailDto(
        Long id,

        Long userId,
        String userName,
        LocalDateTime createdAt,

        String name,
        Long breedingFacilityId,
        String content,
        String icon,
        List<ReceivedFile> docs,
        Set<DogCompetitionScore> scores
) {
}
