package com.universidade.moedaestudantil.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.universidade.moedaestudantil.model.EmpresaParceira;

public interface EmpresaParceiraRepository extends JpaRepository<EmpresaParceira, Long> {
    Optional<EmpresaParceira> findByEmail(String email);
    Optional<EmpresaParceira> findByCnpj(String cnpj);
    boolean existsByEmail(String email);
    boolean existsByCnpj(String cnpj);
}