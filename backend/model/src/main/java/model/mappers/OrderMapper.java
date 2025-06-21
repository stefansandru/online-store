package model.mappers;

import model.Order;
import model.dto.OrderDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(target = "buyerId", source = "buyer.id")
    OrderDTO toDto(Order order);
    Order toEntity(OrderDTO dto);
}
