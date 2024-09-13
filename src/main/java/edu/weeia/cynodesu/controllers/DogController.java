package edu.weeia.cynodesu.controllers;

import edu.weeia.cynodesu.api.v1.model.CreateDogDTO;
import edu.weeia.cynodesu.api.v1.model.DogDetailDto;
import edu.weeia.cynodesu.api.v1.model.GetDogDTO;
import edu.weeia.cynodesu.services.DogService;
import edu.weeia.cynodesu.services.FileService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.Objects;
import java.util.UUID;


@Controller
@RequestMapping
public class DogController {

    private final DogService dogService;
    private final MessageSource messageSource;
    private final FileService fileService;

    public DogController(DogService dogService, MessageSource messageSource, FileService fileService, FileService fileService1) {
        this.dogService = dogService;
        this.messageSource = messageSource;
        this.fileService = fileService1;
    }

    @GetMapping("/dog/pdf/{fileId}")
    public ResponseEntity<byte[]> servePdf(@PathVariable UUID fileId) {
        byte[] pdfContent = fileService.getPdfFileById(fileId); // Implement this method to fetch PDF bytes from your service or database
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"file.pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfContent);
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
}
