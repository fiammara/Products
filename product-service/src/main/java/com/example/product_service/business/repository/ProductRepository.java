package com.example.product_service.business.repository;


import com.example.product_service.business.repository.model.ProductDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<ProductDAO, Long> {


    List<ProductDAO> findByName(String name);

    @Query("SELECT p FROM ProductDAO p WHERE str(p.name) LIKE :keyword OR str(p.category) LIKE :keyword")
    List<ProductDAO> findByKeyword(@Param("keyword") String keyword);

}
