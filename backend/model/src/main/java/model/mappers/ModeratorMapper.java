package model.mappers;

import model.Moderator;
import model.dto.ModeratorDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ModeratorMapper {
    ModeratorMapper INSTANCE = Mappers.getMapper(ModeratorMapper.class);

    ModeratorDTO toDto(Moderator moderator);
    Moderator toEntity(ModeratorDTO dto);
}
