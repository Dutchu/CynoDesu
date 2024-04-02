package edu.weeia.cynodesu.services;

import edu.weeia.cynodesu.api.v1.mapper.DogMapper;
import edu.weeia.cynodesu.api.v1.mapper.OwnerMapper;
import edu.weeia.cynodesu.api.v1.model.CreateDogDTO;
import edu.weeia.cynodesu.api.v1.model.DogPreviewDTO;
import edu.weeia.cynodesu.api.v1.model.GetDogDTO;
import edu.weeia.cynodesu.domain.Dog;
import edu.weeia.cynodesu.domain.DogStatus;
import edu.weeia.cynodesu.exceptions.ResourceNotFoundException;
import edu.weeia.cynodesu.repositories.DogRepository;
import edu.weeia.cynodesu.repositories.OwnerRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DogServiceImpl implements DogService {

    protected final DogMapper dogMapper;
    private final DogRepository dogRepository;
    private final OwnerMapper ownerMapper;
    private final OwnerRepository ownerRepository;

    @Override
    public Page<DogPreviewDTO> previewAll(Pageable pageable) {
        return dogRepository.findWithAllByStatus(pageable, DogStatus.ACCEPTED)
                .map(DogMapper.INSTANCE::mapForPreviewListing);
    }

    //TODO: Owner must come from session or be present as a parameter in URL
    private final OwnerService ownerService;

    @Override
    @Cacheable("previewForPublicHomePage")
    public Page<DogPreviewDTO> previewForPublicHomePage(Pageable pageable) {
        return dogRepository.findWithUserAndAttachedFilesByStatus(DogStatus.ACCEPTED, pageable)
                .map(DogMapper.INSTANCE::mapForPreviewListing);
    }

    public DogServiceImpl(DogMapper dogMapper, DogRepository dogRepository, OwnerService ownerService, OwnerMapper ownerMapper, OwnerRepository ownerRepository) {
        this.dogMapper = dogMapper;
        this.dogRepository = dogRepository;
        //TODO: Owner must come from session or be present as a parameter in URL
        this.ownerService = ownerService;
        this.ownerMapper = ownerMapper;
        this.ownerRepository = ownerRepository;
    }

    @Override
    public List<GetDogDTO> getAllToReview(PageRequest createdDate) {
        return null;
    }

    @Override
    public List<GetDogDTO> previewAllByUser(PageRequest createdDate, Long id) {
        return null;
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
    public Dog createDog(CreateDogDTO articleDto) {
        return null;
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
}
