package edu.weeia.cynodesu.controllers;

import edu.weeia.cynodesu.api.v1.model.CompetitionCreateForm;
import edu.weeia.cynodesu.api.v1.model.CompetitionScoreForm;
import edu.weeia.cynodesu.api.v1.model.DogDetailDto;
import edu.weeia.cynodesu.api.v1.model.DogPreviewDTO;
import edu.weeia.cynodesu.domain.DogCompetitionScoreId;
import edu.weeia.cynodesu.security.AppUserDetails;
import edu.weeia.cynodesu.services.BootstrapService;
import edu.weeia.cynodesu.services.CompetitionService;
import edu.weeia.cynodesu.services.DogService;
import edu.weeia.cynodesu.services.UserService;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Set;

@Controller
public class AdminController {

    private final DogService dogService;
    private final BootstrapService bootstrapService;
    private final MessageSource messageSource;
    private final UserService userService;
    private final CompetitionService competitionService;

    public AdminController(DogService dogService, BootstrapService bootstrapService, MessageSource messageSource, UserService userService, CompetitionService competitionService) {
        this.dogService = dogService;
        this.bootstrapService = bootstrapService;
        this.messageSource = messageSource;
        this.userService = userService;
        this.competitionService = competitionService;
    }

    @GetMapping("/admin")
    public String adminHome(Model model, @AuthenticationPrincipal AppUserDetails principal) {
        return "admin/admin-area";
    }

    @GetMapping("/admin/bootstrapAdmin")
    public String bootstrapAdmin(Model model, @AuthenticationPrincipal AppUserDetails principal) {
        bootstrapService.createThingsForAdmin(principal.getUsername());
        return "admin/admin-area";
    }
    @GetMapping("/admin/bootstrapUser")
    public String bootstrapUser(Model model, @AuthenticationPrincipal AppUserDetails principal) {
        bootstrapService.createThingsForUser(principal.getUsername());
        return "admin/admin-area";
    }

    @GetMapping("/admin/review-dogs")
    public String reviewDogs(Model model, @AuthenticationPrincipal AppUserDetails principal, Pageable pageable) {
        var dogPage = dogService.getAllToReview(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdDate").descending()));
        model.addAttribute("dogs", dogPage);
        ControllerPaginationUtil.decorateModel(model, dogPage);
        return "admin/review-dogs";
    }

    @PostMapping("/admin/activate")
    public String activateDogs(Model model, @RequestParam List<Long> selectedDogs) {
        boolean result = dogService.activateDogs( selectedDogs);

        if (result) {
            model.addAttribute("status", "success");
            model.addAttribute("message", messageSource.getMessage("app.admin.activate.success", null, Locale.getDefault()));
        } else {
            model.addAttribute("status", "error");
            model.addAttribute("message", messageSource.getMessage("app.admin.activate.fail", null, Locale.getDefault()));
        }

        return "_fragments/_message-box :: message";
    }

    @GetMapping("/admin/review-dogs/{dogId}")
    public String adminHome(Model model,
                            @AuthenticationPrincipal AppUserDetails principal,
                            @PathVariable Long dogId) {

        var dogDetails = dogService.getDetails(dogId);
        model.addAttribute(dogDetails);
        return "admin/admin-area";
    }

    @PostMapping("/admin/review-dogs")
    public String activateDogs(Model model, @AuthenticationPrincipal AppUserDetails principal, Pageable pageable) {
        var dogPage = dogService.getAllToReview(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdDate").descending()));
        model.addAttribute("dogs", dogPage);
        ControllerPaginationUtil.decorateModel(model, dogPage);
        return "admin/review-dogs";
    }

    @GetMapping("/admin/review-users")
    public String reviewUsers(Model model, @AuthenticationPrincipal AppUserDetails principal, Pageable pageable) {
        var userPage =  userService.getAllToReview(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdDate").descending()));
        model.addAttribute("users", userPage);
        ControllerPaginationUtil.decorateModel(model, userPage);
        return "admin/review-users";
    }

    @PostMapping("/admin/activate-users")
    public String activateUsers(Model model, @RequestParam List<Long> selectedUsers) {
        boolean result = userService.activateUsers(selectedUsers);

        if (result) {
            model.addAttribute("status", "success");
            model.addAttribute("message", messageSource.getMessage("app.admin.activate.success", null, Locale.getDefault()));
        } else {
            model.addAttribute("status", "error");
            model.addAttribute("message", messageSource.getMessage("app.admin.activate.fail", null, Locale.getDefault()));
        }

        return "_fragments/_message-box :: message";
    }
    private String getWelcomeMessage(AppUserDetails principal) {
        return "Welcome " + principal.getUsername() + "!";
    }


    @GetMapping("/admin/competition")
    public String getCompetition(Model model, @AuthenticationPrincipal AppUserDetails principal, Pageable pageable) {
        var pageCompetitions =  competitionService.getAllCompetitions(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdDate").descending()));
        model.addAttribute("competitions", pageCompetitions);
        model.addAttribute("principal", principal);
        model.addAttribute("competitionCreateForm", new CompetitionCreateForm());
        ControllerPaginationUtil.decorateModel(model, pageCompetitions);

        return "competition/competition";
    }
    @PostMapping("/admin/competition")
    public String createCompetition(Model model, @AuthenticationPrincipal AppUserDetails principal, Pageable pageable, @ModelAttribute CompetitionCreateForm form) {
        var pageCompetitions =  competitionService.getAllCompetitions(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdDate").descending()));
        model.addAttribute("competitions", pageCompetitions);
        model.addAttribute("principal", principal);
        ControllerPaginationUtil.decorateModel(model, pageCompetitions);

        competitionService.save(form);

        return "_fragments/_competition :: list";
    }

    @GetMapping("/admin/competition/{id}")
    public String reviewCompetition(@PathVariable Long id, @AuthenticationPrincipal AppUserDetails principal, Model model, Pageable pageable) {

//        CompetitionScoreForm scoreForm = competitionService.getCompetitionFormInformation(id,);
//        model.addAttribute("scoreForm", scoreForm);
        model.addAttribute("competitionCreateForm", new CompetitionCreateForm());
        model.addAttribute("competitionId", id);
        var dogPage =   dogService.getAllByCompetition(id, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdDate").descending()));
        model.addAttribute("dogs", dogPage);
        model.addAttribute("principal", principal);
        ControllerPaginationUtil.decorateModel(model, dogPage);

        return "admin/competition-dog-list";
    }

    @PostMapping("/admin/competition/{id}")
    public String scoreFormForDog(@PathVariable Long id, @RequestParam Long dogId, @AuthenticationPrincipal AppUserDetails principal, Model model, Pageable pageable) {
        CompetitionScoreForm scoreForm = competitionService.getCompetitionScoreFormInformation(id, dogId);
        model.addAttribute("scoreForm", scoreForm);
        model.addAttribute("competitionId", id);
        model.addAttribute("dogId", dogId);

        return "admin/_score-dog :: form";
    }


    @PostMapping("/admin/competition/{competitionId}/dog/{dogId}")
    public String getScoreForm(@PathVariable Long competitionId, @PathVariable Long dogId,
                               @AuthenticationPrincipal AppUserDetails principal, Model model, Pageable pageable,
                               @ModelAttribute CompetitionScoreForm scoreForm) {

//        model.addAttribute("competitionCreateForm", new CompetitionCreateForm());
//        var pageCompetitions =  competitionService.getAllCompetitions(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdDate").descending()));
//        model.addAttribute("competitions", pageCompetitions);
//        model.addAttribute("principal", principal);
//        ControllerPaginationUtil.decorateModel(model, pageCompetitions);

        Set<DogCompetitionScoreId> scoreId = competitionService.scoreDog(competitionId, dogId, scoreForm);

        if (!scoreId.isEmpty()) {
            model.addAttribute("status", "success");
            model.addAttribute("message", messageSource.getMessage("app.admin.score.success", null, Locale.getDefault()));
        } else {
            model.addAttribute("status", "error");
            model.addAttribute("message", messageSource.getMessage("app.admin.score.fail", null, Locale.getDefault()));
        }

        return "_fragments/_message-box :: message";
    }
}