package edu.weeia.cynodesu.bootstrap;

import edu.weeia.cynodesu.api.v1.model.CompetitionCreateForm;
import edu.weeia.cynodesu.api.v1.model.CreateDogDTO;
import edu.weeia.cynodesu.api.v1.model.CreateFacilityDTO;
import edu.weeia.cynodesu.api.v1.model.GetBreedingFacilityPreviewDTO;
import edu.weeia.cynodesu.domain.*;
import edu.weeia.cynodesu.exceptions.DogImageUploadException;
import edu.weeia.cynodesu.exceptions.ResourceNotFoundException;
import edu.weeia.cynodesu.repositories.AppUserRepository;
import edu.weeia.cynodesu.repositories.AuthorityRepository;
import edu.weeia.cynodesu.security.AppUserDetails;
import edu.weeia.cynodesu.security.AppUserDetailsService;
import edu.weeia.cynodesu.services.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Optional;

@Component
public class Bootstrap implements CommandLineRunner {
    private final AuthorityRepository authorityRepository;
    private final DogService dogService;
    private final AppUserRepository appUserRepository;
    private final AuthorityService authorityService;
    private final PasswordEncoder passwordEncoder;
    private final BreedingFacilityService breedingFacilityService;
    private final AppUserDetailsService userDetailsService;
    private final UserService userService;
    private final FileServiceImage fileService;
    private final AppUserDetailsService appUserDetailsService;
    private final BreedService breedService;
    private final CompetitionService competitionService;

    public Bootstrap(AuthorityRepository authorityRepository, DogService dogService, AppUserRepository appUserRepository, AuthorityService authorityService, PasswordEncoder passwordEncoder, BreedingFacilityService breedingFacilityService, AppUserDetailsService userDetailsService, UserService userService, FileServiceImage fileService, AppUserDetailsService appUserDetailsService, BreedService breedService, CompetitionService competitionService) {
        this.authorityRepository = authorityRepository;
        this.dogService = dogService;
        this.appUserRepository = appUserRepository;
        //presumebly unwanted since we want to register users while bootstraping data
        this.authorityService = authorityService;
        //this should be in register functionality
        this.passwordEncoder = passwordEncoder;
        this.breedingFacilityService = breedingFacilityService;
        this.userDetailsService = userDetailsService;

        this.userService = userService;
        this.fileService = fileService;
        this.appUserDetailsService = appUserDetailsService;
        this.breedService = breedService;
        this.competitionService = competitionService;
    }

    @Override
    public void run(String... args) throws Exception {
        //TODO: authorities should be constants in Constants.class or other Spring.Properties file for easy modification.
        createAuthorities();
        breedService.saveBreedsToDatabase();
        //TODO: key users should also exists as parameters in app.properties or Constants.class with credentials as secrets.
        registerAdmin();
        registerUser();
//        createCompetition();
//        createDogs();
//        loadOwners();
//        loadDogs();
    }

    private void createCompetition() {
        var com = new CompetitionCreateForm();
        com.setName("Testing Competition");
        com.setLocation("ul. Starorudzka 156, Lodz 93-030");
        com.setDate(LocalDateTime.now().plusDays(2));
        competitionService.save(com);
    }

//    @Transactional
//    void createDogs() {
//
//        var facility = new CreateFacilityDTO(
//                "Test Facility",
//                "724748803",
//                "breeding@facility.com",
//                "Uć",
//                "123456789",
//                "123-123-123",
//                "Testing notest",
//                null
//        );
//        var facilityId = breedingFacilityService.create(facility, "user");
//        breedingFacilityService.create(facility, "admin");
//
//        var dog = new CreateDogDTO();
//        dog.setName("Testing");
//        dog.setContent("Dog");
//        dog.setImage(null);
//
//
//        dogService.createDog(facilityId, dog);
//    }

//    public void setUpMockUser(String username, String password) {
//
//        AppUser appUser = appUserRepository.findOneByUsername(username)
//                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
//
//
//        // Create a dummy user with the necessary roles and privileges
//        AppUserDetails userDetails = appUserDetailsService.loadUserByUsername(username);
//
//
//        // Create an authentication token using the user details
//        UsernamePasswordAuthenticationToken authenticationToken =
//                new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
//
//        // Set the authentication in the security context
//        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//    }

    private void registerUser() {
        AppUser appUser = new AppUser();
        appUser.setEmail("user@user.pl");
        appUser.setPassword(passwordEncoder.encode("user"));
        appUser.setFirstName("User");
        appUser.setLastName("Test");
        appUser.setUsername("user");
        appUser.setActive(true);
        appUser.setAuthorities(authorityService.findByNameIn("ROLE_USER"));
        appUserRepository.save(appUser);
        System.out.println("Users loaded: " + appUserRepository.count());
    }

    private void createAuthorities() {
        Authority authority1 = new Authority();
        authority1.setName("ROLE_USER");

        Authority authority2 = new Authority();
        authority2.setName("ROLE_ADMIN");

        authorityService.save(authority1);
        authorityService.save(authority2);

        System.out.println("Authorities loaded: " + authorityRepository.count());
    }

    //TODO: find more elegant way to bootstrap "golden data" within app.
    private void registerAdmin() {
        AppUser appUser = new AppUser();
        appUser.setEmail("admin@admin.pl");
        appUser.setPassword(passwordEncoder.encode("admin"));
        appUser.setFirstName("Admin");
        appUser.setLastName("Admin");
        appUser.setUsername("admin");
        appUser.setActive(true);
        appUser.setAuthorities(authorityService.findByNameIn("ROLE_USER", "ROLE_ADMIN"));
        appUser.setCreatedByUser(null);
        appUserRepository.save(appUser);
        System.out.println("Users loaded: " + appUserRepository.count());
    }

    private void loadOwners() {
        BreedingFacility breedingFacility1 = new BreedingFacility();
//        breedingFacility1.setFirstName("Jan");
//        breedingFacility1.setLastName("Kowalski");
//
//        BreedingFacility breedingFacility2 = new BreedingFacility();
//        breedingFacility2.setFirstName("Adam");
//        breedingFacility2.setLastName("Nowak");
//
//        BreedingFacility breedingFacility3 = new BreedingFacility();
//        breedingFacility3.setFirstName("Anna");
//        breedingFacility3.setLastName("Kowalska");
//
//        BreedingFacility breedingFacility4 = new BreedingFacility();
//        breedingFacility4.setFirstName("Maria");
//        breedingFacility4.setLastName("Nowak");

//        ownerRepository.save(breedingFacility1);
//        ownerRepository.save(breedingFacility2);
//        ownerRepository.save(breedingFacility3);
//        ownerRepository.save(breedingFacility4);

//        System.out.println("Owners loaded: " + ownerRepository.count());
    }

    private void loadDogs() {
//        Dog dog1 = new Dog();
//        dog1.setName("Burek");
//        dog1.setContent("Labrador");
//        dog1.setCreatedByUser(appUserRepository.findById(1L).get());
//        dog1.setCreatedDate(Instant.now());
//        dog1.setStatus(DogStatus.ACCEPTED);
//        dog1.setOwner(ownerRepository.findById(1L).get());

        Optional<AppUser> maybeAdmin = userService.findWithAuthoritiesByEmail("admin@admin.pl");
        AppUser admin = maybeAdmin.orElseThrow(() -> new ResourceNotFoundException("App User with id 1 not found!"));
        Authentication authentication = new UsernamePasswordAuthenticationToken(admin, "admin", admin.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        CreateDogDTO dtoDog = getCreateDogDTO();

//        dogService.createDog(dtoDog);

//        //load 20 more dogs
//        for (int i = 0; i < 20; i++) {
//            Dog dog = new Dog();
//            dog.setName("Dog" + i);
//            dog.setContent("Labrador");
//            dog.setCreatedByUser(appUserRepository.findById(1L).get());
//            dog.setCreatedDate(Instant.now());
//            dog.setStatus(DogStatus.ACCEPTED);
//            dog.setOwner(ownerRepository.findById(1L).get());
//            dogService.createDog(dog);
//        }
//
//        Dog dog2 = new Dog();
//        dog2.setName("Azor");
//        dog2.setContent("Owczarek Niemiecki");
//        dog2.setCreatedByUser(appUserRepository.findById(1L).get());
//        dog2.setCreatedDate(Instant.now());
//        dog2.setOwner(ownerRepository.findById(2L).get());
//
//        Dog dog3 = new Dog();
//        dog3.setName("Reksio");
//        dog3.setContent("Mieszaniec");
//        dog3.setCreatedByUser(appUserRepository.findById(1L).get());
//        dog3.setCreatedDate(Instant.now());
//        dog3.setOwner(ownerRepository.findById(3L).get());
//
//        Dog dog4 = new Dog();
//        dog4.setName("Rex");
//        dog4.setContent("Owczarek Niemiecki");
//        dog4.setCreatedByUser(appUserRepository.findById(1L).get());
//        dog4.setCreatedDate(Instant.now());
//        dog4.setOwner(ownerRepository.findById(4L).get());
//
//        dogRepository.save(dog1);
//        dogRepository.save(dog2);
//        dogRepository.save(dog3);
//        dogRepository.save(dog4);
//
//        System.out.println("Dogs loaded: " + dogRepository.count());
    }

    private CreateDogDTO getCreateDogDTO() {
//        CreateDogDTO dtoDog = null;
//        try {
//            byte[] defaultImageBytes = fileService.getDefaultImage();
//            MultipartFile image = new CustomMultipartFile(
//                    "image",                        // Name of the parameter in the form
//                    "default-dog.jpg",                  // Original file name
//                    "image/jpeg",                   // Content type
//                    defaultImageBytes               // File content as byte[]
//            );
//            dtoDog = new CreateDogDTO(
//                    1L,
//                    "Burek",
//                    "Elo",
//                    image
//                    );
//        } catch (IOException e) {
//            throw new DogImageUploadException("Unable to load default image due to IO exception in Bootstrap.");
//        }
        return null;
    }
}
