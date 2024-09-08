package edu.weeia.cynodesu.api.v1.model;

import java.util.Base64;

public record GetBreedingFacilityPreviewDTO(
        Long id,
        String name,
        String address,
        String icon
) {
    // Static factory method to create the DTO from byte[]
    public static GetBreedingFacilityPreviewDTO fromBytes(Long id, String name, String address, byte[] icon) {
        String encodedIcon = Base64.getEncoder().encodeToString(icon);
        return new GetBreedingFacilityPreviewDTO(id, name, address, encodedIcon);
    }
}
