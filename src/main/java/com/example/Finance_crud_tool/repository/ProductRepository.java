package com.example.Finance_crud_tool.repository;

import com.example.Finance_crud_tool.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT MAX(p.accountNumber) FROM Product p WHERE p.accountNumber LIKE :prefix%")
    String findMaxAccountNumberStartingWith(@Param("prefix") String prefix);

    Optional<Product> findByAccountNumber(String accountNumber);

}