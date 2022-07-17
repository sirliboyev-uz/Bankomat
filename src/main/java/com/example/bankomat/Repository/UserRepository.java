package com.example.bankomat.Repository;

import com.example.bankomat.Model.Bankomat;
import com.example.bankomat.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Integer>{
    Optional<Users> findByUsername(String username);
    Optional<Users> findByBankomat(Bankomat bankomat);
    boolean existsByUsername(String username);
}
