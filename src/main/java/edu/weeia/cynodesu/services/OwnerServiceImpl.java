package edu.weeia.cynodesu.services;

import edu.weeia.cynodesu.api.v1.mapper.OwnerMapper;
import edu.weeia.cynodesu.api.v1.model.OwnerDTO;
import edu.weeia.cynodesu.domain.Owner;
import edu.weeia.cynodesu.exceptions.ResourceNotFoundException;
import edu.weeia.cynodesu.repositories.OwnerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OwnerServiceImpl implements OwnerService {

    private final OwnerRepository ownerRepository;
    private final OwnerMapper ownerMapper;

    public OwnerServiceImpl(OwnerRepository ownerRepository, OwnerMapper ownerMapper) {
        this.ownerRepository = ownerRepository;
        this.ownerMapper = ownerMapper;
    }

    @Override
    public List<OwnerDTO> getAllOwners() {
        return ownerRepository.findAll().stream()
                .map(owner -> {
                    OwnerDTO ownerDTO = ownerMapper.ownerToOwnerDTO(owner);
                    ownerDTO.setOwnerURL("/api/v1/owners/" + owner.getId());
                    return ownerDTO;
                })
                .toList();
    }

    @Override
    public OwnerDTO getOwnerById(Long id) {
        return ownerRepository.findById(id)
                .map(ownerMapper::ownerToOwnerDTO)
                .map(ownerDTO -> {
                    ownerDTO.setOwnerURL("/api/v1/owners/" + id);
                    return ownerDTO;
                })
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public OwnerDTO createNewOwner(OwnerDTO ownerDTO) {
        Owner savedOwner = ownerRepository.save(ownerMapper.ownerDTOToOwner(ownerDTO));
        OwnerDTO savedOwnerDTO = ownerMapper.ownerToOwnerDTO(savedOwner);
        savedOwnerDTO.setOwnerURL("/api/v1/owners/" + savedOwner.getId());
        return savedOwnerDTO;
    }

    @Override
    public OwnerDTO updateOwner(Long id, OwnerDTO ownerDTO) {
        Owner owner = ownerMapper.ownerDTOToOwner(ownerDTO);
        owner.setId(id);
        Owner savedOwner = ownerRepository.save(owner);
        OwnerDTO savedOwnerDTO = ownerMapper.ownerToOwnerDTO(savedOwner);
        savedOwnerDTO.setOwnerURL("/api/v1/owners/" + savedOwner.getId());
        return savedOwnerDTO;
    }

    @Override
    public OwnerDTO patchOwner(Long id, OwnerDTO ownerDTO) {
        return ownerRepository.findById(id)
                .map(owner -> {
                    if (ownerDTO.getFirstName() != null) {
                        owner.setFirstName(ownerDTO.getFirstName());
                    }
                    if (ownerDTO.getLastName() != null) {
                        owner.setLastName(ownerDTO.getLastName());
                    }
                    OwnerDTO savedOwnerDTO = ownerMapper.ownerToOwnerDTO(ownerRepository.save(owner));
                    savedOwnerDTO.setOwnerURL("/api/v1/owners/" + id);
                    return savedOwnerDTO;
                })
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public OwnerDTO deleteOwnerById(Long id) {
        OwnerDTO ownerDTO = getOwnerById(id);
        ownerRepository.deleteById(id);
        return ownerDTO;
    }

}

