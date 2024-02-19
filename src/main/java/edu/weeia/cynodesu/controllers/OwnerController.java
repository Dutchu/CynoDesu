package edu.weeia.cynodesu.controllers;

import edu.weeia.cynodesu.api.v1.model.OwnerDTO;
import edu.weeia.cynodesu.api.v1.model.OwnerListDTO;
import edu.weeia.cynodesu.services.OwnerService;
import edu.weeia.cynodesu.services.OwnerServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(OwnerController.BASE_URL)
public class OwnerController {
    public static final String BASE_URL = "/api/v1/owners";
    private final OwnerService ownerService;

    public OwnerController(OwnerServiceImpl ownerService) {
        this.ownerService = ownerService;
    }

    @GetMapping
    ResponseEntity<OwnerListDTO> getOwners() {
        return new ResponseEntity<>(
                new OwnerListDTO(ownerService.getAllOwners()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    ResponseEntity<OwnerDTO> getOwnerById(@PathVariable Long id) {
        return new ResponseEntity<>(ownerService.getOwnerById(id), HttpStatus.OK);
    }

    @PostMapping
     ResponseEntity<OwnerDTO> createOwner(@RequestBody OwnerDTO ownerDTO) {
        return new ResponseEntity<>(ownerService.createNewOwner(ownerDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    ResponseEntity<OwnerDTO> updateOwner(@PathVariable Long id, @RequestBody OwnerDTO ownerDTO) {
        return new ResponseEntity<>(ownerService.updateOwner(id, ownerDTO), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    ResponseEntity<OwnerDTO> patchOwner(@PathVariable Long id, @RequestBody OwnerDTO ownerDTO) {
        return new ResponseEntity<>(ownerService.patchOwner(id, ownerDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<OwnerDTO> deleteOwner(@PathVariable Long id) {
        return new ResponseEntity<>(ownerService.deleteOwnerById(id), HttpStatus.OK);
    }
}