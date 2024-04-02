package edu.weeia.cynodesu.controllers;

import edu.weeia.cynodesu.api.v1.model.CreateDogDTO;
import edu.weeia.cynodesu.api.v1.model.GetDogDTO;
import edu.weeia.cynodesu.domain.Dog;
import edu.weeia.cynodesu.services.DogService;
import edu.weeia.cynodesu.services.OwnerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping(DogController.BASE_URL)
@Controller
public class DogController {
    public static final String BASE_URL = "/dog";
    private final DogService dogService;
    private final OwnerService ownerService;

    public DogController(DogService dogService, OwnerService ownerService) {
        this.dogService = dogService;
        this.ownerService = ownerService;
    }

    @GetMapping("/add")
    public String startAddArticle(Model model) {
        model.addAttribute("msg", "Add a new article");
        model.addAttribute("article", new Dog());
        return "dog/new-dog";
    }

    @PostMapping("/add")
    public String finishAddArticle(CreateDogDTO dogDto, RedirectAttributes redirectAttrs) {

        //TODO:validate and return to GET:/add on errors

        Dog dog = dogService.createDog(dogDto);

        redirectAttrs.addFlashAttribute("success", "Article with id " + dog.getId() + " is created");

        return "redirect:/";
    }

    @GetMapping({"/"})
    public String getAllDogs(Model model) {
//        return new ResponseEntity<>(
//                new DogListDTO(dogService.getAllDogs().stream().toList()), HttpStatus.OK);
        model.addAttribute("dogs", dogService.getAllDogs());
        model.addAttribute("owners", ownerService.getAllOwners());
        return "dog/dogs";
    }

    @ResponseBody
    @DeleteMapping(value = "/{id}", produces = MediaType.TEXT_HTML_VALUE)
    public String deleteDog(@PathVariable Long id) {
        GetDogDTO dog = dogService.deleteDogById(id);
        System.out.println("Deleted dog: " + dog);
        return "";
    }

    @PostMapping
    public String createDog(
            @RequestParam ("new-dog") String dogName,
            Model model) {
        dogService.createNewDog(dogName);
        model.addAttribute("dogs", dogService.getAllDogs());
        return "dog/dogs :: dogs-list";
    }

    @GetMapping("/{name}")
    public ResponseEntity<GetDogDTO> getDogByName(@PathVariable String name) {
        return new ResponseEntity<>(
                dogService.getDogByName(name), HttpStatus.OK);
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('USER')")
    public String editDog(@PathVariable Long id, Model model) {
        model.addAttribute("dog", dogService.getDogById(id));
        model.addAttribute("owners", ownerService.getAllOwners());
        return "dog/dogs/edit-dog";
    }

}
