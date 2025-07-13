package com.example.product_service.business.mappers;


import com.example.product_service.business.repository.model.ProductDAO;
import com.example.product_service.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "spring")
public interface ProductMapStructMapper {

    ProductDAO productToDAO(Product product);

    @Mapping(source = "initialQuantity", target = "initial_quantity")
    Product productDAOToProduct(ProductDAO dao);

    @Mapping(source = "initial_quantity", target = "initialQuantity")
    ProductDAO ProductToDAO(Product product);

    List<Product> productDAOToProductList(List<ProductDAO> daoList);

    List<ProductDAO> productToDAOList(List<Product> productList);


}

