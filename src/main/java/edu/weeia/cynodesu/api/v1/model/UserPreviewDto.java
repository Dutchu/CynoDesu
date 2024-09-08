package edu.weeia.cynodesu.api.v1.model;

import java.time.LocalDateTime;

public record UserPreviewDto(
        Long id,
        String username,
        String firstName,
        String lastName,
        String email,
        String icon,
        LocalDateTime createdAt
) {
}
