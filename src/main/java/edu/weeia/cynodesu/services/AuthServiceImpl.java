package edu.weeia.cynodesu.services;

import edu.weeia.cynodesu.api.v1.model.*;
import edu.weeia.cynodesu.domain.AppUser;
import edu.weeia.cynodesu.security.AppUserDetails;
import edu.weeia.cynodesu.security.AppUserDetailsService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final AuthorityService authorityService;
    private final AppUserDetailsService appUserDetailsService;

    public AuthServiceImpl(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, UserService userService, AuthorityService authorityService, AppUserDetailsService appUserDetailsService) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.authorityService = authorityService;
        this.appUserDetailsService = appUserDetailsService;
    }

    @Override
    public UserSingUpResponseDTO signUp(UserSignUpDTO signUpRequest) {

        //Generate Hashed Password
        String hashedPassword;
        hashedPassword = passwordEncoder.encode(signUpRequest.pwdPlainText());

        //TODO: Check validation
        /***
         *         Check unique email
         *         Check unique username
         *         Done in controller i guess with help of @Validated and @Valid and BindingResult and SignUpValidator
         */
        UserSignUpHashedDTO hashedUser = new UserSignUpHashedDTO(
                signUpRequest.email(),
                signUpRequest.firstName(),
                signUpRequest.lastName(),
                signUpRequest.username(),
                hashedPassword
        );

        //Save User
        Long id = userService.save(hashedUser).Id();

        //return Token
        return new UserSingUpResponseDTO(
                id,
                true);
    }

    @Override
    public AppUserDetails signIn(LoginDTO loginRequest) {

        Optional<AppUser> user = userService.findWithAuthoritiesByUsername(loginRequest.username());
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found with email: " + loginRequest.username());
        }

        // Check if the provided password matches the stored password
        if (!passwordEncoder.matches(loginRequest.password(), user.get().getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        // Create an Authentication object
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.get().getUsername(),
                user.get().getPassword(),
                user.get().getAuthorities()
        );

        // Set the Authentication object in the SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(authentication);

        AppUserDetails appUser = appUserDetailsService.getCustomUserDetails(user.get());
        System.out.println(appUser);
        // Return the token in the LoginResponseDTO
        return appUser;
    }

    public AppUserDetails getCustomUserDetails(AppUser user) {

        return new AppUserDetails(user.getId(), user.getUsername(), user.getEmail(), user.getPassword(), user.getFirstName(), user.getLastName(), user.getAuthorities(),
                user.isEnabled(), user.isAccountNonExpired(), user.isCredentialsNonExpired(), user.isAccountNonLocked());
    }
}
