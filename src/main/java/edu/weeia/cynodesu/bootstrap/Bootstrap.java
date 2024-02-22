package edu.weeia.cynodesu.bootstrap;

import edu.weeia.cynodesu.domain.Dog;
import edu.weeia.cynodesu.domain.Owner;
import edu.weeia.cynodesu.repositories.DogRepository;
import edu.weeia.cynodesu.repositories.OwnerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Bootstrap implements CommandLineRunner {

    private final DogRepository dogRepository;
    private final OwnerRepository ownerRepository;

    public Bootstrap(DogRepository dogRepository, OwnerRepository ownerRepository) {
        this.dogRepository = dogRepository;
        this.ownerRepository = ownerRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        logUser();
        loadOwners();
        loadDogs();
    }

    private void logUser() {

    }

    private void loadOwners() {
        Owner owner1 = new Owner();
        owner1.setFirstName("Jan");
        owner1.setLastName("Kowalski");

        Owner owner2 = new Owner();
        owner2.setFirstName("Adam");
        owner2.setLastName("Nowak");

        Owner owner3 = new Owner();
        owner3.setFirstName("Anna");
        owner3.setLastName("Kowalska");

        Owner owner4 = new Owner();
        owner4.setFirstName("Maria");
        owner4.setLastName("Nowak");

        ownerRepository.save(owner1);
        ownerRepository.save(owner2);
        ownerRepository.save(owner3);
        ownerRepository.save(owner4);

        System.out.println("Owners loaded: " + ownerRepository.count());
    }

    private void loadDogs() {
        Dog dog1 = new Dog();
        dog1.setName("Burek");
        dog1.setContent("Labrador");
        dog1.setOwner(ownerRepository.findById(1L).get());

        Dog dog2 = new Dog();
        dog2.setName("Azor");
        dog2.setContent("Owczarek Niemiecki");
        dog2.setOwner(ownerRepository.findById(2L).get());

        Dog dog3 = new Dog();
        dog3.setName("Reksio");
        dog3.setContent("Mieszaniec");
        dog3.setOwner(ownerRepository.findById(3L).get());

        Dog dog4 = new Dog();
        dog4.setName("Rex");
        dog4.setContent("Owczarek Niemiecki");
        dog4.setOwner(ownerRepository.findById(4L).get());

        dogRepository.save(dog1);
        dogRepository.save(dog2);
        dogRepository.save(dog3);
        dogRepository.save(dog4);

        System.out.println("Dogs loaded: " + dogRepository.count());
    }
}
