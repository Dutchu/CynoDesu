package edu.weeia.cynodesu.controllers;

import edu.weeia.cynodesu.api.v1.model.CreateFacilityDTO;
import edu.weeia.cynodesu.api.v1.model.GetBreedingFacilityPreviewDTO;
import edu.weeia.cynodesu.domain.BreedingFacility;
import edu.weeia.cynodesu.repositories.BreedingFacilityPreview;
import edu.weeia.cynodesu.services.BreedingFacilityService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller

@RequestMapping(BreedingController.BASE_URL)
public class BreedingController {

    public static final String BASE_URL = "/facility";
    private final BreedingFacilityService breedingFacilityService;

    public BreedingController(BreedingFacilityService breedingFacilityService) {
        this.breedingFacilityService = breedingFacilityService;
    }

    @GetMapping("/list")
    public String getFacilityList(Model model, @AuthenticationPrincipal UserDetails principal, Pageable pageable) {
        var page = breedingFacilityService.getByUserUsername(principal.getUsername(), PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdDate").descending()));
        model.addAttribute("facilities", page);
        ControllerPaginationUtil.decorateModel(model, page);
        return "_fragments/_facility :: list";
    }

    @PostMapping("/create")
    public String createNewFacility(
            @Valid @ModelAttribute("createFacilityDTO") CreateFacilityDTO createFacilityDTO,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserDetails principal,
            Model model,
            RedirectAttributes redirectAttributes,
            Pageable pageable
    ) {
        Page<GetBreedingFacilityPreviewDTO> facilities = breedingFacilityService.getByUserUsername(principal.getUsername(), pageable);
        model.addAttribute("facilities", facilities.getContent());
        model.addAttribute("page", facilities);

        //Update model with errors in form when Bean Validation fails
        if (bindingResult.hasErrors()) {
            model.addAttribute("createFacilityDTO", createFacilityDTO);
            return "_fragments/_facility :: create-facility-form";
        }

        //TODO: Manage currently picked facility by user. This here is wrong because facility wasn't picked yet.
        Long bfId = breedingFacilityService.create(createFacilityDTO, principal.getUsername());
        redirectAttributes.addFlashAttribute("breedingFacilityId", bfId);

        //Update model with facilities after creating one
        var pageFacilities = breedingFacilityService.getByUserUsername(principal.getUsername(), PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdDate").descending()));
        model.addAttribute("facilities", pageFacilities);
        ControllerPaginationUtil.decorateModel(model, pageFacilities);

        // Return the fragment to update
        return "_fragments/_facility :: list";
    }
}
