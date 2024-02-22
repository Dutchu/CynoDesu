package edu.weeia.cynodesu.controllers;

import edu.weeia.cynodesu.domain.Dog;
import edu.weeia.cynodesu.security.AppUserDetails;
import edu.weeia.cynodesu.services.DogService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.data.domain.Pageable;

@Controller
@RequiredArgsConstructor
public class LandingController {

    @NonNull
    private DogService dogService;

    @GetMapping({"/", ""})
    public String landing(Model model, Pageable pageable) {
        model.addAttribute("greeting", "こんにちは Cyno-desu!");
        model.addAttribute("tagline", "Cyno-desuは、あなたの犬の競技会のスコアを追跡するための最高の方法です。");
        model.addAttribute("success", "Your operation was successful."); // Add this line
        var page = dogService.previewForPublicHomePage(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdDate").descending()));

        model.addAttribute("dog", new Dog());
        model.addAttribute("dogs", page);
        DogControllerUtil.decorateModel(model, page);

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
        model.addAttribute("dogsToReview", dogService.getAllToReview(PageRequest.of(0, 20, Sort.by("createdDate").descending())));
        return "admin/admin-area";
    }

    @GetMapping("/user")
    public String userHome(Model model, @AuthenticationPrincipal AppUserDetails principal) {
        model.addAttribute("message", getWelcomeMessage(principal));
        model.addAttribute("dogs", dogService.previewAllByUser(PageRequest.of(0, 20, Sort.by("createdDate").descending()), principal.getId()));
//        model.addAttribute("article", new Dog());
        return "dog/dogs";
    }


    private String getWelcomeMessage(AppUserDetails principal) {
        return "Welcome " + principal.getUsername() + "!";
    }
}
