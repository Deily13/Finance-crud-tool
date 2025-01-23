package com.example.Finance_crud_tool.repository;

import com.example.Finance_crud_tool.entity.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {

}
