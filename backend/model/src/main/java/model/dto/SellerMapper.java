package model.dto;

import model.Seller;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SellerMapper {
    SellerMapper INSTANCE = Mappers.getMapper(SellerMapper.class);

    SellerDTO toDto(Seller seller);
    Seller toEntity(SellerDTO dto);
} 