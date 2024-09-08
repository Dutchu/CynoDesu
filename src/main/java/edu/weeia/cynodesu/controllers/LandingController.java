package edu.weeia.cynodesu.controllers;

import edu.weeia.cynodesu.api.v1.model.UserSignUpForm;
import edu.weeia.cynodesu.security.AppUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

@Controller
@RequiredArgsConstructor
public class LandingController {


    @GetMapping({"/", ""})
    public String landing(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "showRegistrationSuccess", required = false) boolean showRegistrationSuccess,
            @RequestParam(value = "showModal", required = false) boolean showModal,
            Model model,
            Pageable pageable,
            @AuthenticationPrincipal AppUserDetails principal) {

        model.addAttribute("userSignUpForm", new UserSignUpForm());
        /***
         * Idea to add notes with simple updates to the landing page
         */
//        model.addAttribute("notes", noteService.readAll(PageRequest.of(0, 20, Sort.by("createdDate").descending()))));
//        model.addAttribute("note", new Note());
        return "landing";
    }

    private String getWelcomeMessage(AppUserDetails principal) {
        return "Welcome " + principal.getUsername() + "!";
    }
}
