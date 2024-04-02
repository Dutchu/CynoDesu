package edu.weeia.cynodesu.controllers;

import edu.weeia.cynodesu.api.v1.model.LoginDTO;
import edu.weeia.cynodesu.api.v1.model.LoginResponseDTO;
import edu.weeia.cynodesu.api.v1.model.UserSignUpDTO;
import edu.weeia.cynodesu.api.v1.model.UserSingUpResponseDTO;
import edu.weeia.cynodesu.domain.AppUser;
import edu.weeia.cynodesu.security.AppUserDetails;
import edu.weeia.cynodesu.services.AuthService;
import edu.weeia.cynodesu.services.UserSignUpValidator;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Validated
public class AuthController {
    private final AuthService authService;
    private final UserSignUpValidator userSignupValidator;

    public AuthController(AuthService authService, UserSignUpValidator userSignupValidator) {
        this.authService = authService;
        this.userSignupValidator = userSignupValidator;
    }

    @PostMapping("/auth/success")
    public String authenticateUser(Model model, @AuthenticationPrincipal AppUserDetails principal) {


//        AppUserDetails principal = authService.signIn(
//                new LoginDTO(username, password)
//        );
        System.out.println("\n" + principal.getUsername() + "\n");
        model.addAttribute("principal", principal);
        return "_fragments/header :: headerbar";
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<UserSingUpResponseDTO> registerUser(@RequestBody @Valid UserSignUpDTO signUpRequest, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        //do custom validation along with the BeanValidation
        userSignupValidator.validate(signUpRequest, bindingResult);


        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new UserSingUpResponseDTO(null,false));
        }

        return ResponseEntity.ok().body(
                authService.signUp(signUpRequest)
        );
    }
}