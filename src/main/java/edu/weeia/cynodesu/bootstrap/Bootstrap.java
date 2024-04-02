package edu.weeia.cynodesu.bootstrap;

import edu.weeia.cynodesu.domain.*;
import edu.weeia.cynodesu.repositories.AppUserRepository;
import edu.weeia.cynodesu.repositories.AuthorityRepository;
import edu.weeia.cynodesu.repositories.DogRepository;
import edu.weeia.cynodesu.repositories.OwnerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class Bootstrap implements CommandLineRunner {

    private final DogRepository dogRepository;
    private final OwnerRepository ownerRepository;
    private final AppUserRepository appUserRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    public Bootstrap(DogRepository dogRepository, OwnerRepository ownerRepository, AppUserRepository appUserRepository, AuthorityRepository authorityRepository, PasswordEncoder passwordEncoder) {
        this.dogRepository = dogRepository;
        this.ownerRepository = ownerRepository;
        this.appUserRepository = appUserRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        createAuthorities();
        logUser();
        loadOwners();
        loadDogs();
    }

    private void createAuthorities() {
        Authority authority1 = new Authority();
        authority1.setName("ROLE_USER");

        Authority authority2 = new Authority();
        authority2.setName("ROLE_ADMIN");

        authorityRepository.save(authority1);
        authorityRepository.save(authority2);

        System.out.println("Authorities loaded: " + authorityRepository.count());
    }

    private void logUser() {
        AppUser appUser = new AppUser();
        appUser.setEmail("admin@admin.pl");
        appUser.setPassword(passwordEncoder.encode("admin"));
        appUser.setFirstName("Admin");
        appUser.setLastName("Admin");
        appUser.setUsername("admin");
        appUser.setActive(true);
        appUser.setAuthorities(authorityRepository.findByNameIn(List.of("ROLE_USER", "ROLE_ADMIN")));

        appUserRepository.save(appUser);

        System.out.println("Users loaded: " + appUserRepository.count());
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
        dog1.setCreatedByUser(appUserRepository.findById(1L).get());
        dog1.setCreatedDate(Instant.now());
        dog1.setStatus(DogStatus.ACCEPTED);
        dog1.setOwner(ownerRepository.findById(1L).get());

        //load 20 more dogs
        for (int i = 0; i < 20; i++) {
            Dog dog = new Dog();
            dog.setName("Dog" + i);
            dog.setContent("Labrador");
            dog.setCreatedByUser(appUserRepository.findById(1L).get());
            dog.setCreatedDate(Instant.now());
            dog.setStatus(DogStatus.ACCEPTED);
            dog.setOwner(ownerRepository.findById(1L).get());
            dogRepository.save(dog);
        }

        Dog dog2 = new Dog();
        dog2.setName("Azor");
        dog2.setContent("Owczarek Niemiecki");
        dog2.setCreatedByUser(appUserRepository.findById(1L).get());
        dog2.setCreatedDate(Instant.now());
        dog2.setOwner(ownerRepository.findById(2L).get());

        Dog dog3 = new Dog();
        dog3.setName("Reksio");
        dog3.setContent("Mieszaniec");
        dog3.setCreatedByUser(appUserRepository.findById(1L).get());
        dog3.setCreatedDate(Instant.now());
        dog3.setOwner(ownerRepository.findById(3L).get());

        Dog dog4 = new Dog();
        dog4.setName("Rex");
        dog4.setContent("Owczarek Niemiecki");
        dog4.setCreatedByUser(appUserRepository.findById(1L).get());
        dog4.setCreatedDate(Instant.now());
        dog4.setOwner(ownerRepository.findById(4L).get());

        dogRepository.save(dog1);
        dogRepository.save(dog2);
        dogRepository.save(dog3);
        dogRepository.save(dog4);

        System.out.println("Dogs loaded: " + dogRepository.count());
    }
}
