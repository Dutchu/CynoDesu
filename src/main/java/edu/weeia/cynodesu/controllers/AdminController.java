package edu.weeia.cynodesu.controllers;

import edu.weeia.cynodesu.api.v1.model.DogDetailDto;
import edu.weeia.cynodesu.security.AppUserDetails;
import edu.weeia.cynodesu.services.BootstrapService;
import edu.weeia.cynodesu.services.DogService;
import edu.weeia.cynodesu.services.UserService;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Locale;

@Controller
public class AdminController {

    private final DogService dogService;
    private final BootstrapService bootstrapService;
    private final MessageSource messageSource;
    private final UserService userService;

    public AdminController(DogService dogService, BootstrapService bootstrapService, MessageSource messageSource, UserService userService) {
        this.dogService = dogService;
        this.bootstrapService = bootstrapService;
        this.messageSource = messageSource;
        this.userService = userService;
    }

    @GetMapping("/admin")
    public String adminHome(Model model, @AuthenticationPrincipal AppUserDetails principal) {
        return "admin/admin-area";
    }

    @GetMapping("/admin/bootstrap")
    public String bootstrap(Model model, @AuthenticationPrincipal AppUserDetails principal) {
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
}