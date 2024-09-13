package edu.weeia.cynodesu.services;

import edu.weeia.cynodesu.api.v1.model.CreateFacilityDTO;

import edu.weeia.cynodesu.domain.AppUser;
import edu.weeia.cynodesu.domain.BreedingFacility;
import edu.weeia.cynodesu.exceptions.FacilityImageUploadException;
import edu.weeia.cynodesu.exceptions.ResourceNotFoundException;
import edu.weeia.cynodesu.api.v1.model.GetBreedingFacilityPreviewDTO;
import edu.weeia.cynodesu.repositories.BreedingFacilityRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class BreedingFacilityImpl implements BreedingFacilityService {

    private final BreedingFacilityRepository breedingFacilityRepository;
    private final UserService userService;
    private final FileServiceImage fileServiceImage;

    BreedingFacilityImpl(BreedingFacilityRepository breedingFacilityRepository, UserService userService, FileServiceImage fileServiceImage) {
        this.breedingFacilityRepository = breedingFacilityRepository;
        this.userService = userService;
        this.fileServiceImage = fileServiceImage;
    }

    @Override
    public Long create(CreateFacilityDTO facilityDto, String username) {
        var facilityToSave = new BreedingFacility();
        var user = userService.findWithAuthoritiesByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User: " + username + "was not found when creating a Breeding Facility."));

        byte[] icon = Optional.ofNullable(facilityDto.image())
                .map(image -> handleIOException(image::getBytes, new FacilityImageUploadException("Couldn't upload provided image due to IO Exception.")))
                .orElseGet(() -> handleIOException(() -> fileServiceImage.getDefaultImage(BreedingFacility.class), new FacilityImageUploadException("Couldn't upload default image due to IO Exception.")));

//TODO: USE MAPSTRUCT FOR MAPPING
        facilityToSave.getUsers().add(user);
        facilityToSave.setName(facilityDto.name());
        facilityToSave.setPhoneNumber(facilityDto.phoneNumber());
        facilityToSave.setEmail(facilityDto.email());
        facilityToSave.setAddress(facilityDto.address());
        facilityToSave.setRegistrationNumber(facilityDto.registrationNumber());
        facilityToSave.setLicenseInfo(facilityDto.licenseInfo());
        facilityToSave.setNotes(facilityDto.notes());
        facilityToSave.setIcon(icon);


        var savedFacility = breedingFacilityRepository.save(facilityToSave);

        return savedFacility.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GetBreedingFacilityPreviewDTO> getByUserUsername(String username, Pageable pageable) {
        AppUser user = userService.findWithAuthoritiesByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User with username: " + username + ", not found."));

        var facilities = breedingFacilityRepository.findByUserId(user.getId(), pageable);
        return facilities.map(facility -> GetBreedingFacilityPreviewDTO.fromBytes(
                facility.getId(),
                facility.getName(),
                facility.getAddress(),
                facility.getIcon()
        ));
    }

    @Transactional
    public List<BreedingFacility> findByUserUsername(String username) {
        AppUser user = userService.findWithAuthoritiesByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User with username: " + username + ", not found."));

        return breedingFacilityRepository.findBreedingFacilitiesByUserId(user.getId());
    }

    boolean existsById(Long id) {
        return breedingFacilityRepository.findById(id).isPresent();
    }

    BreedingFacility getFacilityById(Long id) {
        return breedingFacilityRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Could not find Breeding Facility with id:" + id));
    }

    private <T> T handleIOException(ThrowingSupplier<T> supplier, RuntimeException exceptionToThrow) {
        try {
            return supplier.get();
        } catch (IOException e) {
            throw exceptionToThrow;
        }
    }

    @FunctionalInterface
    public interface ThrowingSupplier<T> {
        T get() throws IOException;
    }
}
