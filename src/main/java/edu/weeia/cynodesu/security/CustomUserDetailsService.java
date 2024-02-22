package edu.weeia.cynodesu.security;

import edu.weeia.cynodesu.domain.AppUser;
import edu.weeia.cynodesu.repositories.AppUserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public class CustomUserDetailsService implements UserDetailsService {

    private final AppUserRepository userRepository;

    public CustomUserDetailsService(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public AppUserDetails loadUserByUsername(String email) {
        Optional<AppUser> userFromDatabase = userRepository.findOneWithAuthoritiesByEmail(email);

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
