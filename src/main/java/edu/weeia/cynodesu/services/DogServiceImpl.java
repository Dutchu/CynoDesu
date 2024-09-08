package edu.weeia.cynodesu.services;

import edu.weeia.cynodesu.api.v1.mapper.DogMapper;
import edu.weeia.cynodesu.api.v1.model.*;
import edu.weeia.cynodesu.domain.*;
import edu.weeia.cynodesu.exceptions.DogCreationException;
import edu.weeia.cynodesu.exceptions.DogImageUploadException;
import edu.weeia.cynodesu.exceptions.ResourceNotFoundException;
import edu.weeia.cynodesu.repositories.DogRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.OptionalDouble;
import java.util.Set;

@Service
public class DogServiceImpl implements DogService {

    protected final DogMapper dogMapper;
    private final DogRepository dogRepository;
    private final FileService fileService;
    private final BreedingFacilityImpl breedingFacilityImpl;
    private final BreedService breedService;


    public DogServiceImpl(DogMapper dogMapper, BreedingFacilityImpl getBreedingFacilityImpl, DogRepository dogRepository, BreedingFacilityImpl breedingFacilityImpl, FileService fileService, BreedService breedService) {
        this.dogMapper = dogMapper;
        this.dogRepository = dogRepository;
        this.breedingFacilityImpl = breedingFacilityImpl;
        this.fileService = fileService;
        this.breedService = breedService;
    }


    @Transactional(readOnly = true)
    @Override
    public Page<DogPreviewDTO> previewAllByFacility(Long facilityId, Pageable pageable) {
        return dogRepository.findAcceptedDogsByFacilityId(facilityId, pageable, DogStatus.ACCEPTED)
                .map(DogMapper.INSTANCE::mapForPreviewListing);
    }


    @Override
    @Cacheable("previewForPublicHomePage")
    public Page<DogPreviewDTO> previewForPublicHomePage(Pageable pageable) {
        return dogRepository.findWithUserAndAttachedFilesByStatus(DogStatus.ACCEPTED, pageable)
                .map(DogMapper.INSTANCE::mapForPreviewListing);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DogPreviewDTO> getAllToReview(Pageable pageable) {
        return dogRepository.findAllByDogStatus(pageable, DogStatus.AWAIT)
                .map(DogMapper.INSTANCE::mapForPreviewListing);
    }

    @Transactional(readOnly = true)
    public DogDetailDto getDetails(Long id) {
        return dogRepository.findById(id)
                .map(DogMapper.INSTANCE::mapForDetailListing)
                .orElseThrow(() -> new ResourceNotFoundException("Couldn't find dog with id: " + id));
    }

    @Override
    public GetDogDTO getDogById(Long id) {
        return null;
    }

    @Override
    public List<GetDogDTO> getAllDogs() {
        return dogRepository.findAll().stream()
                .map(dogMapper::dogToDogDTO)
                .toList();
    }

    @Override
    public GetDogDTO getDogByName(String name) {
        return dogMapper.dogToDogDTO(dogRepository.findByName(name));
    }


    @Override
    @Transactional
    public GetDogDTO createNewDog(GetDogDTO getDogDTO) {
        getDogDTO.setId(null);
        Dog savedDog = dogRepository.save(dogMapper.dogDTOToDog(getDogDTO));
        System.out.println("Deleted dog: " + savedDog);
        return dogMapper.dogToDogDTO(savedDog);
    }

    @Override
    public GetDogDTO deleteDogById(Long id) {
        Dog dog = dogRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        dogRepository.deleteById(id);
        return dogMapper.dogToDogDTO(dog);
    }

    @Override

    @Transactional
    public SavedDogDTO createDog(Long facilityId, CreateDogDTO dogDTO) {
        if (dogDTO == null) {
            throw new DogCreationException("Dog DTO was a null!");
        }

        boolean facilityExists = breedingFacilityImpl.existsById(facilityId);
        if (!facilityExists) {
            throw new ResourceNotFoundException("Can't save dog with owner id: " + facilityId + ". Owner not found!");
        }

        BreedingFacility dogBreedingFacility = breedingFacilityImpl.getFacilityById(facilityId);
        Dog dogToSave = new Dog();

        //TODO: USE MAPSTRUCT FOR MAPPING
        dogToSave.setName(dogDTO.getName());
        dogToSave.setSex(dogDTO.getSex());
        dogToSave.setBreed(dogDTO.getBreed());
        dogToSave.setDob(dogDTO.getDob());
        dogToSave.setBreedingFacility(dogBreedingFacility);
        dogToSave.setContent(dogDTO.getContent());
        dogToSave.setStatus(DogStatus.AWAIT);
        dogToSave.setAttachedFiles(Collections.emptyList());

        //TODO: COPY FUNCTIONAL HANDLING OF EXCEPTIONS FROM BREEDING FACILITY IMPL
        try {
            dogToSave.setIcon(fileService.getDefaultImage(Dog.class));
        } catch (IOException e) {
            throw new DogImageUploadException("Couldn't save dog with default image due to IO exception", e);
        } catch (IllegalArgumentException e) {
            throw new DogImageUploadException("Couldn't save dog with default image due to Illegal argument Exception", e);
        }

        Dog savedDog = dogRepository.save(dogToSave);

        return new SavedDogDTO(
                savedDog.getId(),
                savedDog.getName(),
                savedDog.getContent(),
                savedDog.getIcon(),
                calculateAverageScore(savedDog)
        );
    }

    @Override
    public List<Breed> getBreeds() {
        return breedService.get();
    }

    private Double calculateAverageScore(Dog dog) {
        double result;
        OptionalDouble averageScore = dog.getScores().stream()
                .mapToInt(DogCompetitionScore::getScore)
                .average();

        if (averageScore.isPresent()) {
            result = averageScore.getAsDouble();
        } else {
            //TODO: Use ControllerAdvice to show error message as Pop-Up box on a side.
            result = 0;
        }
        return result;
    }

    public boolean activateDogs(List<Long> dogsId) {

        dogRepository.findAllById(dogsId).stream().map(this::setDogStatusActive).forEach(dogRepository::save);

        return true;
    }

    @Override
    public GetDogDTO createNewDog(String dogName) {
//        Dog dogToCreate = new Dog();
//        dogToCreate.setName(dogName);
        //TODO: Owner must come from session or be present as a parameter in URL
//        dogToCreate.setOwner(ownerRepository.findById(1L).orElseThrow(ResourceNotFoundException::new));
//        Dog createdDog = dogRepository.save(dogToCreate);
//        System.out.println("Dog created: " + createdDog);
//        return dogMapper.dogToDogDTO(createdDog);
        return null;
    }

    private Dog setDogStatusActive(Dog dog) {
        dog.setStatus(DogStatus.ACCEPTED);
        return dog;
    }
}
