package edu.weeia.cynodesu.api.v1.model;

public record SavedDogDTO(
        Long id,
        String name,
        String content,
        byte[] image,
        Double averageScore
) {
}
