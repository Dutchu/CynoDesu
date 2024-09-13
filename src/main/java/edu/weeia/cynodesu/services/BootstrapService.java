package edu.weeia.cynodesu.services;

import edu.weeia.cynodesu.api.v1.model.CompetitionCreateForm;
import edu.weeia.cynodesu.api.v1.model.CreateDogDTO;
import edu.weeia.cynodesu.api.v1.model.CreateFacilityDTO;
import edu.weeia.cynodesu.domain.Sex;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
public class BootstrapService {
    private final BreedingFacilityService breedingFacilityService;
    private final DogService dogService;
    private final BreedService breedService;
    private final CompetitionService competitionService;

    public BootstrapService(BreedingFacilityService breedingFacilityService, DogService dogService, BreedService breedService, CompetitionService competitionService) {
        this.breedingFacilityService = breedingFacilityService;
        this.dogService = dogService;
        this.breedService = breedService;
        this.competitionService = competitionService;
    }

    @Transactional
    public void createThingsForUser(String username) {
        createDogsForUser(username, 61);
    }

    @Transactional
    public void createThingsForAdmin(String username) {
        var ids = createDogsForAdmin(username, 3);
        registerForCompetition(ids);
    }

    private void registerForCompetition(Set<Long> ids) {
        var competition = new CompetitionCreateForm();
        competition.setName("Admin testing Competition");
        competition.setLocation("ul. Starorudzka 156, Lodz 93-030");
        competition.setDate(LocalDateTime.now().plusDays(2));
        var comId = competitionService.save(competition);
        ids.forEach(id -> competitionService.registerDogForCompetition(comId, id));
    }

    private Set<Long> createDogsForAdmin(String username, int howMany) {
        Set<Long> createdIds = new HashSet<>();
        var facility = new CreateFacilityDTO(
                "Test Admin Facility",
                "724748803",
                "admin@facility.com",
                "Uć",
                "123456789",
                "123-123-123",
                "Testing Admin Notest",
                null
        );
        var facilityId = breedingFacilityService.create(facility, "admin");

        for (int i = 0; i < howMany; i++) {
            var dog = new CreateDogDTO();
            dog.setName("Testing" + i);
            dog.setContent("Dog" + i);
            dog.setSex(Sex.STUD);
            dog.setDob(LocalDate.of(1960 + i, 1, 1));
            dog.setBreed(breedService.get().get(0));
            dog.setRegistrationNo("XAB1-0000-ABCV-90AS");
            dog.setImage(null);
            createdIds.add(dogService.createDog(facilityId, dog).id());
        }
        return createdIds;
    }

    void createDogsForUser(String _for, int howMany) {

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
