package edu.weeia.cynodesu.controllers.v1;

import edu.weeia.cynodesu.api.v1.model.GetDogDTO;
import edu.weeia.cynodesu.services.DogService;
import edu.weeia.cynodesu.services.OwnerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping(DogController.BASE_URL)
@Controller
public class DogController {
    public static final String BASE_URL = "/api/v1/dogs";
    private final DogService dogService;
    private final OwnerService ownerService;

    public DogController(DogService dogService, OwnerService ownerService) {
        this.dogService = dogService;
        this.ownerService = ownerService;
    }

    @RequestMapping({"", "/"})
    public String getAllDogs(Model model) {
//        return new ResponseEntity<>(
//                new DogListDTO(dogService.getAllDogs().stream().toList()), HttpStatus.OK);
        model.addAttribute("dogs", dogService.getAllDogs());
        model.addAttribute("owners", ownerService.getAllOwners());
        return "dogs";
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
        return "dogs :: dogs-list";
    }

    @GetMapping("/{name}")
    public ResponseEntity<GetDogDTO> getDogByName(@PathVariable String name) {
        return new ResponseEntity<>(
                dogService.getDogByName(name), HttpStatus.OK);
    }
}
