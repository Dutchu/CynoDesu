package edu.weeia.cynodesu.services;

import edu.weeia.cynodesu.api.v1.model.GetDogDTO;

import java.util.List;

public interface DogService {
    List<GetDogDTO> getAllDogs();
    GetDogDTO getDogByName(String Name);
    GetDogDTO createNewDog(GetDogDTO getDogDTO);
    GetDogDTO createNewDog(String dogName);
    GetDogDTO deleteDogById(Long id);
}
