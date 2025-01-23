package com.example.Finance_crud_tool.service;


import com.example.Finance_crud_tool.dto.TransferenciaRequest;
import com.example.Finance_crud_tool.entity.Transaccion;

public interface TransaccionService{


    Transaccion processTransaction(TransferenciaRequest request);
}
