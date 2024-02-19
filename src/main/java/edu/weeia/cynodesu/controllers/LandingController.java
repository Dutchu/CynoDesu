package edu.weeia.cynodesu.controllers;

import edu.weeia.cynodesu.security.AppUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.data.domain.Pageable;

@Controller
@RequiredArgsConstructor
public class LandingController {
    @GetMapping({"/", ""})
    public String landing(Model model, Pageable pageable) {
        model.addAttribute("greeting", "こんにちは Cyno-desu!");
        model.addAttribute("tagline", "Cyno-desuは、あなたの犬の競技会のスコアを追跡するための最高の方法です。");

        /***
         * Idea to add notes with simple updates to the landing page
         */
//        model.addAttribute("notes", noteService.readAll(PageRequest.of(0, 20, Sort.by("createdDate").descending()))));
//        model.addAttribute("note", new Note());
        return "landing";
    }

    @GetMapping("/admin")
    public String adminHome(Model model, @AuthenticationPrincipal AppUserDetails principal) {
        model.addAttribute("message", getWelcomeMessage(principal));

        return "dogs";
    }

    @GetMapping("/note")
    public String userHome(Model model, @AuthenticationPrincipal AppUserDetails principal) {
        model.addAttribute("message", getWelcomeMessage(principal));
//        model.addAttribute("notes", noteService.readAllByUser(PageRequest.of(0, 20, Sort.by("createdDate").descending()), principal.getId()));
//        model.addAttribute("note", new Note());
        return "landing";
    }

    private String getWelcomeMessage(AppUserDetails principal) {
        return "Welcome " + principal.getUsername() + "!";
    }
}
