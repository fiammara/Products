package com.example.sales_service.business.mappers;


import com.example.sales_service.business.repository.model.ProductDAO;
import com.example.sales_service.model.Product;
import org.mapstruct.Mapper;

import java.util.List;


@Mapper(componentModel = "spring")
public interface ProductMapStructMapper {

  ProductDAO productToDAO(Product product);

  Product productDAOToProduct(ProductDAO dao);

  List<Product> productDAOToProductList(List<ProductDAO> daoList);

  List<ProductDAO> productToDAOList(List<Product> productList);


}


