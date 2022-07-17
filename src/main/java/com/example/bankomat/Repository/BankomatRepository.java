package com.example.bankomat.Repository;

import com.example.bankomat.Model.Bankomat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BankomatRepository extends JpaRepository<Bankomat, Integer> {
    boolean existsByPrivateNumber(String privateNumber);
    Optional<Bankomat> findByPrivateNumber(String pvNumber);
}
