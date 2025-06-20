package model.dto;

import model.Moderator;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ModeratorMapper {
    ModeratorMapper INSTANCE = Mappers.getMapper(ModeratorMapper.class);

    ModeratorDTO toDto(Moderator moderator);
    Moderator toEntity(ModeratorDTO dto);
} 