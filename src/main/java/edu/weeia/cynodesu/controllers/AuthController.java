package edu.weeia.cynodesu.controllers;

import edu.weeia.cynodesu.api.v1.model.UserSignUpDto;
import edu.weeia.cynodesu.api.v1.model.UserSignUpForm;
import edu.weeia.cynodesu.security.AppUserDetails;
import edu.weeia.cynodesu.services.AuthService;
import edu.weeia.cynodesu.services.UserSignUpValidator;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;

@Controller
public class AuthController {
    private final AuthService authService;
    private final UserSignUpValidator userSignupValidator;
    private final MessageSource messageSource;
    private final SseController sseController;

    public AuthController(AuthService authService, UserSignUpValidator userSignupValidator, MessageSource messageSource, SseController sseController) {
        this.authService = authService;
        this.userSignupValidator = userSignupValidator;
        this.messageSource = messageSource;
        this.sseController = sseController;
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

    @InitBinder("signUpRequest")
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(userSignupValidator);
    }
    @PostMapping("/auth/signup")
    public String registerUser(
            @ModelAttribute @Valid UserSignUpForm signUpRequest,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {

        userSignupValidator.validate(signUpRequest, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("userSignUpForm", signUpRequest);
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "_fragments/_register :: form";
        }

        var validatedData = UserSignUpDto.validatedFromForm(signUpRequest);
        var result = authService.signUp(validatedData);

        if (result.success()) {

            model.addAttribute("status", "success");
            model.addAttribute("message", messageSource.getMessage("app.auth.registration.success", null, Locale.getDefault()));
        } else {
            model.addAttribute("status", "error");
            model.addAttribute("message", messageSource.getMessage("app.auth.registration.fail", null, Locale.getDefault()));
        }
        return "_fragments/_message-box :: message";
    }

    @GetMapping("/signup")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserSignUpForm());
        return "auth/signup";
    }
}