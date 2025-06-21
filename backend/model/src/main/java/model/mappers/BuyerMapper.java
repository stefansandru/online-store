package model.mappers;

import model.Buyer;
import model.dto.BuyerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BuyerMapper {
    BuyerMapper INSTANCE = Mappers.getMapper(BuyerMapper.class);

    BuyerDTO toDto(Buyer buyer);
    Buyer toEntity(BuyerDTO dto);
}
