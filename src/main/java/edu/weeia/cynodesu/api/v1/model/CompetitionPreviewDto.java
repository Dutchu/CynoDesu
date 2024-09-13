package edu.weeia.cynodesu.api.v1.model;

import java.time.LocalDateTime;

public record CompetitionPreviewDto(
        Long id,
        String name,
        String location,
        LocalDateTime dateTime
) {
}
