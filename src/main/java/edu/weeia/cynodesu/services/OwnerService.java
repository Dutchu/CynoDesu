package edu.weeia.cynodesu.services;

import edu.weeia.cynodesu.api.v1.model.OwnerDTO;

import java.util.List;

public interface OwnerService {
    List<OwnerDTO> getAllOwners();
    OwnerDTO getOwnerById(Long id);
    OwnerDTO createNewOwner(OwnerDTO ownerDTO);
    OwnerDTO updateOwner(Long id, OwnerDTO ownerDTO);
    OwnerDTO patchOwner(Long id, OwnerDTO ownerDTO);
    OwnerDTO deleteOwnerById(Long id);

}
