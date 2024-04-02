package edu.weeia.cynodesu.controllers;

import edu.weeia.cynodesu.api.v1.model.CreateDogDTO;
import edu.weeia.cynodesu.api.v1.model.LoginDTO;
import edu.weeia.cynodesu.api.v1.model.UserSignUpDTO;
import edu.weeia.cynodesu.domain.AppUser;
import edu.weeia.cynodesu.domain.Dog;
import edu.weeia.cynodesu.security.AppUserDetails;
import edu.weeia.cynodesu.services.DogService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class LandingController {

    @NonNull
    private DogService dogService;

    @GetMapping({"/", ""})
    public String landing(Model model, Pageable pageable, @AuthenticationPrincipal AppUserDetails principal) {
        model.addAttribute("greeting", "こんにちは Cyno-desu!");
        model.addAttribute("tagline", "Cyno-desuは、あなたの犬の競技会のスコアを追跡するための最高の方法です。");
        model.addAttribute("success", "Your operation was successful."); // Add this line


        var page = dogService.previewForPublicHomePage(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdDate").descending()));
        model.addAttribute("principal", principal);
        model.addAttribute("dog", new CreateDogDTO());
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
