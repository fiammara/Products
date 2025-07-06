package com.example.products.business.repository;


import com.example.products.business.repository.model.ProductDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<ProductDAO, Long> {


    List<ProductDAO> findByName(String name);

    @Query(value = "select * from product p where p.name like %:keyword% or p.category like %:keyword%", nativeQuery = true)
    List<ProductDAO> findByKeyword(@Param("keyword") String keyword);


}
