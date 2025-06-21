package model.mappers;

import model.Product;
import model.dto.ProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(target = "categoryName", source = "category.name")
    ProductDTO toDto(Product product);
    // For reverse mapping, you may need a custom method or expression if you want to map categoryName back to a Category object
    Product toEntity(ProductDTO dto);
}
