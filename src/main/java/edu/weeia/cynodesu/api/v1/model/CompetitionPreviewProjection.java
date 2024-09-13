package edu.weeia.cynodesu.api.v1.model;

import java.time.Instant;
import java.time.LocalDateTime;

public interface CompetitionPreviewProjection {
    Long getId();
    String getName();
    String getLocation();
    Instant getVenueDate();
}
