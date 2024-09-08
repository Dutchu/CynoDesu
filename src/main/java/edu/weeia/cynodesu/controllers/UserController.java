package edu.weeia.cynodesu.controllers;

import edu.weeia.cynodesu.api.v1.model.CreateDogDTO;
import edu.weeia.cynodesu.api.v1.model.CreateFacilityDTO;
import edu.weeia.cynodesu.domain.BreedingFacility;
import edu.weeia.cynodesu.security.AppUserDetails;
import edu.weeia.cynodesu.services.BreedingFacilityService;
import edu.weeia.cynodesu.services.DogService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class UserController {

    private final DogService dogService;
    private final BreedingFacilityService breedingFacilityService;

    UserController(DogService dogService, BreedingFacilityService breedingFacilityService) {
        this.dogService = dogService;

        this.breedingFacilityService = breedingFacilityService;
    }

    @GetMapping("/home")
    public String userHome(Model model, @AuthenticationPrincipal AppUserDetails principal, Pageable pageable, @ModelAttribute CreateFacilityDTO createFacilityDTO) {

        model.addAttribute("greeting", "こんにちは Cyno-desu!");
        model.addAttribute("tagline", "Cyno-desuは、あなたの犬の競技会のスコアを追跡するための最高の方法です。");
        model.addAttribute("success", "Your operation was successful."); // Add this line

        model.addAttribute("principal", principal);
        model.addAttribute("message", getWelcomeMessage(principal));
        model.addAttribute("dogs", new CreateDogDTO());

        //Ready facilities
        var pageFacilities = breedingFacilityService.getByUserUsername(principal.getUsername(), PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdDate").descending()));
        model.addAttribute("facilities", pageFacilities);

        ControllerPaginationUtil.decorateModel(model, pageFacilities);

        return "user/home";
    }


    private String getWelcomeMessage(AppUserDetails principal) {
        return "Welcome " + principal.getUsername() + "!";
    }
}
