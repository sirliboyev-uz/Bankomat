package com.example.bankomat.Repository;

import com.example.bankomat.Model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Roles, Integer> {

}
