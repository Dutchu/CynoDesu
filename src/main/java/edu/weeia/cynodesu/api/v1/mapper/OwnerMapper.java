package edu.weeia.cynodesu.api.v1.mapper;

import edu.weeia.cynodesu.api.v1.model.OwnerDTO;
import edu.weeia.cynodesu.domain.Owner;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OwnerMapper {
    OwnerMapper INSTANCE = Mappers.getMapper(OwnerMapper.class);

    OwnerDTO ownerToOwnerDTO(Owner owner);
    Owner ownerDTOToOwner(OwnerDTO ownerDTO);
}
