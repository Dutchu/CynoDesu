package edu.weeia.cynodesu.services;

import edu.weeia.cynodesu.api.v1.model.*;
import edu.weeia.cynodesu.domain.Breed;
import edu.weeia.cynodesu.domain.Dog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface DogService {
    List<GetDogDTO> getAllDogs();
    GetDogDTO getDogByName(String Name);
    GetDogDTO createNewDog(GetDogDTO getDogDTO);
    GetDogDTO createNewDog(String dogName);
    GetDogDTO deleteDogById(Long id);
    List<DogPreviewDTO> findAllByUser(String username);
    DogDetailDto getDetails(Long id) ;
    Page<DogPreviewDTO> getAllToReview(Pageable pageable);
    Page<DogPreviewDTO> getAllByCompetition(Long id, Pageable pageable);
    Page<DogPreviewDTO> previewAllByFacility(Long facilityId, Pageable pageable);
    Dog findDogById(Long id);
    SavedDogDTO createDog(Long facilityId, CreateDogDTO createDogDTO);

    List<Breed> getBreeds();

    Page<DogPreviewDTO> previewForPublicHomePage(Pageable pageable);

    public boolean activateDogs(List<Long> dogsId);
}
