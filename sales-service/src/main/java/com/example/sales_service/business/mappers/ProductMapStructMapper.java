package com.example.sales_service.business.mappers;


import com.example.sales_service.business.repository.model.ProductDAO;
import com.example.sales_service.model.Product;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface ProductMapStructMapper {

  //  @Mapping(target = "productId", source = "productDAO", qualifiedByName = "productId")
    Product productDAOToProduct(ProductDAO productDAO);
    ProductDAO productToDAO(Product product);

}

