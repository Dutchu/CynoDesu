package edu.weeia.cynodesu.services;
import edu.weeia.cynodesu.security.AppUserDetails;
import edu.weeia.cynodesu.domain.AppUser;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AppUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserService userService;

    @Override
    public AppUserDetails loadUserByUsername(String email) {
        Optional<AppUser> userFromDatabase = userService.findWithAuthoritiesByEmail(email);

        return userFromDatabase
                .map(this::getCustomUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(" User with login:" + email + " was not found in the " + " database "));
    }

    @Transactional(readOnly = true)
    public AppUserDetails getCustomUserDetails(AppUser user) {

        return new AppUserDetails(user.getId(), user.getUsername(), user.getEmail(), user.getPassword(), user.getFirstName(), user.getLastName(), user.getAuthorities(),
                user.isEnabled(), user.isAccountNonExpired(), user.isCredentialsNonExpired(), user.isAccountNonLocked());
    }

}