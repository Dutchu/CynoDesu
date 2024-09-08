package edu.weeia.cynodesu.services;

import edu.weeia.cynodesu.api.v1.model.CreateFacilityDTO;
import edu.weeia.cynodesu.api.v1.model.GetBreedingFacilityPreviewDTO;
import edu.weeia.cynodesu.repositories.BreedingFacilityPreview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BreedingFacilityService {
    Long create(CreateFacilityDTO facilityDto, String username);
    Page<GetBreedingFacilityPreviewDTO> getByUserUsername(String username, Pageable pageable);
}
