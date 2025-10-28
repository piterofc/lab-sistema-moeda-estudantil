package com.universidade.moedaestudantil.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.universidade.moedaestudantil.model.EmpresaParceira;

public interface EmpresaParceiraRepository extends JpaRepository<EmpresaParceira, Long> {
}