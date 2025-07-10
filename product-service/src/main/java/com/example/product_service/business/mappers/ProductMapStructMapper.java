package com.example.product_service.business.mappers;


import com.example.product_service.business.repository.model.ProductDAO;
import com.example.product_service.model.Product;
import org.mapstruct.Mapper;

import java.util.List;


@Mapper(componentModel = "spring")
public interface ProductMapStructMapper {

    ProductDAO productToDAO(Product product);

    Product productDAOToProduct(ProductDAO dao);

    List<Product> productDAOToProductList(List<ProductDAO> daoList);

    List<ProductDAO> productToDAOList(List<Product> productList);


}

