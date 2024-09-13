package edu.weeia.cynodesu.controllers;

import edu.weeia.cynodesu.api.v1.model.CompetitionCreateForm;
import edu.weeia.cynodesu.api.v1.model.DogPreviewDTO;
import edu.weeia.cynodesu.api.v1.model.RegisterDogCompetitionForm;
import edu.weeia.cynodesu.services.CompetitionService;
import edu.weeia.cynodesu.services.DogService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(CompetitionController.BASE_URL)
public class CompetitionController {
    public static final String BASE_URL = "/competition";

    private final CompetitionService competitionService;
    private final DogService dogService;

    public CompetitionController(CompetitionService competitionService, DogService dogService) {
        this.competitionService = competitionService;
        this.dogService = dogService;
    }

    @GetMapping("")
    public String getUsersCompetitions(@AuthenticationPrincipal UserDetails principal, Pageable pageable, Model model) {
        var pageCompetitions =  competitionService.getAllCompetitions(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdDate").descending()));
        model.addAttribute("competitions", pageCompetitions);
        model.addAttribute("principal", principal);
        model.addAttribute("competitionCreateForm", new CompetitionCreateForm());
        ControllerPaginationUtil.decorateModel(model, pageCompetitions);

        return "competition/competition";
    }


    @GetMapping("/{competitionId}")
    public String registerDogForm(@AuthenticationPrincipal UserDetails principal, @PathVariable Long competitionId, Model model) {

        List<DogPreviewDTO> userDogs = dogService.findAllByUser(principal.getUsername()); // Fetch dogs for the current user
        model.addAttribute("userDogs", userDogs);
        model.addAttribute("principal", principal);
        model.addAttribute("registerForm", new RegisterDogCompetitionForm());
        model.addAttribute("competitionId", competitionId);
        return "competition/register";
    }
    @PostMapping("/{competitionId}/dog/")
    public String registerDogForCompetition(@AuthenticationPrincipal UserDetails principal, @ModelAttribute RegisterDogCompetitionForm form, @PathVariable Long competitionId, Model model) {
        model.addAttribute("principal", principal);
        model.addAttribute("registerForm", new RegisterDogCompetitionForm());

        Long id = competitionService.registerDogForCompetition(competitionId,  form.getDogId());

        return "competition/register";
    }
}
