package com.example.product_service.business.mappers;


import com.example.product_service.business.repository.model.ProductDAO;
import com.example.product_service.model.Category;
import com.example.product_service.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;


import java.util.List;


@Mapper(componentModel = "spring")
public interface ProductMapStructMapper {


    Product productDAOToProduct(ProductDAO dao);

    ProductDAO productToDAO(Product product);

    List<Product> productDAOToProductList(List<ProductDAO> daoList);
    List<ProductDAO> productToDAOList(List<Product> productList);
}
