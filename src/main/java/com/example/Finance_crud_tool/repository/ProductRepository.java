package com.example.Finance_crud_tool.repository;

import com.example.Finance_crud_tool.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT MAX(p.account_number) FROM Product p WHERE p.account_number LIKE :prefix%")
    String findMaxAccountNumberStartingWith(@Param("prefix") String prefix);

}
