package edu.weeia.cynodesu.services;

import edu.weeia.cynodesu.api.v1.mapper.DogMapper;
import edu.weeia.cynodesu.api.v1.mapper.OwnerMapper;
import edu.weeia.cynodesu.api.v1.model.GetDogDTO;
import edu.weeia.cynodesu.domain.Dog;
import edu.weeia.cynodesu.repositories.DogRepository;
import edu.weeia.cynodesu.repositories.OwnerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DogServiceImpl implements DogService {

    protected final DogMapper dogMapper;
    private final DogRepository dogRepository;
    private final OwnerMapper ownerMapper;
    private final OwnerRepository ownerRepository;

    //TODO: Owner must come from session or be present as a parameter in URL
    private final OwnerService ownerService;

    public DogServiceImpl(DogMapper dogMapper, DogRepository dogRepository, OwnerService ownerService, OwnerMapper ownerMapper, OwnerRepository ownerRepository) {
        this.dogMapper = dogMapper;
        this.dogRepository = dogRepository;
        //TODO: Owner must come from session or be present as a parameter in URL
        this.ownerService = ownerService;
        this.ownerMapper = ownerMapper;
        this.ownerRepository = ownerRepository;
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
    public GetDogDTO createNewDog(String dogName) {
        Dog dogToCreate = new Dog();
        dogToCreate.setName(dogName);
        //TODO: Owner must come from session or be present as a parameter in URL
        dogToCreate.setOwner(ownerRepository.findById(1L).orElseThrow(ResourceNotFoundException::new));
        Dog createdDog = dogRepository.save(dogToCreate);
        System.out.println("Dog created: " + createdDog);
        return dogMapper.dogToDogDTO(createdDog);
    }
}
