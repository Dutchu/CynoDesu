package edu.weeia.cynodesu.api.v1.mapper;

import edu.weeia.cynodesu.domain.ReceivedFile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ReceivedFileMapper {
    ReceivedFileMapper INSTANCE = Mappers.getMapper(ReceivedFileMapper.class);

//    @Mapping(source = "data", target = "data")
//    ReceivedFileDto toDto(ReceivedFile entity);
//
//    @Mapping(source = "data", target = "data")
//    ReceivedFile toEntity(ReceivedFileDto dto);
}
