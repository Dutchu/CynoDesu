package edu.weeia.cynodesu.services;

import edu.weeia.cynodesu.api.v1.model.CreateDogDTO;
import edu.weeia.cynodesu.api.v1.model.CreateFacilityDTO;
import edu.weeia.cynodesu.domain.Breed;
import edu.weeia.cynodesu.domain.Sex;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class BootstrapService {
    private final BreedingFacilityService breedingFacilityService;
    private final DogService dogService;
    private final BreedService breedService;

    public BootstrapService(BreedingFacilityService breedingFacilityService, DogService dogService, BreedService breedService) {
        this.breedingFacilityService = breedingFacilityService;
        this.dogService = dogService;
        this.breedService = breedService;
    }

    @Transactional
    public void createThingsForUser(String username) {
        createDogs(username, 61);
    }

    void createDogs(String _for, int howMany) {

        var facility = new CreateFacilityDTO(
                "Test Facility",
                "724748803",
                "breeding@facility.com",
                "Uć",
                "123456789",
                "123-123-123",
                "Testing notest",
                null
        );
        var facilityId = breedingFacilityService.create(facility, "user");
        breedingFacilityService.create(facility, "admin");

        for (int i = 0; i < howMany; i++) {
            var dog = new CreateDogDTO();
            dog.setName("Testing" + i);
            dog.setContent("Dog" + i);
            dog.setSex(Sex.STUD);
            dog.setDob(LocalDate.of(1960 + i, 1, 1));
            dog.setBreed(breedService.get().get(0));
            dog.setRegistrationNo("XAB1-0000-ABCV-90AS");
            dog.setImage(null);
            dogService.createDog(facilityId, dog);
        }
    }
}
