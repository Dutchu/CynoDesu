package edu.weeia.cynodesu.api.v1.mapper;

import edu.weeia.cynodesu.api.v1.model.DogPreviewDTO;
import edu.weeia.cynodesu.api.v1.model.CreateDogDTO;
import edu.weeia.cynodesu.api.v1.model.GetDogDTO;
import edu.weeia.cynodesu.domain.Dog;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DogMapper {
    DogMapper INSTANCE = Mappers.getMapper(DogMapper.class);

    GetDogDTO dogToDogDTO(Dog dog);
    Dog dogDTOToDog(GetDogDTO getDogDTO);
    @Mapping(source = "ownerId", target = "owner.id")
    Dog saveDogDTOToDog(CreateDogDTO createDogDTO);


    @Mapping(source = "createdByUser.id", target = "userId")
    @Mapping(source = "createdByUser.username", target = "username")
    @Mapping(source = "attachedFiles", target = "files")
    @Mapping(target = "content", qualifiedByName = {"substringArticleContent"})
    DogPreviewDTO mapForPreviewListing(Dog dog);

    @Named("substringArticleContent")//will do custom transformation once we move to  Markdown format
    default String substringArticleContent(String content) {
        if (content.length() < 200) {
            return content;
        }

        return content.substring(0, 200) + " ...";
    }
}
