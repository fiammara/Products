package com.example.products.business.mappers;

import com.example.products.model.Product;
import com.example.products.business.repository.model.ProductDAO;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface ProductMapStructMapper {

  //  @Mapping(target = "productId", source = "productDAO", qualifiedByName = "productId")
    Product productDAOToProduct(ProductDAO productDAO);
    ProductDAO productToDAO(Product product);

}

