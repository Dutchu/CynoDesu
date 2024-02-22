package edu.weeia.cynodesu.controllers;

import edu.weeia.cynodesu.api.v1.model.LoginDTO;
import edu.weeia.cynodesu.api.v1.model.LoginResponseDTO;
import edu.weeia.cynodesu.api.v1.model.UserSignUpDTO;
import edu.weeia.cynodesu.api.v1.model.UserSingUpResponseDTO;
import edu.weeia.cynodesu.services.AuthService;
import edu.weeia.cynodesu.services.UserSignUpValidator;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Validated
@RestController
public class AuthController {
    private final AuthService authService;
    private final UserSignUpValidator userSignupValidator;

    public AuthController(AuthService authService, UserSignUpValidator userSignupValidator) {
        this.authService = authService;
        this.userSignupValidator = userSignupValidator;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponseDTO> authenticateUser(@RequestBody @Valid LoginDTO loginRequest) {
        return ResponseEntity.ok().body(
                authService.signIn(loginRequest));
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<UserSingUpResponseDTO> registerUser(@RequestBody @Valid UserSignUpDTO signUpRequest, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        //do custom validation along with the BeanValidation
        userSignupValidator.validate(signUpRequest, bindingResult);

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new UserSingUpResponseDTO(null,"Validation failed", false));
        }

        return ResponseEntity.ok().body(
                authService.signUp(signUpRequest)
        );
    }
}