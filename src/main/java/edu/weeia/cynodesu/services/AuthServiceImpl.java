package edu.weeia.cynodesu.services;

import edu.weeia.cynodesu.api.v1.model.*;
import edu.weeia.cynodesu.controllers.exceptions.AuthenticationFailedException;
import edu.weeia.cynodesu.domain.AppUser;
import edu.weeia.cynodesu.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final AuthorityService authorityService;

    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider, PasswordEncoder passwordEncoder, UserService userService, AuthorityService authorityService) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.authorityService = authorityService;
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

        // TODO: Check if authentication should be done before saving user and how authentication works with Spring and JWT
        String token = signToken(hashedUser.username(), hashedUser.hashedPassword());

        //return Token
        return new UserSingUpResponseDTO(
                id,
                token,
                true);
    }

    @Override
    public LoginResponseDTO signIn(LoginDTO loginRequest) {

        //Check if User exists

        //if not exists throw exception

//        return this.signToken(user.id, user.email);
        return null;
    }

    private String signToken(String username, String hashedPassword) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        hashedPassword
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT token
        return tokenProvider.generateToken(authentication);
    }
}
