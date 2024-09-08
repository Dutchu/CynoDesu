package edu.weeia.cynodesu.services;

import edu.weeia.cynodesu.api.v1.model.UserPreviewDto;
import edu.weeia.cynodesu.api.v1.model.UserCreatedDTO;
import edu.weeia.cynodesu.api.v1.model.UserSignUpHashedDTO;
import edu.weeia.cynodesu.domain.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<AppUser> findWithAuthoritiesByEmail(String email);
    UserCreatedDTO save(UserSignUpHashedDTO user);
    Long getFacilityId(String username);
    Optional<AppUser> findWithAuthoritiesByUsername(String username);
    Page<UserPreviewDto> getAllToReview(Pageable pageable);
    boolean activateUsers(List<Long> selectedUsers);
}
