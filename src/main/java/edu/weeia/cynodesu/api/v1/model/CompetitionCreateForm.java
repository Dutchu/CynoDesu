package edu.weeia.cynodesu.api.v1.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CompetitionCreateForm {
    @NotNull
    private String name;
    @NotNull
    private String location;
    @NotNull
    private LocalDateTime date;
}
