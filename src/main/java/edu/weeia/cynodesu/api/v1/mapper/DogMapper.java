package edu.weeia.cynodesu.api.v1.mapper;

import edu.weeia.cynodesu.api.v1.model.*;
import edu.weeia.cynodesu.domain.Dog;

import edu.weeia.cynodesu.domain.Sex;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.*;
import java.time.temporal.TemporalAmount;
import java.util.Base64;

@Mapper
public interface DogMapper {
    DogMapper INSTANCE = Mappers.getMapper(DogMapper.class);

    GetDogDTO dogToDogDTO(Dog dog);

    Dog dogDTOToDog(GetDogDTO getDogDTO);

    Dog saveDogDTOToDog(CreateDogDTO createDogDTO);


    @Mapping(source = "createdByUser.id", target = "userId")
    @Mapping(source = "createdByUser.username", target = "username")
    @Mapping(source = "sex", target = "sex", qualifiedByName = "sexToString")
    @Mapping(source = "breed", target = "breed")
    @Mapping(source = "dob", target = "age", qualifiedByName = "calculateYearsFromDate")
    @Mapping(source = "icon", target = "icon", qualifiedByName = "stringFromBytes")
    @Mapping(source = "createdDate", target = "createdDate", qualifiedByName = "instantToLocalDateTime")
    DogPreviewDTO mapForPreviewListing(Dog dog);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "createdByUser.id", target = "userId")
    @Mapping(source = "createdByUser.username", target = "userName")
    @Mapping(source = "createdDate", target = "createdAt", qualifiedByName = "instantToLocalDateTime")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "breedingFacility.id", target = "breedingFacilityId")
    @Mapping(source = "content", target = "content")
    @Mapping(source = "icon", target = "icon", qualifiedByName = "stringFromBytes")
    @Mapping(source = "attachedFiles", target = "docs")
    @Mapping(source = "scores", target = "scores")
    DogDetailDto mapForDetailListing(Dog dog);

    @Named("instantToLocalDateTime")
    public static LocalDateTime mapInstantToLocalDateTime(Instant instant) {
        return instant == null ? null : LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    @Named("calculateYearsFromDate")
    default Integer calculateYearsFromDate(LocalDate date) {
        return date == null ? null :
                Period.between(date, LocalDate.now()).getYears();

    }

    @Named("substringArticleContent")//will do custom transformation once we move to  Markdown format
    default String substringArticleContent(String content) {
        if (content.length() < 200) {
            return content;
        }

        return content.substring(0, 200) + " ...";
    }

    @Named("stringFromBytes")
    public static String stringFromBytes(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    @Named("sexToString")
    public static String sexToString(Sex sex) {
        return switch(sex) {
            case STUD -> "Male";
            case BITCH -> "FEMALE";
        };
    }
}
