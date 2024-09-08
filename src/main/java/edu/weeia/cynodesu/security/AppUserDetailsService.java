package edu.weeia.cynodesu.security;

import edu.weeia.cynodesu.domain.AppUser;
import edu.weeia.cynodesu.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public AppUserDetails loadUserByUsername(String username) {
        return userService.findWithAuthoritiesByUsername(username)
                .map(AppUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException(" User with login:" + username + " was not found in the " + " database "));
//
//        App result = userFromDatabase
//                .map(() -> getCustomUserDetails())
//                .orElseThrow(() -> new UsernameNotFoundException(" User with login:" + username + " was not found in the " + " database "));
//        System.out.println(result);
//        return new AppUserDetails(result);
    }

//    @Transactional(readOnly = true)
//    public AppUserDetails getCustomUserDetails(AppUser user) {
//
//        return new AppUserDetails(user.getId(), user.getUsername(), user.getEmail(), user.getPassword(), user.getFirstName(), user.getLastName(), user.getAuthorities(),
//                user.isEnabled(), user.isAccountNonExpired(), user.isCredentialsNonExpired(), user.isAccountNonLocked());
//    }

}