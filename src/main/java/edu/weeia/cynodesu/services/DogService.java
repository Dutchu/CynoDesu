package edu.weeia.cynodesu.services;

import edu.weeia.cynodesu.api.v1.model.CreateDogDTO;
import edu.weeia.cynodesu.api.v1.model.DogPreviewDTO;
import edu.weeia.cynodesu.api.v1.model.GetDogDTO;
import edu.weeia.cynodesu.domain.Dog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DogService {
    List<GetDogDTO> getAllDogs();
    GetDogDTO getDogByName(String Name);
    GetDogDTO createNewDog(GetDogDTO getDogDTO);
    GetDogDTO createNewDog(String dogName);
    GetDogDTO deleteDogById(Long id);

    List<GetDogDTO> getAllToReview(PageRequest createdDate);

    List<GetDogDTO> previewAllByUser(PageRequest createdDate, Long id);

    GetDogDTO getDogById(Long id);

    Page<DogPreviewDTO> previewAll(Pageable pageable);

    Dog createDog(CreateDogDTO articleDto);

    Page<DogPreviewDTO> previewForPublicHomePage(Pageable pageable);
}
