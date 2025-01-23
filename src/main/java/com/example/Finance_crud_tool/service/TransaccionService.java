package com.example.Finance_crud_tool.service;


import com.example.Finance_crud_tool.dto.ConsignacionRequest;
import com.example.Finance_crud_tool.dto.RetiroRequest;
import com.example.Finance_crud_tool.dto.TransferenciaRequest;
import com.example.Finance_crud_tool.entity.Transaccion;

public interface TransaccionService{


    Transaccion processTransaction(TransferenciaRequest request);

    Transaccion processConsignacion(ConsignacionRequest request);


    Transaccion processRetiro(RetiroRequest request);
}
