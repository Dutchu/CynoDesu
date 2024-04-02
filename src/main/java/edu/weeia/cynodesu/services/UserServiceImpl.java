package edu.weeia.cynodesu.services;

import edu.weeia.cynodesu.api.v1.model.UserCreatedDTO;
import edu.weeia.cynodesu.api.v1.model.UserSignUpDTO;
import edu.weeia.cynodesu.api.v1.model.UserSignUpHashedDTO;
import edu.weeia.cynodesu.domain.AppUser;
import edu.weeia.cynodesu.repositories.AppUserRepository;
import edu.weeia.cynodesu.security.AppUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final AuthorityService authorityService;
    private final AppUserRepository userRepository;

    public Optional<AppUser> findWithAuthoritiesByEmail(String email) {
        return userRepository.findOneWithAuthoritiesByEmail(email);
    }

    @Override
    public Optional<AppUser> findWithAuthoritiesByUsername(String username) {
        return userRepository.findOneWithAuthoritiesByUsername(username);
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

        AppUser savedUser = userRepository.save(user);

        return new UserCreatedDTO(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getAuthorities()
        );
    }

    public void saveAsAdmin(UserSignUpDTO user) {
        // Create a new Admin
    }
}
