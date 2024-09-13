package edu.weeia.cynodesu.api.v1.mapper;

import edu.weeia.cynodesu.api.v1.model.CompetitionCreateForm;
import edu.weeia.cynodesu.api.v1.model.CompetitionPreviewDto;
import edu.weeia.cynodesu.api.v1.model.CompetitionPreviewProjection;
import edu.weeia.cynodesu.domain.Competition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

@Mapper
public interface CompetitionMapper {
    CompetitionMapper INSTANCE = Mappers.getMapper(CompetitionMapper.class);

    @Mapping(target = "venueDate", source = "date", qualifiedByName = "localDateTimeToInstant")
    Competition mapToEntity(CompetitionCreateForm form);

    @Mapping(target = "dateTime", source = "venueDate", qualifiedByName = "instantToLocalDateTime")
    CompetitionPreviewDto mapToPreview(Competition competition);

    @Mapping(target = "dateTime", source = "venueDate", qualifiedByName = "instantToLocalDateTime")
    CompetitionPreviewDto mapToPreview(CompetitionPreviewProjection projection);

    @Named("localDateTimeToInstant")
    default Instant mapLocalDateTimeToInstant(LocalDateTime date) {
        return date == null ? null : date.toInstant(ZoneOffset.UTC);
    }

    @Named("instantToLocalDateTime")
    default LocalDateTime mapInstantToLocalDateTime(Instant instant) {
        return instant == null ? null : LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }
}
