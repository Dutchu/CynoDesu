package edu.weeia.cynodesu.api.v1.mapper;
import edu.weeia.cynodesu.api.v1.model.CreateDogDTO;
import edu.weeia.cynodesu.api.v1.model.GetDogDTO;
import edu.weeia.cynodesu.domain.Dog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DogMapper {
    DogMapper INSTANCE = Mappers.getMapper(DogMapper.class);

    GetDogDTO dogToDogDTO(Dog dog);
    Dog dogDTOToDog(GetDogDTO getDogDTO);
    @Mapping(source = "ownerId", target = "owner.id")
    Dog saveDogDTOToDog(CreateDogDTO createDogDTO);
}
