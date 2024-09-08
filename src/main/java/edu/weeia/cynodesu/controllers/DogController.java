package edu.weeia.cynodesu.controllers;

import edu.weeia.cynodesu.api.v1.model.CreateDogDTO;
import edu.weeia.cynodesu.api.v1.model.DogDetailDto;
import edu.weeia.cynodesu.api.v1.model.GetDogDTO;
import edu.weeia.cynodesu.services.BreedingFacilityService;
import edu.weeia.cynodesu.services.DogService;
import edu.weeia.cynodesu.services.UserService;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.web.IWebExchange;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;


@Controller
@RequestMapping
public class DogController {

    private final DogService dogService;
    private final MessageSource messageSource;
    private final SseController sseController;
    private final UserService userService;
    private final BreedingFacilityService breedingFacilityService;
    private final ThymeleafViewResolver thymeleafViewResolver;
    private final SpringTemplateEngine templateEngine;

    public DogController(DogService dogService, MessageSource messageSource, SseController sseController, UserService userService, BreedingFacilityService breedingFacilityService, ThymeleafViewResolver thymeleafViewResolver, SpringTemplateEngine templateEngine) {
        this.dogService = dogService;
        this.messageSource = messageSource;
        this.sseController = sseController;
        this.userService = userService;
        this.breedingFacilityService = breedingFacilityService;
        this.thymeleafViewResolver = thymeleafViewResolver;
        this.templateEngine = templateEngine;
    }

    @PostMapping("/facility/{facilityId}/create-dog")
    public String createDogByFacility(
            @PathVariable Long facilityId,
            @Valid @ModelAttribute("createDogDto") CreateDogDTO createDogDto,
            Pageable pageable,
            BindingResult bindingResult,
            Model model) {

        var saveResult = dogService.createDog(facilityId, createDogDto);

        if (Objects.nonNull(saveResult)) {
            sseController.sendNotification("New dog created: " + saveResult.name());
            model.addAttribute("status", "success");
            model.addAttribute("message", messageSource.getMessage("app.dog.creation.success", null, Locale.getDefault()));
        } else {
            model.addAttribute("status", "error");
            model.addAttribute("message", messageSource.getMessage("app.dog.creation.fail", null, Locale.getDefault()));
        }

        return "_fragments/_message-box :: message";
    }

    @GetMapping("/facility/{facilityId}")
    public String getDogsByFacility(@PathVariable Long facilityId, Model model, Pageable pageable) {
        var pageDogs = dogService.previewAllByFacility(facilityId, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdDate").descending()));
        model.addAttribute("createDogDto", new CreateDogDTO());
        model.addAttribute("dogs", pageDogs);
        model.addAttribute("breeds", dogService.getBreeds());
        ControllerPaginationUtil.decorateModel(model, pageDogs);
        model.addAttribute("facilityId", facilityId);
        // Return the fragment that displays the list of dogs
        return "_fragments/_dog :: list"; // Assumes you have a Thymeleaf fragment named 'list' for displaying dogs
    }


    @GetMapping(value = "/dog/{id}")
    public String getDog(@PathVariable Long id, Model model) {
        DogDetailDto dog = dogService.getDetails(id);
        model.addAttribute("dogDetail", dog);
        return "_fragments/_dog :: dog-detail";
    }

    @GetMapping({"/all-dogs"})
    public String getAllDogs(Model model) {
        model.addAttribute("dogs", dogService.getAllDogs());
        return "dog/dogs";
    }

    @ResponseBody
    @DeleteMapping(value = "/dog/{id}", produces = MediaType.TEXT_HTML_VALUE)
    public String deleteDog(@PathVariable Long id) {
        GetDogDTO dog = dogService.deleteDogById(id);
        System.out.println("Deleted dog: " + dog);
        return "";
    }

    @PostMapping
    public String createDog(
            @RequestParam("new-dog") String dogName,
            Model model) {
        dogService.createNewDog(dogName);
        model.addAttribute("dogs", dogService.getAllDogs());
        return "dog/dogs :: dogs-list";
    }


    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('USER')")
    public String editDog(@PathVariable Long id, Model model) {
        model.addAttribute("dog", dogService.getDogById(id));
//        model.addAttribute("owners", ownerService.getAllOwners());
        return "dog/dogs/edit-dog";
    }

}
