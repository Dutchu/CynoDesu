package edu.weeia.cynodesu.services;

import edu.weeia.cynodesu.api.v1.model.UserCreatedDTO;
import edu.weeia.cynodesu.api.v1.model.UserSignUpHashedDTO;
import edu.weeia.cynodesu.api.v1.model.UserWithAuthoritiesDTO;
import edu.weeia.cynodesu.domain.AppUser;
import edu.weeia.cynodesu.security.AppUserDetails;

import java.util.Optional;

public interface UserService {
    Optional<AppUser> findWithAuthoritiesByEmail(String email);

    UserCreatedDTO save(UserSignUpHashedDTO user);

    Optional<AppUser> findWithAuthoritiesByUsername(String username);
}
