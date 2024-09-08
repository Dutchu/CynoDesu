package edu.weeia.cynodesu.services;

import edu.weeia.cynodesu.api.v1.mapper.UserMapper;
import edu.weeia.cynodesu.api.v1.model.UserPreviewDto;
import edu.weeia.cynodesu.api.v1.model.UserCreatedDTO;
import edu.weeia.cynodesu.api.v1.model.UserSignUpDto;
import edu.weeia.cynodesu.api.v1.model.UserSignUpHashedDTO;
import edu.weeia.cynodesu.domain.AppUser;
import edu.weeia.cynodesu.domain.BreedingFacility;
import edu.weeia.cynodesu.exceptions.ResourceNotFoundException;
import edu.weeia.cynodesu.repositories.AppUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final AuthorityService authorityService;
    private final AppUserRepository userRepository;
    private final FileService fileService;

    @Override
    public boolean activateUsers(List<Long> selectedUsers) {
        userRepository.findAllById(selectedUsers).stream().map(this::setUserStatusActive).forEach(userRepository::save);
        return true;
    }
    private AppUser setUserStatusActive(AppUser user) {
        user.setActive(true);
        return user;
    }


    public Optional<AppUser> findWithAuthoritiesByEmail(String email) {
        return userRepository.findOneWithAuthoritiesByEmail(email);
    }

    @Override
    public Optional<AppUser> findWithAuthoritiesByUsername(String username) {
        return userRepository.findOneWithAuthoritiesByUsername(username);
    }

    @Override
    public Page<UserPreviewDto> getAllToReview(Pageable pageable) {
        return userRepository.findAllByActiveIsFalse(pageable)
                .map(UserMapper.INSTANCE::mapForPreview);
    }

    public UserCreatedDTO save(UserSignUpHashedDTO toCreate) {

        // Create a new user
        AppUser user = new AppUser();
        user.setEmail(toCreate.email());
        user.setPassword(toCreate.hashedPassword());
        user.setFirstName(toCreate.firstName());
        user.setLastName(toCreate.lastName());
        user.setUsername(toCreate.username());

        // Set user role
        // user.setRole("USER");
        //TODO: Admin logic
        user.setAuthorities(authorityService.findByNameIn("ROLE_USER"));

        try {
            user.setAvatar(fileService.getDefaultImage(AppUser.class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        AppUser savedUser = userRepository.save(user);

        return new UserCreatedDTO(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getAuthorities()
        );
    }

    @Override
    public Long getFacilityId(String username) {
        AppUser appUser = userRepository.findOneByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User " + username + "was not found!"));
        Set<BreedingFacility> breedingFacilities = appUser.getBreedingFacilities();
        return null;
    }

    public void saveAsAdmin(UserSignUpDto user) {
        // Create a new Admin
    }
}
