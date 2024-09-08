package edu.weeia.cynodesu.services;

import edu.weeia.cynodesu.configuration.BreedsConfig;
import edu.weeia.cynodesu.domain.Breed;
import edu.weeia.cynodesu.repositories.BreedRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class BreedService {

    private final BreedsConfig breedsConfig;
    private final BreedRepository breedRepository;

    public BreedService(BreedsConfig breedsConfig, BreedRepository breedRepository) {
        this.breedsConfig = breedsConfig;
        this.breedRepository = breedRepository;
    }

    public List<Breed> get() {
        return breedRepository.findAll();
    }

    public void saveBreedsToDatabase() {
        Set<String> breeds = breedsConfig.getBreeds();
        breeds.forEach(breedName -> {
            Breed breed = new Breed();
            breed.setName(breedName);
            breedRepository.save(breed);
        });
    }
}
